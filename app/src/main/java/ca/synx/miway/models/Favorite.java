/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;
import java.util.List;

import ca.synx.miway.interfaces.IFavorite;
import ca.synx.miway.util.StorageHandler;

public class Favorite implements Serializable, IFavorite {

    private int mId;
    private Stop mStop;
    private List<StopTime> mNearestStopTimes;
    private List<StopTime> mStopTimes;

    private StorageHandler mStorageHandler;

    public Favorite(Stop stop) {
        this.mStop = stop;
    }

    public Favorite(int id, Stop stop) {
        this.mId = id;
        this.mStop = stop;
    }

    public Stop getStop() {
        return (null == this.mStop) ? new Stop("", "", 0) : this.mStop;
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

    /* </Implementation of interface IFavorite> */
}

