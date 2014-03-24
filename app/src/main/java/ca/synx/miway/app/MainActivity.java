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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.adapters.ListItemAdapter;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;

public class MainActivity extends FragmentActivity  {

    TabHost mTabHost;
    ListView mFavoritesListView;
    ListView mRoutesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init tabs
        initializeTabs();

        initTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Load Tabs
    protected void initializeTabs() {

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        mTabHost.addTab(
                mTabHost.newTabSpec("tab_favorites")
                        .setContent(R.id.tab1)
                        .setIndicator(getResources().getString(R.string.tab_favorites)
                        )
        );

        mTabHost.addTab(
                mTabHost.newTabSpec("tab_routes")
                        .setContent(R.id.tab2)
                        .setIndicator(getResources().getString(R.string.tab_routes)
                        )
        );
    }

    protected void initTabs() {
        mFavoritesListView = (ListView) findViewById(R.id.favoritesListView);
        mRoutesListView = (ListView) findViewById(R.id.routesListView);

        // Load online.
        new GTFSRouteTask(this).execute();
        new GTFSStopTask(this).execute();
    }

    private class GTFSRouteTask extends AsyncTask<String, Void, List<Route>> {

        private Context context;

        public GTFSRouteTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Route> doInBackground(String... params) {
            List<Route> routes = new ArrayList<Route>();
            String data = (new GTFSDataExchange("miway").getRouteData());

            try {
                routes = GTFSParser.getRoutes(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;

        }

        @Override
        protected void onPostExecute(List<Route> routes) {
            super.onPostExecute(routes);

            ListItemAdapter adapter = new ListItemAdapter(routes, context);
            mRoutesListView.setAdapter(adapter);
            mRoutesListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    StopsFragment fragment = new StopsFragment();
                    Bundle args = new Bundle();
                    //args.putInt(StopsFragment.ARG_POSITION, position);
                   // StopsFragment.setArguments(args);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();


                    // When clicked, show a toast with the TextView text
//                    Intent intent = new Intent(context, StopsActivity.class);

//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class GTFSStopTask extends AsyncTask<String, Void, List<Stop>> {

        private Context context;

        public GTFSStopTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Stop> doInBackground(String... params) {
            List<Stop> stops = new ArrayList<Stop>();
            String data = (new GTFSDataExchange("miway").getStopsData("201", "Eastbound"));

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
            mFavoritesListView.setAdapter(adapter);
        }

    }
}