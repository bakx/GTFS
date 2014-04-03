/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.helpers;

import android.content.Context;

import java.util.List;

import ca.synx.miway.interfaces.IDataUpdate;
import ca.synx.miway.interfaces.INextStopTimesTask;
import ca.synx.miway.interfaces.IStopTimesTask;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.StopTime;
import ca.synx.miway.tasks.NextStopTimesTask;
import ca.synx.miway.tasks.StopTimesTask;
import ca.synx.miway.util.StorageHandler;

public class FavoriteHelper implements IStopTimesTask, INextStopTimesTask {

    private Context mContext;
    private Favorite mFavorite;
    private IDataUpdate mDataUpdateListener;
    private StorageHandler mStorageHandler;

    public FavoriteHelper(Context context, IDataUpdate dataUpdateListener, StorageHandler storageHandler, Favorite favorite) {
        this.mContext = context;
        this.mDataUpdateListener = dataUpdateListener;
        this.mFavorite = favorite;
        this.mStorageHandler = storageHandler;
    }

    public void loadStopTimes() {
        new StopTimesTask(mContext, this, mStorageHandler).execute(mFavorite.getStop());
    }

    @Override
    public void onStopTimesTaskComplete(List<StopTime> stopTimes) {
        mFavorite.setStopTimes(stopTimes);
        new NextStopTimesTask(mContext, this, 3).execute(stopTimes);
    }

    @Override
    public void onNextStopTimesTaskComplete(List<StopTime> nextStopTimes) {
        mFavorite.setNextStopTimes(nextStopTimes);
        mDataUpdateListener.onDataUpdate();
    }
}
