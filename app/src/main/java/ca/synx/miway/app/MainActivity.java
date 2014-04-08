/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.List;

import ca.synx.miway.adapters.FavoriteItemAdapter;
import ca.synx.miway.adapters.RouteAdapter;
import ca.synx.miway.helpers.FavoriteHelper;
import ca.synx.miway.interfaces.IDataUpdate;
import ca.synx.miway.interfaces.IFavoritesTask;
import ca.synx.miway.interfaces.IRoutesTask;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.Route;
import ca.synx.miway.tasks.FavoritesTask;
import ca.synx.miway.tasks.RoutesTask;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class MainActivity extends ActionBarActivity implements IFavoritesTask, IRoutesTask, IDataUpdate, SearchView.OnQueryTextListener {

    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

    private ProgressDialog mProgressDialog;

    private FavoriteItemAdapter<Favorite> mFavoritesAdapter;
    private RouteAdapter<Route> mRoutesAdapter;

    private SearchView mSearchView;

    private TabHost mTabHost;
    private ListView mFavoritesListView;
    private ListView mRoutesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mDatabaseHandler = new DatabaseHandler(this);
        mStorageHandler = new StorageHandler(mDatabaseHandler);

        // Init tabs
        initializeTabs();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Display loading dialog.
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_routes));
        mProgressDialog.show();

        // Set up class..
        mFavoritesListView = (ListView) findViewById(R.id.favoritesListView);
        mRoutesListView = (ListView) findViewById(R.id.routesListView);

        // Prepare favorites.
        new FavoritesTask(mDatabaseHandler, this).execute();

        // Fetch Routes from online web service.
        new RoutesTask(this, mStorageHandler).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_map:

                // Create new intent.
                Intent intent = new Intent(mContext, MapActivity.class);

                // Start the intent.
                startActivity(intent);
                return true;

            case R.id.action_refresh:

                // Display loading dialog.
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.loading_stop_times));
                mProgressDialog.show();

                // Prepare favorites.
                new FavoritesTask(mDatabaseHandler, this).execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onFavoritesTaskComplete(List<Favorite> favorites) {
        mFavoritesAdapter = new FavoriteItemAdapter<Favorite>(mContext, favorites, R.layout.listview_item_favorite, true);
        mFavoritesListView.setAdapter(mFavoritesAdapter);
        mFavoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get tag from clicked view.
                Favorite favorite = (Favorite) view.getTag(R.id.tag_id_2);

                // Create new intent.
                Intent intent = new Intent(mContext, StopTimesActivity.class);

                // Pass selected data.
                intent.putExtra("stopData", favorite.getStop());

                // Start the intent.
                startActivity(intent);
            }
        });

        // Get the stop times for all favorites.
        for (Favorite favorite : favorites) {
            new FavoriteHelper(mContext, this, mStorageHandler, favorite).loadStopTimes();
        }
    }

    @Override
    public void onRoutesTaskComplete(List<Route> routes) {

        // Check if routes object contains data.
        if (routes == null) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
            return;
        }

        mRoutesAdapter = new RouteAdapter<Route>(mContext, routes, R.layout.listview_item_basic, true);
        mRoutesListView.setAdapter(mRoutesAdapter);
        mRoutesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get tag from clicked view.
                Route route = (Route) view.getTag(R.id.tag_id_2);

                // No point in starting a new intent without a route. This can happen when a search
                // is performed and the user clicks on the 'no results found' list item.
                if (route == null)
                    return;

                // Create new intent.
                Intent intent = new Intent(mContext, StopsActivity.class);

                // Pass selected data.
                intent.putExtra("routeData", route);

                // Start the intent.
                startActivity(intent);
            }
        });

        // Dismiss the progress dialog (if any)
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void onDataUpdate() {
        mFavoritesAdapter.notifyDataSetChanged();

        // Dismiss the progress dialog (if any)
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mRoutesAdapter.getFilter().filter(s);
        return false;
    }
}