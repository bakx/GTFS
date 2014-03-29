/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.Util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;

public class GTFSDataExchange {

    private static String GTFS_BASE_URL = "http://miway.dataservices.synx.ca";
    private static String GET_ROUTES_URL = "/api/GTFS/GetRoutes/%s";
    private static String GET_STOPS_URL = "/api/GTFS/GetStops/%s/%s/%s";
    private static String GET_STOP_TIMES_URL = "/api/GTFS/GetStops/%s/%s/%s/%s";
    private String mTransitCompany;

    public GTFSDataExchange(String transitCompany) {
        this.mTransitCompany = transitCompany;
    }

    private String getData(String dataURL) {

        HttpClient client;
        String result;

        try {
            client = new DefaultHttpClient();
            HttpResponse response = client.execute(new HttpGet(dataURL));
            InputStream is = response.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.v("GTFSDataExchange.getData threw error", e.getMessage());
            return "";
        }

        return result;
    }

    public String getRouteData() {
        return getData(
                String.format(GTFS_BASE_URL + GET_ROUTES_URL, getServiceTimeStamp())
        );
    }

    public String getStopsData(Route route) {
        return getData(
                String.format(GTFS_BASE_URL + GET_STOPS_URL, route.getRouteNumber(), route.getRouteHeading(), getServiceTimeStamp())
        );
    }

    public String getStopTimesData(Stop stop) {
        return getData(
                String.format(GTFS_BASE_URL + GET_STOP_TIMES_URL, stop.getRoute().getRouteNumber(), stop.getRoute().getRouteHeading(), stop.getStopId(), getServiceTimeStamp())
        );
    }

    private String getServiceTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
    }
}