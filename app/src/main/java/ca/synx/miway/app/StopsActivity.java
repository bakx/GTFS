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
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ca.synx.miway.adapters.BaseAdapter;
import ca.synx.miway.interfaces.IStopsTask;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.tasks.RouteStopsTask;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class StopsActivity extends ActionBarActivity implements IStopsTask {

    static final String ROUTE_DATA = "routeData";
    Route mRoute;
    ListView mStopsListView;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

    private ProgressDialog mProgressDialog;


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
            mRoute = (Route) intent.getSerializableExtra(ROUTE_DATA);

        } else {
            mRoute = (Route) savedInstanceState.getSerializable(ROUTE_DATA);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        savedInstanceState.putSerializable(ROUTE_DATA, mRoute);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mRoute = (Route) savedInstanceState.getSerializable(ROUTE_DATA);
    }


    @Override
    public void onStopsTaskComplete(List<Stop> stops) {

        // Check if stops object contain data.
        if (stops == null) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
            return;
        }

        BaseAdapter<Stop> adapter = new BaseAdapter<Stop>(stops, R.layout.listview_item_basic, true, mContext);
        mStopsListView.setAdapter(adapter);
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
}