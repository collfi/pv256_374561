package cz.muni.fi.pv256.movio.uco374561.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by collfi on 5. 12. 2015.
 */
public class SyncService extends Service {

    private static final Object LOCK = new Object();
    private static MovieSyncAdapter sUpdaterSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (LOCK) {
            if (sUpdaterSyncAdapter == null) {
                sUpdaterSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sUpdaterSyncAdapter.getSyncAdapterBinder();
    }
}