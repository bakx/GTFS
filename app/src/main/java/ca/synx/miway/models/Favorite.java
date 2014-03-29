/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;
import java.util.List;

import ca.synx.miway.Tasks.StopTimesTask;
import ca.synx.miway.interfaces.IDBItem;
import ca.synx.miway.interfaces.IDataUpdate;
import ca.synx.miway.interfaces.IFavorite;
import ca.synx.miway.interfaces.IStopTimesTask;
import ca.synx.miway.tables.FavoriteTable;

public class Favorite implements IDBItem, Serializable, IFavorite, IStopTimesTask {
    private int mId;
    private Stop mStop;
    private List<StopTime> mNearestStopTimes;
    private List<StopTime> mStopTimes;
    private IDataUpdate mDataUpdateListener;

    public Favorite() {
    }

    public Favorite(Stop stop) {
        this.mStop = stop;
    }

    public Favorite(int id, Stop stop) {
        this.mId = id;
        this.mStop = stop;
    }

    public Stop getStop() {
        return this.mStop;
    }

    public void setStopTimes(List<StopTime> nearestStopTimes, List<StopTime> stopTimes) {
        this.mNearestStopTimes = nearestStopTimes;
        this.mStopTimes = stopTimes;
    }

    /* <Implementation of interface IDBItem> */
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
    /* </Implementation of interface IDBItem> */

    /* <Implementation of interface IFavorite> */
    public String getTitle() {
        return this.mStop.getRoute().getFull();
    }

    public String getSubtitle() {
        return this.mStop.getStopName();
    }

    public List<StopTime> getStopTimes() {
        return mStopTimes;
    }

    public List<StopTime> getNearestStopTimes() {
        return mNearestStopTimes;
    }

    public void loadStopTimes(IDataUpdate dataUpdate) {
        this.mDataUpdateListener = dataUpdate;
        new StopTimesTask(3, this).execute(mStop);
    }
    /* </Implementation of interface IFavorite> */

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
    public void onStopTimesTaskComplete(List<StopTime> nearestStopTimes, List<StopTime> stopTimes) {
        setStopTimes(nearestStopTimes, stopTimes);
        mDataUpdateListener.onDataUpdate();
    }
}

