/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.helpers;

import java.util.List;

import ca.synx.miway.interfaces.IDataUpdate;
import ca.synx.miway.interfaces.IStopTimesTask;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.StopTime;
import ca.synx.miway.tasks.StopTimesTask;
import ca.synx.miway.util.StorageHandler;

public class FavoriteHelper implements IStopTimesTask {

    private Favorite mFavorite;
    private IDataUpdate mDataUpdateListener;
    private StorageHandler mStorageHandler;

    public FavoriteHelper(IDataUpdate dataUpdateListener, Favorite favorite, StorageHandler storageHandler) {
        this.mDataUpdateListener = dataUpdateListener;
        this.mFavorite = favorite;
        this.mStorageHandler = storageHandler;
    }

    public void loadStopTimes() {
        new StopTimesTask(3, this, mStorageHandler).execute(mFavorite.getStop());
    }

    @Override
    public void onStopTimesTaskComplete(List<StopTime> nearestStopTimes, List<StopTime> stopTimes) {
        mFavorite.setStopTimes(nearestStopTimes, stopTimes);
        mDataUpdateListener.onDataUpdate();
    }
}
