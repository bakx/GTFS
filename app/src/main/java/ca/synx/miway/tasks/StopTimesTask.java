/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.interfaces.IStopTimesTask;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;
import ca.synx.miway.util.GTFSDataExchange;
import ca.synx.miway.util.GTFSParser;
import ca.synx.miway.util.StorageHandler;

public class StopTimesTask extends AsyncTask<Stop, Void, List<StopTime>> {

    private Context mContext;
    private IStopTimesTask mListener;
    private StorageHandler mStorageHandler;

    public StopTimesTask(Context context, IStopTimesTask listener, StorageHandler storageHandler) {
        this.mContext = context;
        this.mListener = listener;
        this.mStorageHandler = storageHandler;
    }

    @Override
    protected List<StopTime> doInBackground(Stop... params) {

        Stop stop = params[0];

        List<StopTime> stopTimes = mStorageHandler.getStopTimes(stop);

        // Check if items were found in cache.
        if (stopTimes.size() > 0)
            return stopTimes;

        stopTimes = new ArrayList<StopTime>();

        String data = (new GTFSDataExchange().getStopTimesData(stop));

        if (data == null)
            return null;

        try {
            stopTimes = GTFSParser.getStopTimes(data);

        } catch (JSONException e) {
            Log.e("StopTimesTask:doInBackground", "" + e.getMessage());
            e.printStackTrace();
        }

        for (StopTime stopTime : stopTimes)
            stopTime.setStop(stop);

        // Store items in cache.
        mStorageHandler.saveStopTimes(stopTimes);

        return stopTimes;
    }

    @Override
    protected void onPostExecute(List<StopTime> stopTimes) {
        super.onPostExecute(stopTimes);

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("hh:mm aa");

        for (StopTime stopTime : stopTimes) {
            try {
                stopTime.setDepartureTime(
                        newDateFormat.format(
                                currentDateFormat.parse(stopTime.getDepartureTime()
                                )
                        )
                );
            } catch (Exception e) {
                Log.e("StopTimesTask:onPostExecute", "" + e.getMessage());
                e.printStackTrace();
            }
        }

        mListener.onStopTimesTaskComplete(stopTimes);
    }
}