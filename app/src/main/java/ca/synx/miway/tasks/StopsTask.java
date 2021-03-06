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

import ca.synx.miway.interfaces.IStopsTask;
import ca.synx.miway.models.Stop;
import ca.synx.miway.util.GTFSDataExchange;
import ca.synx.miway.util.GTFSParser;
import ca.synx.miway.util.StorageHandler;

public class StopsTask extends AsyncTask<Void, Void, List<Stop>> {

    private IStopsTask mListener;
    private StorageHandler mStorageHandler;

    public StopsTask(IStopsTask stopsTaskListener, StorageHandler storageHandler) {
        this.mListener = stopsTaskListener;
        this.mStorageHandler = storageHandler;
    }

    @Override
    protected List<Stop> doInBackground(Void... params) {

        List<Stop> stops = mStorageHandler.getStops();

        // Check if items were found in cache.
        if (stops.size() > 0)
            return stops;

        String data = (new GTFSDataExchange().getStopsData());

        if (data == null)
            return null;

        try {
            stops = GTFSParser.getStops(data);

        } catch (JSONException e) {
            Log.e("StopsTask:doInBackground", "" + e.getMessage());
            e.printStackTrace();
        }


        // Store items in cache.
        mStorageHandler.saveStops(stops);

        return stops;
    }

    @Override
    protected void onPostExecute(List<Stop> stops) {
        super.onPostExecute(stops);

        mListener.onStopsTaskComplete(
                stops
        );
    }
}