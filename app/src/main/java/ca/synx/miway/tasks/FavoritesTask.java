/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.interfaces.IFavoritesTask;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class FavoritesTask extends AsyncTask<String, Void, List<Favorite>> {

    private DatabaseHandler mDatabaseHandler;
    private IFavoritesTask mListener;

    public FavoritesTask(DatabaseHandler databaseHandler, IFavoritesTask favoriteListener) {
        this.mDatabaseHandler = databaseHandler;
        this.mListener = favoriteListener;
    }

    @Override
    protected List<Favorite> doInBackground(String... params) {

        List<Favorite> favorites = new ArrayList<Favorite>();

        try {
            favorites = new StorageHandler(mDatabaseHandler).getFavorites();
        } catch (Exception e) {
            Log.e("FavoritesTask:doInBackground", e.getMessage());
            e.printStackTrace();
        }

        return favorites;
    }

    @Override
    protected void onPostExecute(List<Favorite> favorites) {
        super.onPostExecute(favorites);

        mListener.onFavoritesTaskComplete(favorites);
    }
}
