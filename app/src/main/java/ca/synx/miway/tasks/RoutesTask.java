/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

import ca.synx.miway.interfaces.IRoutesTask;
import ca.synx.miway.models.Route;
import ca.synx.miway.util.GTFSDataExchange;
import ca.synx.miway.util.GTFSParser;
import ca.synx.miway.util.StorageHandler;

public class RoutesTask extends AsyncTask<String, Void, List<Route>> {

    private IRoutesTask mRouteTaskListener;
    private StorageHandler mStorageHandler;

    public RoutesTask(IRoutesTask listener, StorageHandler storageHandler) {
        this.mRouteTaskListener = listener;
        this.mStorageHandler = storageHandler;
    }

    @Override
    protected List<Route> doInBackground(String... params) {
        List<Route> routes = mStorageHandler.getRoutes();

        // Check if items were found in cache.
        if (routes.size() > 0)
            return routes;

        // Fetch data from web service.
        String data = (new GTFSDataExchange("miway").getRouteData());

        if (data == null)
            return null;

        try {
            routes = GTFSParser.getRoutes(data);

        } catch (JSONException e) {
            Log.e("RoutesTask:doInBackground", e.getMessage());
            e.printStackTrace();
        }

        // Store items in cache.
        mStorageHandler.saveRoutes(routes);

        return routes;
    }

    @Override
    protected void onPostExecute(List<Route> routes) {
        super.onPostExecute(routes);

        mRouteTaskListener.onRoutesTaskComplete(routes);
    }
}