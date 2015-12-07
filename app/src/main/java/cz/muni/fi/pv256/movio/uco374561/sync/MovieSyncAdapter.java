package cz.muni.fi.pv256.movio.uco374561.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.db.MovieContract;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.services.Download;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by collfi on 5. 12. 2015.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter{
    public static final int SYNC_INTERVAL = 10;// 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;


    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public MovieSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i("zzzzzz", "perform sync");
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setClient(new OkClient())
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(final RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                    }
                })
                .build();
        //http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2015-11-09&primary_
        // release_date.lte=2015-11-16&sort_by=avg_rating.desc&api_key=" + "c331638cd30b7ab8a4b73dedbbb62193
        Download d = adapter.create(Download.class);
        ArrayList<Movie> l = new ArrayList<>();
        try {
            l.addAll(d.getMovies2("2015-11-09", "2015-11-16", "avg_rating.desc",
                    "c331638cd30b7ab8a4b73dedbbb62193"));

            l.addAll(d.getMovies1("c331638cd30b7ab8a4b73dedbbb62193"));
        } catch (Exception e) {
            notification(e.toString());
        }
        if (l.size() == 0) {
            notification("No movies");
        } else {
            Intent i = new Intent("cz.muni.fi.movio");
            i.putParcelableArrayListExtra("movies", l);
//            sendBroadcast(i);
            
            //todo cosi spravit
        }
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void syncImmediately(Context context) {
        Toast.makeText(context, "SYNCCCCCCCCC si", Toast.LENGTH_SHORT).show();
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

    public static Account getAccount(){
        return new Account("account", "cz.muni.fi.pv256.movio.uco374561.providers.MovieProvider");
    }

    public class ItemTypeAdapterFactory implements TypeAdapterFactory {

        public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

            return new TypeAdapter<T>() {

                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                public T read(JsonReader in) throws IOException {

                    JsonElement jsonElement = elementAdapter.read(in);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.has("results") && jsonObject.get("results").isJsonArray()) {
                            jsonElement = jsonObject.get("results");
//                            jsonElement = jsonObject.get("data");
                        }
                    }

                    return delegate.fromJsonTree(jsonElement);
                }
            }.nullSafe();
        }
    }

    public void notification(String error) {
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        Notification n = new Notification.Builder(getContext())
                .setContentTitle("Chyba pri ziskavani filmov")
                .setContentText(error)
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
}
