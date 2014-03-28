/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.synx.miway.Util.FavoritesHandler;
import ca.synx.miway.Util.GTFSDataExchange;
import ca.synx.miway.Util.GTFSParser;
import ca.synx.miway.adapters.SingleItemAdapter;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;

public class StopTimesActivity extends ActionBarActivity {
    static final String FAVORITE_DATA = "favoriteData";
    static final String STOP_DATA = "stopData";

    Stop mStop;
    Favorite mFavorite;

    TextView mRouteName;
    TextView mStopName;
    ListView mNextStopTimesListView;
    ListView mStopTimesListView;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoptimes);

        // Resume?
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mStop = (Stop) intent.getSerializableExtra(STOP_DATA);
        } else {
            mStop = (Stop) savedInstanceState.getSerializable(STOP_DATA);
            mFavorite = (Favorite) savedInstanceState.getSerializable(FAVORITE_DATA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set up class..
        mNextStopTimesListView = (ListView) findViewById(R.id.nextStopTimesListView);
        mStopTimesListView = (ListView) findViewById(R.id.stopTimesListView);

        mRouteName = (TextView) findViewById(R.id.routeName);
        mStopName = (TextView) findViewById(R.id.stopName);

        mRouteName.setText(mStop.getRoute().getFull());
        mStopName.setText(mStop.getFull());

        // Check favorite..
        mFavorite = new Favorite(mStop);
        new FavoritesHandler(this).isFavorite(mFavorite);

        // Execute task.
        new GTFSStopTimesTask(this).execute(mStop);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STOP_DATA, mStop);
        savedInstanceState.putSerializable(FAVORITE_DATA, mFavorite);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStop = (Stop) savedInstanceState.getSerializable(STOP_DATA);
        mFavorite = (Favorite) savedInstanceState.getSerializable(FAVORITE_DATA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stoptimes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mFavorite.getId() > 0) {
            menu.findItem(R.id.action_add_favorite).setVisible(false);
            menu.findItem(R.id.action_remove_favorite).setVisible(true);
        } else {
            menu.findItem(R.id.action_add_favorite).setVisible(true);
            menu.findItem(R.id.action_remove_favorite).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_favorite:

                new FavoritesHandler(getApplicationContext())
                        .saveFavorite(mFavorite);

                handleFavorite();

                Toast.makeText(this, R.string.added_favorites, Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_remove_favorite:

                new FavoritesHandler(getApplicationContext())
                        .removeFavorite(mFavorite);

                handleFavorite();

                Toast.makeText(this, R.string.removed_favorites, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleFavorite() {
        supportInvalidateOptionsMenu();
    }

    private class GTFSStopTimesTask extends AsyncTask<Stop, Void, List<StopTime>> {

        private Context mContext;

        public GTFSStopTimesTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected List<StopTime> doInBackground(Stop... params) {

            Stop stop = params[0];

            List<StopTime> stopTimes = new ArrayList<StopTime>();

            String data = (new GTFSDataExchange("miway").getStopTimesData(stop));

            if (data == "") {
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_SHORT).show();
                return stopTimes;
            }

            try {
                stopTimes = GTFSParser.getStopTimes(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (StopTime stopTime : stopTimes)
                stopTime.setStop(stop);

            return stopTimes;
        }

        @Override
        protected void onPostExecute(List<StopTime> stopTimes) {
            super.onPostExecute(stopTimes);

            SimpleDateFormat currentDateFormat = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat newDateFormat = new SimpleDateFormat("hh:mm aa");

            for (StopTime stopTime : stopTimes) {
                try {
                    stopTime.departureTime = newDateFormat.format(
                            currentDateFormat.parse(stopTime.departureTime)
                    );
                } catch (Exception e) {
                    Log.e("StopTime Parse error", e.getMessage());
                }
            }

            List<StopTime> nearestTime = getNearestStopTimes(stopTimes, 5);

            SingleItemAdapter<StopTime> adapter = new SingleItemAdapter<StopTime>(nearestTime, R.layout.listview_item_single, false, mContext);
            mNextStopTimesListView.setAdapter(adapter);

            adapter = new SingleItemAdapter<StopTime>(stopTimes, R.layout.listview_item_single, false, mContext);
            mStopTimesListView.setAdapter(adapter);
        }

        protected List<StopTime> getNearestStopTimes(List<StopTime> source, int targetCount) {
            List<StopTime> nearestStopTimes = new ArrayList<StopTime>();

            Date currentDate = null;

            //
            // Get current time stamp as date
            //

            try {
                currentDate = new SimpleDateFormat("hh:mm aa").parse(
                        new SimpleDateFormat("hh:mm aa").format(
                                Calendar.getInstance().getTime()
                        )
                );
            } catch (Exception e) {
                Log.v("getNearestStopTimes currentDate error", e.getMessage());
                return nearestStopTimes;
            }

            //
            // Loop through all departure times to find best match
            //

            for (StopTime stopTime : source) {

                try {

                    Date stopDate = new SimpleDateFormat("hh:mm aa").parse(
                            stopTime.departureTime
                    );

                    long timeDifference = (stopDate.getTime() - currentDate.getTime()) / (60 * 1000);

                    // If vehicle is already gone, continue.
                    if (timeDifference < 0)
                        continue;

                    stopTime.departureTime = stopTime.departureTime + " (" + String.valueOf(timeDifference) + " min)";

                    // Since time is already sorted on server, all objects that come after the
                    // time difference is 0, are valid. Keep adding them until we reach 'targetCount'
                    nearestStopTimes.add(stopTime);
                } catch (Exception e) {
                    Log.v("getNearestStopTimes departureTime parsing error", e.getMessage());
                }

                if (nearestStopTimes.size() >= targetCount)
                    break;
            }

            return nearestStopTimes;
        }
    }
}