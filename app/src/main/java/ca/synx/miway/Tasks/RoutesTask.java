/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.Util.GTFSDataExchange;
import ca.synx.miway.Util.GTFSParser;
import ca.synx.miway.interfaces.IRouteTask;
import ca.synx.miway.models.Route;

public class RoutesTask extends AsyncTask<String, Void, List<Route>> {

    private IRouteTask mListener;

    public RoutesTask(IRouteTask listener) {
        this.mListener = listener;
    }

    @Override
    protected List<Route> doInBackground(String... params) {
        List<Route> routes = new ArrayList<Route>();
        String data = (new GTFSDataExchange("miway").getRouteData());

        try {
            routes = GTFSParser.getRoutes(data);

        } catch (JSONException e) {
            Log.v("GTFSRouteTask->doInBackground", e.getMessage());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<Route> routes) {
        super.onPostExecute(routes);

        mListener.onRouteTaskComplete(routes);
    }
}