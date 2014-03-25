/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.adapters.ListItemAdapter;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;

public class StopTimesActivity extends Activity {

    static final String STOP_DATA = "stopData";
    Stop mStop;
    ListView mStopTimesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listview);

        // Set up class..
        mStopTimesListView = (ListView) findViewById(R.id.listView);

        // Resume?
        if (savedInstanceState == null) {

            // Get the message from the intent
            Intent intent = getIntent();
            mStop = (Stop) intent.getSerializableExtra(STOP_DATA);

        } else {
            mStop = (Stop) savedInstanceState.getSerializable(STOP_DATA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update label.
        setTitle(String.format(getTitle().toString(), mStop.getTitle(), mStop.getSubtitle()));

        // Execute task.
        new GTFSStopTimesTask(this).execute(mStop);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STOP_DATA, mStop);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStop = (Stop) savedInstanceState.getSerializable(STOP_DATA);
    }

    private class GTFSStopTimesTask extends AsyncTask<Stop, Void, List<StopTime>> {

        private Context context;

        public GTFSStopTimesTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<StopTime> doInBackground(Stop... params) {

            Stop stop = params[0];

            List<StopTime> stopTimes = new ArrayList<StopTime>();

            String data = (new GTFSDataExchange("miway").getStopTimesData(stop));

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

            ListItemAdapter adapter = new ListItemAdapter(stopTimes, false, context);
            mStopTimesListView.setAdapter(adapter);
        }
    }
}