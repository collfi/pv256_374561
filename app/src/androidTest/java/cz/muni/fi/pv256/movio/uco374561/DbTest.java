package cz.muni.fi.pv256.movio.uco374561;

import android.test.AndroidTestCase;

import cz.muni.fi.pv256.movio.uco374561.models.Movie;

/**
 * Created by collfi on 22. 11. 2015.
 */
public class DbTest extends AndroidTestCase {

    private static final String TAG = "Movie";

    private Movie mMovie;

    @Override
    protected void setUp() throws Exception {
        mMovie = new Movie();
    }

    @Override
    public void tearDown() throws Exception {
//        mContext.getContentResolver().delete(
//                WorkTimeEntry.CONTENT_URI,
//                null,
//                null
//        );
    }

    public void testGetWorkTimesInDay() throws Exception {
//        List<WorkTime> expectedWorkTimes = new ArrayList<>(2);
//        WorkTime workTime1 = createWorkTime(new DateTime(2015, 5, 26, 1, 0), new DateTime(2015, 5, 26, 5, 0));
//        WorkTime workTime2 = createWorkTime(new DateTime(2015, 5, 26, 6, 0), new DateTime(2015, 5, 26, 7, 0));
//        expectedWorkTimes.add(workTime1);
//        expectedWorkTimes.add(workTime2);
//
//        mManager.createWorkTime(workTime1);
//        mManager.createWorkTime(createWorkTime(new DateTime(2015, 5, 27, 1, 0), new DateTime(2015, 5, 27, 5, 0)));
//        mManager.createWorkTime(workTime2);
//
//        List<WorkTime> workTimes = mManager.getWorkTimesInDay(new LocalDate(2015, 5, 26));
//        Log.d(TAG, workTimes.toString());
//        assertTrue(workTimes.size() == 2);
//        assertEquals(expectedWorkTimes, workTimes);
    }

    public void testGetWorkTimesInInterval() throws Exception {
//        List<WorkTime> expectedWorkTimes = new ArrayList<>(2);
//        WorkTime workTime1 = createWorkTime(new DateTime(2015, 5, 26, 1, 0), new DateTime(2015, 5, 26, 5, 0));
//        WorkTime workTime2 = createWorkTime(new DateTime(2015, 5, 26, 6, 0), new DateTime(2015, 5, 26, 7, 0));
//        WorkTime workTime3 = createWorkTime(new DateTime(2015, 5, 27, 1, 0), new DateTime(2015, 5, 27, 5, 0));
//        expectedWorkTimes.add(workTime1);
//        expectedWorkTimes.add(workTime2);
//        expectedWorkTimes.add(workTime3);
//
//        mManager.createWorkTime(createWorkTime(new DateTime(2015, 5, 25, 2, 0), new DateTime(2015, 5, 25, 5, 0)));
//        mManager.createWorkTime(workTime1);
//        mManager.createWorkTime(workTime2);
//        mManager.createWorkTime(workTime3);
//        mManager.createWorkTime(createWorkTime(new DateTime(2015, 5, 28, 1, 0), new DateTime(2015, 5, 28, 5, 0)));
//
//        List<WorkTime> workTimes = mManager.getWorkTimesInInterval(new Interval(new DateTime(2015, 5, 26, 0, 0), new DateTime(2015, 5, 27, 0, 0)));
//        Log.d(TAG, workTimes.toString());
//        assertTrue(workTimes.size() == 3);
//        assertEquals(expectedWorkTimes, workTimes);
    }

//    private WorkTime createWorkTime(DateTime startDate, DateTime endDate) {
//        WorkTime workTime = new WorkTime();
//        workTime.setStartDate(startDate);
//        workTime.setEndDate(endDate);
//        return workTime;
//    }
}