/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.Tasks.StopTimesTask;
import ca.synx.miway.Util.FavoritesHandler;
import ca.synx.miway.adapters.SingleItemAdapter;
import ca.synx.miway.interfaces.ITask;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;

public class StopTimesActivity extends ActionBarActivity implements ITask {
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
        new StopTimesTask(this, 5, this).execute(mStop);
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

    @Override
    public void onTaskComplete(Object[] objects) {

        List<StopTime> nearestTime = (ArrayList<StopTime>) objects[0];
        List<StopTime> stopTimes = (ArrayList<StopTime>) objects[1];

        SingleItemAdapter<StopTime> adapter = new SingleItemAdapter<StopTime>(nearestTime, R.layout.listview_item_single, false, this);
        mNextStopTimesListView.setAdapter(adapter);

        adapter = new SingleItemAdapter<StopTime>(stopTimes, R.layout.listview_item_single, false, this);
        mStopTimesListView.setAdapter(adapter);
    }
}