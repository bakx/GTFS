/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;
import java.util.ArrayList;

import ca.synx.miway.interfaces.IDBItem;
import ca.synx.miway.interfaces.IFavorite;
import ca.synx.miway.interfaces.IListItem;
import ca.synx.miway.interfaces.ITask;
import ca.synx.miway.tables.FavoriteTable;

public class Favorite implements IDBItem, IListItem, Serializable, IFavorite, ITask {

    private int mId;
    private Stop mStop;
    private ArrayList<StopTime> mStopTimes;

    public Favorite() {
    }

    public Favorite(Stop stop) {
        this.mStop = stop;
    }

    public Favorite(int id, Stop stop) {
        this.mId = id;
        this.mStop = stop;

        // THIS CODE SHOULD NOT BE HERE!  //HACK //TODO
        //try {
        //    new StopTimesTask(null, 3, this).execute(mStop).get(2, TimeUnit.SECONDS);
        //}
        //catch (Exception e) {
        //    e.printStackTrace();
        //}
    }

    public Stop getStop() {
        return this.mStop;
    }

    public ArrayList<StopTime> getStopTimes() {
        return mStopTimes;
    }

    public String getTitle() {
        return this.mStop.getRoute().getFull();
    }

    public String getSubtitle() {
        return this.mStop.getStopName();
    }

    public String getFull() {
        return this.mStop.getRoute().getRouteNumber() + this.mStop.getRoute().getRouteHeading().substring(0, 1) + " - " + this.mStop.getRoute().getRouteName();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String CREATE_SQL_ENTRIES() {
        return "CREATE TABLE " + FavoriteTable.TABLE_NAME + " (" +
                FavoriteTable.COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteTable.COLUMN_STOP_ID + " TEXT," +
                FavoriteTable.COLUMN_STOP_NAME + " TEXT," +
                FavoriteTable.COLUMN_STOP_SEQUENCE + " INT," +
                FavoriteTable.COLUMN_ROUTE_NUMBER + " TEXT," +
                FavoriteTable.COLUMN_ROUTE_NAME + " TEXT," +
                FavoriteTable.COLUMN_ROUTE_HEADING + " TEXT" +
                ")";
    }

    public String DELETE_SQL_ENTRIES() {
        return "DROP TABLE IF EXIST " + FavoriteTable.TABLE_NAME + ";";
    }

    @Override
    public void onTaskComplete(Object[] objects) {
        mStopTimes = (ArrayList<StopTime>) objects[0];
    }
}

