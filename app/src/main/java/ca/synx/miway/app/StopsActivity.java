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
import android.widget.Toast;

import java.util.List;

import ca.synx.miway.adapters.StopAdapter;
import ca.synx.miway.interfaces.IStopsTask;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.tasks.RouteStopsTask;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class StopsActivity extends ActionBarActivity implements IStopsTask, SearchView.OnQueryTextListener {

    static final String sROUTE_DATA = "routeData";

    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

    private StopAdapter<Stop> mStopsAdapter;

    private ProgressDialog mProgressDialog;

    private SearchView mSearchView;

    private ListView mStopsListView;
    private Route mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listview);

        mContext = this;

        // Set up class..
        mStopsListView = (ListView) findViewById(R.id.listView);

        // Resume?
        if (savedInstanceState == null) {

            // Get the message from the intent
            Intent intent = getIntent();
            mRoute = (Route) intent.getSerializableExtra(sROUTE_DATA);

        } else {
            mRoute = (Route) savedInstanceState.getSerializable(sROUTE_DATA);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stops, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_map:

                // Create new intent.
                Intent intent = new Intent(mContext, MapActivity.class);

                // Pass route to map.
                intent.putExtra(sROUTE_DATA, mRoute);

                // Start the intent.
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mContext = this;
        mDatabaseHandler = new DatabaseHandler(this);
        mStorageHandler = new StorageHandler(mDatabaseHandler);

        // Display loading dialog.
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_stops));
        mProgressDialog.show();

        // Update the title of the activity.
        setTitle(String.format(
                getString(R.string.stops),
                mRoute.getTitle(),
                mRoute.getSubtitle()
        ));

        // Execute task.
        new RouteStopsTask(this, mStorageHandler).execute(mRoute);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable(sROUTE_DATA, mRoute);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mRoute = (Route) savedInstanceState.getSerializable(sROUTE_DATA);
    }


    @Override
    public void onStopsTaskComplete(List<Stop> stops) {

        // Check if stops object contain data.
        if (stops == null) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
            return;
        }

        mStopsAdapter = new StopAdapter<Stop>(mContext, stops, R.layout.listview_item_basic, true);
        mStopsListView.setAdapter(mStopsAdapter);
        mStopsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get tag from clicked view.
                Stop stop = (Stop) view.getTag(R.id.tag_id_2);

                // Create new intent.
                Intent intent = new Intent(mContext, StopTimesActivity.class);

                // Pass selected data.
                intent.putExtra("stopData", stop);

                // Start the intent.
                startActivity(intent);
            }
        });

        // This function is called latest. Once this is complete, the process loading dialog can be dismissed.
        mProgressDialog.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mStopsAdapter.getFilter().filter(s);
        return false;
    }
}