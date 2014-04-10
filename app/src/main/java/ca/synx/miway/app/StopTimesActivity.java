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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.synx.miway.adapters.SingleItemAdapter;
import ca.synx.miway.interfaces.INextStopTimesTask;
import ca.synx.miway.interfaces.IStopTimesTask;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;
import ca.synx.miway.tasks.NextStopTimesTask;
import ca.synx.miway.tasks.StopTimesTask;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class StopTimesActivity extends ActionBarActivity implements IStopTimesTask, INextStopTimesTask {
    static final String sFAVORITE_DATA = "favoriteData";
    static final String sSTOP_DATA = "stopData";

    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

    private ProgressDialog mProgressDialog;

    private Stop mStop;
    private Favorite mFavorite;

    private TextView mRouteName;
    private TextView mStopName;
    private ListView mNextStopTimesListView;
    private GridView mStopTimesGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoptimes);

        // Resume?
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mStop = (Stop) intent.getSerializableExtra(sSTOP_DATA);
        } else {
            mStop = (Stop) savedInstanceState.getSerializable(sSTOP_DATA);
            mFavorite = (Favorite) savedInstanceState.getSerializable(sFAVORITE_DATA);
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
        mProgressDialog.setMessage(getString(R.string.loading_stop_times));
        mProgressDialog.show();

        // Set up class..
        mNextStopTimesListView = (ListView) findViewById(R.id.nextStopTimesListView);
        mStopTimesGridView = (GridView) findViewById(R.id.stopTimesGridView);

        mRouteName = (TextView) findViewById(R.id.routeName);
        mStopName = (TextView) findViewById(R.id.stopName);

        mRouteName.setText(mStop.getRoute().getFull());
        mStopName.setText(mStop.getFull());

        // Check favorite..
        mFavorite = new Favorite(mStop);
        mStorageHandler.isFavorite(mFavorite);

        // Execute task.
        new StopTimesTask(mContext, this, mStorageHandler).execute(mStop);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(sSTOP_DATA, mStop);
        savedInstanceState.putSerializable(sFAVORITE_DATA, mFavorite);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStop = (Stop) savedInstanceState.getSerializable(sSTOP_DATA);
        mFavorite = (Favorite) savedInstanceState.getSerializable(sFAVORITE_DATA);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stoptimes, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_add_favorite:

                mStorageHandler.saveFavorite(mFavorite);

                handleFavorite();

                Toast.makeText(mContext, R.string.added_favorites, Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_remove_favorite:

                mStorageHandler.removeFavorite(mFavorite);

                handleFavorite();

                Toast.makeText(mContext, R.string.removed_favorites, Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_map:

                // Create new intent.
                Intent intent = new Intent(mContext, MapActivity.class);

                // Pass stop to map.
                intent.putExtra(sSTOP_DATA, mStop);

                // Start the intent.
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleFavorite() {
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onStopTimesTaskComplete(List<StopTime> stopTimes) {

        // Check if any of the stop times object contain data.
        if (stopTimes == null) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
            return;
        }

        // Set adapter that loads the list view.
        SingleItemAdapter adapter = new SingleItemAdapter<StopTime>(mContext, stopTimes, R.layout.listview_item_single, false);
        mStopTimesGridView.setAdapter(adapter);

        // Execute another ASyncTask that calculates the next stop times.
        new NextStopTimesTask(mContext, this, 5).execute(stopTimes);
    }

    @Override
    public void onNextStopTimesTaskComplete(List<StopTime> nextStopTimes) {
        SingleItemAdapter<StopTime> adapter = new SingleItemAdapter<StopTime>(mContext, nextStopTimes, R.layout.listview_item_single, false);
        mNextStopTimesListView.setAdapter(adapter);

        // This function is called latest. Once this is complete, the process loading dialog can be dismissed.
        mProgressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}