package cz.muni.fi.pv256.movio.uco374561.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.db.MovieContract;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.providers.MovieManager;
import cz.muni.fi.pv256.movio.uco374561.services.Download;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by collfi on 5. 12. 2015.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private Context mContext;


    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setClient(new OkClient())
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(final RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                    }
                })
                .build();
        Download d = adapter.create(Download.class);

        try {
            MovieManager manager = new MovieManager(mContext);
            ArrayList<Movie> l = manager.getAll();

            for (Movie m : l) {
                Movie downloaded = d.getMovie(m.getMovieId(), "c331638cd30b7ab8a4b73dedbbb62193");
                if (!downloaded.equals(m)) {
                    manager.updateMovie(downloaded);
                    notification(downloaded.getTitle(), "Movie updated");
                }
            }
        } catch (Exception e) {
            Log.e("sync service", "Exception. " + e.getLocalizedMessage());
            notification("Error while getting data", "Download Error");
        }
    }


    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getAccount(), "cz.muni.fi.pv256.movio.uco374561.providers.MovieProvider", bundle);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getAccount();
        String authority = MovieContract.CONTENT_AUTHORITY;

        ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);

    }

    public static Account getAccount() {
        return new Account("account", "cz.muni.fi.pv256.movio.uco374561.providers.MovieProvider");
    }

    public void notification(String message, String title) {
        Notification n = new Notification.Builder(getContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.btn_star)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .getNotification();

        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one if the
     * fake account doesn't exist yet.  If we make a new account, we call the onAccountCreated
     * method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account account = getAccount();

        if (accountManager.addAccountExplicitly(account, null, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, MovieContract.CONTENT_AUTHORITY, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, MovieContract.CONTENT_AUTHORITY, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(
                    account, MovieContract.CONTENT_AUTHORITY, new Bundle(), 10);

        }
        // If the password doesn't exist, the account doesn't exist


        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */


        return account;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, "cz.muni.fi.pv256.movio.uco374561.providers.MovieProvider", true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
