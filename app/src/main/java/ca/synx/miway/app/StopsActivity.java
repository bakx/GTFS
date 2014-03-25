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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.adapters.ListItemAdapter;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;

public class StopsActivity extends Activity {

    ListView mStopsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listview);

        // Set up class..
        mStopsListView = (ListView) findViewById(R.id.listView);

        // Get the message from the intent
        Intent intent = getIntent();
        Route route = (Route) intent.getSerializableExtra("routeData");

        // Update label.
        setTitle(String.format(getTitle().toString(), route.routeNumber, route.routeHeading));

        // Execute task.
        new GTFSStopTask(this).execute(route);
    }

    private class GTFSStopTask extends AsyncTask<Route, Void, List<Stop>> {

        private Context context;

        public GTFSStopTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Stop> doInBackground(Route... params) {

            Route route = params[0];

            List<Stop> stops = new ArrayList<Stop>();

            String data = (new GTFSDataExchange("miway").getStopsData(route.routeNumber, route.routeHeading));

            try {
                stops = GTFSParser.getStops(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return stops;
        }

        @Override
        protected void onPostExecute(List<Stop> stops) {
            super.onPostExecute(stops);

            ListItemAdapter adapter = new ListItemAdapter(stops, context);
            mStopsListView.setAdapter(adapter);
            mStopsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Get tag from clicked view.
                    Stop stop = (Stop) view.getTag(R.id.tag_id_2);

                    // Create new intent.
                    Intent intent = new Intent(context, StopTimesActivity.class);

                    // Pass selected data.
                    intent.putExtra("stopData", stop);

                    // Start the intent.
                    startActivity(intent);
                }
            });
        }
    }
}