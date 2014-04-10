/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;
import java.util.List;

import ca.synx.miway.util.StorageHandler;

public class Favorite implements Serializable {

    private int mId;
    private Stop mStop;
    private List<StopTime> mNextStopTimes;
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
        return (null == mStop) ? new Stop() : this.mStop;
    }

    public void setNextStopTimes(List<StopTime> nextStopTimes) {
        this.mNextStopTimes = nextStopTimes;
    }

    /* <Implementation of interface IDBItem> */
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public List<StopTime> getStopTimes() {
        return mStopTimes;
    }

    public void setStopTimes(List<StopTime> stopTimes) {
        this.mStopTimes = stopTimes;
    }

    public List<StopTime> getNearestStopTimes() {
        return mNextStopTimes;
    }

    /* </Implementation of interface IFavorite> */
}

