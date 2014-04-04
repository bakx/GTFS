/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;

public class GTFSDataExchange {

    private static String GTFS_BASE_URL = "http://miway.dataservices.synx.ca";
    private static String GET_ROUTES_URL = "/api/GTFS/GetRoutes/%s";
    private static String GET_STOPS_URL = "/api/GTFS/GetStops";
    private static String GET_STOPS_ROUTE_URL = "/api/GTFS/GetStops/%s/%s/%s";
    private static String GET_STOP_TIMES_URL = "/api/GTFS/GetStopTimes/%s/%s/%s/%s";

    public GTFSDataExchange() {
    }

    private String getData(String dataURL) {

        HttpClient client;

        try {
            client = new DefaultHttpClient();
            HttpResponse response = client.execute(new HttpGet(dataURL));
            InputStream is = response.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + '\n');
            }
            is.close();

            return sb.toString();
        } catch (Exception e) {
            Log.e("GTFSDataExchange:getData", "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String getRouteData() {
        return getData(
                String.format(GTFS_BASE_URL + GET_ROUTES_URL, GTFS.getServiceTimeStamp())
        );
    }

    public String getStopsData() {
        return getData(
                String.format(GTFS_BASE_URL + GET_STOPS_URL)
        );
    }

    public String getStopsData(Route route) {
        return getData(
                String.format(GTFS_BASE_URL + GET_STOPS_ROUTE_URL, route.getRouteNumber(), route.getRouteHeading(), GTFS.getServiceTimeStamp())
        );
    }

    public String getStopTimesData(Stop stop) {
        return getData(
                String.format(GTFS_BASE_URL + GET_STOP_TIMES_URL, stop.getRoute().getRouteNumber(), stop.getRoute().getRouteHeading(), stop.getStopId(), GTFS.getServiceTimeStamp())
        );
    }
}