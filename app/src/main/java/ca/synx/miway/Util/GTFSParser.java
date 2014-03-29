/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;

public class GTFSParser {

    public static List<Route> getRoutes(String data) throws JSONException {
        List<Route> routes = new ArrayList<Route>();

        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                routes.add(
                        new Route(
                                jsonObject.getString("RouteNumber"),
                                jsonObject.getString("RouteName"),
                                jsonObject.getString("RouteHeading")
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return routes;
    }

    public static List<Stop> getStops(String data) throws JSONException {
        List<Stop> stops = new ArrayList<Stop>();

        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                stops.add(
                        new Stop(
                                jsonObject.getString("StopId"),
                                jsonObject.getString("StopName"),
                                jsonObject.getInt("StopSequence")
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stops;
    }

    public static List<StopTime> getStopTimes(String data) throws JSONException {
        List<StopTime> stopTimes = new ArrayList<StopTime>();

        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                stopTimes.add(
                        new StopTime(
                                jsonObject.getString("ArrivalTime"),
                                jsonObject.getString("DepartureTime")
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stopTimes;
    }
}