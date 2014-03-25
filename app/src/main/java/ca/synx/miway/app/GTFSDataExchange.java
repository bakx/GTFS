/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GTFSDataExchange {

    private String transitCompany;
    private static String GTFS_BASE_URL = "http://miway.dataservices.synx.ca";
    private static String GET_ROUTES_URL = "/api/MiWay/GetRoutes";
    private static String GET_STOPS_URL = "/api/MiWay/GetStops/%s/%s";
    private static String GET_STOP_TIMES_URL = "/api/MiWay/GetStops/{0}/{1}/{2}";

    public GTFSDataExchange(String transitCompany) {
        this.transitCompany = transitCompany;
    }

    private String getData(String dataURL) {

        HttpClient client;
        String result = "";

        try {
            client = new DefaultHttpClient();
            HttpResponse response = client.execute(new HttpGet(dataURL));
            InputStream is = response.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            return null;
        }

        return result;
    }

    public String getRouteData() {
        return getData(GTFS_BASE_URL + GET_ROUTES_URL);
    }

    public String getStopsData(String routeNumber, String routeHeading) {
        return getData(
                String.format(GTFS_BASE_URL + GET_STOPS_URL, routeNumber, routeHeading)
        );
    }
}