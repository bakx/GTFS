/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;
import ca.synx.miway.tables.CacheRoutesTable;
import ca.synx.miway.tables.CacheStopTimesTable;

public final class CacheHandler {

    private static DatabaseHandler mDatabaseHandler;

    public CacheHandler(DatabaseHandler databaseHandler) {
        this.mDatabaseHandler = databaseHandler;
    }

    public List<Route> getRoutes() {

        List<Route> list = new ArrayList<Route>();

        SQLiteDatabase db = mDatabaseHandler.getReadableDatabase();

        try {
            Cursor cursor = db.query(CacheRoutesTable.TABLE_NAME,
                    new String[]{
                            CacheRoutesTable.COLUMN_ROUTE_NUMBER,
                            CacheRoutesTable.COLUMN_ROUTE_NAME,
                            CacheRoutesTable.COLUMN_ROUTE_HEADING
                    },
                    CacheRoutesTable.COLUMN_SERVICE_DATE + " = ? ",
                    new String[]{
                            GTFS.getServiceTimeStamp()
                    }, null, null, null
            );

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    Route route = new Route(
                            cursor.getString(cursor.getColumnIndex(CacheRoutesTable.COLUMN_ROUTE_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(CacheRoutesTable.COLUMN_ROUTE_NAME)),
                            cursor.getString(cursor.getColumnIndex(CacheRoutesTable.COLUMN_ROUTE_HEADING))
                    );

                    list.add(route);
                    cursor.moveToNext();
                }
            }

            return list;
        } catch (Exception e) {
            Log.e("getRoutes", e.getMessage());
        } finally {
            db.close();
        }

        return list;
    }

    public void saveRoutes(List<Route> routes) {

        SQLiteDatabase db = this.mDatabaseHandler.getWritableDatabase();

        try {
            for (Route route : routes) {
                ContentValues values = new ContentValues();
                values.put(CacheRoutesTable.COLUMN_ROUTE_NUMBER, route.getRouteNumber());
                values.put(CacheRoutesTable.COLUMN_ROUTE_NAME, route.getRouteName());
                values.put(CacheRoutesTable.COLUMN_ROUTE_HEADING, route.getRouteHeading());
                values.put(CacheRoutesTable.COLUMN_SERVICE_DATE, GTFS.getServiceTimeStamp());

                db.insert(CacheRoutesTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            Log.e("saveRoutes", e.getMessage());
        } finally {
            db.close();
        }
    }

    public List<StopTime> getStopTime(Stop stop) {

        List<StopTime> list = new ArrayList<StopTime>();

        SQLiteDatabase db = mDatabaseHandler.getReadableDatabase();

        try {
            Cursor cursor = db.query(CacheStopTimesTable.TABLE_NAME,
                    new String[]{
                            CacheStopTimesTable.COLUMN_ARRIVAL_TIME,
                            CacheStopTimesTable.COLUMN_DEPARTURE_TIME
                    },
                    CacheStopTimesTable.COLUMN_ROUTE_NUMBER + " = ? " +
                            "AND " + CacheStopTimesTable.COLUMN_ROUTE_HEADING + " = ? " +
                            "AND " + CacheStopTimesTable.COLUMN_STOP_ID + " = ? " +
                            "AND " + CacheStopTimesTable.COLUMN_SERVICE_DATE + " = ? ",
                    new String[]{
                            stop.getRoute().getRouteNumber(),
                            stop.getRoute().getRouteHeading(),
                            stop.getStopId(),
                            GTFS.getServiceTimeStamp()
                    }, null, null, null
            );

            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    StopTime stopTime = new StopTime(
                            cursor.getString(cursor.getColumnIndex(CacheStopTimesTable.COLUMN_ARRIVAL_TIME)),
                            cursor.getString(cursor.getColumnIndex(CacheStopTimesTable.COLUMN_DEPARTURE_TIME))
                    );

                    stopTime.setStop(stop);

                    list.add(stopTime);
                    cursor.moveToNext();
                }
            }

            return list;
        } catch (Exception e) {
            Log.e("getStopTime", e.getMessage());
        } finally {
            db.close();
        }

        return list;
    }
}