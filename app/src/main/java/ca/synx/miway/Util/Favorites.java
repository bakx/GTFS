/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.tables.FavoriteTable;

public final class Favorites {

    private DatabaseHandler mDatabaseHandler;
    private Context mContext;

    public Favorites(Context context) {
        this.mDatabaseHandler = new DatabaseHandler(context);
        this.mContext = context;
    }

    public void saveFavorite(Favorite favorite) {

        SQLiteDatabase db = this.mDatabaseHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteTable.COLUMN_FAVORITE_ID, UUID.randomUUID().toString());
        values.put(FavoriteTable.COLUMN_STOP_ID, favorite.getStop().getStopId());
        values.put(FavoriteTable.COLUMN_STOP_NAME, favorite.getStop().getStopName());
        values.put(FavoriteTable.COLUMN_STOP_SEQUENCE, favorite.getStop().getStopSequence());
        values.put(FavoriteTable.COLUMN_ROUTE_NUMBER, favorite.getStop().getRoute().getRouteNumber());
        values.put(FavoriteTable.COLUMN_ROUTE_NAME, favorite.getStop().getRoute().getRouteName());
        values.put(FavoriteTable.COLUMN_ROUTE_HEADING, favorite.getStop().getRoute().getRouteHeading());
        values.put(FavoriteTable.COLUMN_STOP_ID, favorite.getStop().getStopId());

        db.insert(FavoriteTable.TABLE_NAME, "", values);
    }

    public void removeFavorite(Favorite favorite) {

        SQLiteDatabase db = this.mDatabaseHandler.getWritableDatabase();

        db.delete(FavoriteTable.TABLE_NAME, FavoriteTable.COLUMN_FAVORITE_ID + " = ? ", new String[]{favorite.getId()});
    }

    public List<Favorite> getFavorites() {

        List<Favorite> list = new ArrayList<Favorite>();

        SQLiteDatabase db = this.mDatabaseHandler.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + FavoriteTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {

                Route route = new Route(
                        cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ROUTE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ROUTE_NAME)),
                        cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ROUTE_HEADING))
                );

                Stop stop = new Stop(
                        cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_STOP_ID)),
                        cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_STOP_NAME)),
                        cursor.getInt(cursor.getColumnIndex(FavoriteTable.COLUMN_STOP_SEQUENCE))
                );

                stop.setRoute(route);

                Favorite favorite = new Favorite(
                        cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_FAVORITE_ID)),
                        stop
                );


                list.add(favorite);
                cursor.moveToNext();
            }
        }


        return list;
    }
}