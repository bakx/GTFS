/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ca.synx.miway.adapters.BaseAdapter;
import ca.synx.miway.interfaces.IStopsTask;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.tasks.StopsTask;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class StopsActivity extends Activity implements IStopsTask {

    static final String ROUTE_DATA = "routeData";
    Route mRoute;
    ListView mStopsListView;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

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
            mRoute = (Route) intent.getSerializableExtra("routeData");

        } else {
            mRoute = (Route) savedInstanceState.getSerializable(ROUTE_DATA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update label.
        setTitle(String.format(getTitle().toString(), mRoute.getTitle(), mRoute.getSubtitle()));

        // Execute task.
        new StopsTask(this, mStorageHandler).execute(mRoute);
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
    }
}