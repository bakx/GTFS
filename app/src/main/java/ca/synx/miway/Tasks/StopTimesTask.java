/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.synx.miway.Util.GTFSDataExchange;
import ca.synx.miway.Util.GTFSParser;
import ca.synx.miway.interfaces.ITask;
import ca.synx.miway.models.Stop;
import ca.synx.miway.models.StopTime;

public class StopTimesTask extends AsyncTask<Stop, Void, List<StopTime>> {

    private int mNextStopTimesCount;
    private Context mContext;
    private ITask mListener;

    public StopTimesTask(Context context, int nextStopTimesCount, ITask listener) {
        this.mContext = context;
        this.mNextStopTimesCount = nextStopTimesCount;
        this.mListener = listener;
    }

    @Override
    protected List<StopTime> doInBackground(Stop... params) {

        Stop stop = params[0];

        List<StopTime> stopTimes = new ArrayList<StopTime>();

        String data = (new GTFSDataExchange("miway").getStopTimesData(stop));

        try {
            stopTimes = GTFSParser.getStopTimes(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (StopTime stopTime : stopTimes)
            stopTime.setStop(stop);

        return stopTimes;
    }

    @Override
    protected void onPostExecute(List<StopTime> stopTimes) {
        super.onPostExecute(stopTimes);

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("hh:mm aa");

        for (StopTime stopTime : stopTimes) {
            try {
                stopTime.departureTime = newDateFormat.format(
                        currentDateFormat.parse(stopTime.departureTime)
                );
            } catch (Exception e) {
                Log.v("StopTime Parse error", e.getMessage());
            }
        }

        this.mListener.onTaskComplete(
                new Object[]{
                        getNearestStopTimes(stopTimes, mNextStopTimesCount),
                        stopTimes
                }
        );
    }

    protected List<StopTime> getNearestStopTimes(List<StopTime> source, int targetCount) {
        List<StopTime> nearestStopTimes = new ArrayList<StopTime>();

        Date currentDate = null;

        //
        // Get current time stamp as date
        //

        try {
            currentDate = new SimpleDateFormat("hh:mm aa").parse(
                    new SimpleDateFormat("hh:mm aa").format(
                            Calendar.getInstance().getTime()
                    )
            );
        } catch (Exception e) {
            Log.v("getNearestStopTimes currentDate error", e.getMessage());
            return nearestStopTimes;
        }

        //
        // Loop through all departure times to find best match
        //

        for (StopTime stopTime : source) {

            try {

                Date stopDate = new SimpleDateFormat("hh:mm aa").parse(
                        stopTime.departureTime
                );

                long timeDifference = (stopDate.getTime() - currentDate.getTime()) / (60 * 1000);

                // If vehicle is already gone, continue.
                if (timeDifference < 0)
                    continue;

                stopTime.departureTime = stopTime.departureTime + " (" + String.valueOf(timeDifference) + " min)";

                // Since time is already sorted on server, all objects that come after the
                // time difference is 0, are valid. Keep adding them until we reach 'targetCount'
                nearestStopTimes.add(stopTime);
            } catch (Exception e) {
                Log.v("getNearestStopTimes departureTime parsing error", e.getMessage());
            }

            if (nearestStopTimes.size() >= targetCount)
                break;
        }

        return nearestStopTimes;
    }
}