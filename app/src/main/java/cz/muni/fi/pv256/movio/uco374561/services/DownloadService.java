package cz.muni.fi.pv256.movio.uco374561.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

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

import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


/**
 * Created by collfi on 17. 11. 2015.
 */
public class DownloadService extends IntentService {

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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
            sendBroadcast(i);
        }

    }

    public void notification(String error) {
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        Notification n = new Notification.Builder(this)
                .setContentTitle("Chyba pri ziskavani filmov")
                .setContentText(error)
                .setSmallIcon(android.R.drawable.btn_star)
                .getNotification();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
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


}
