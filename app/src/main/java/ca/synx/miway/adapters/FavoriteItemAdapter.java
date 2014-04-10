/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.synx.miway.app.R;
import ca.synx.miway.models.Favorite;
import ca.synx.miway.models.StopTime;

public class FavoriteItemAdapter<T extends Favorite> extends ArrayAdapter<Favorite> {

    public int mResourceId;
    private Context mContext;


    public FavoriteItemAdapter(Context context, List<Favorite> list, int resourceId) {
        super(context, resourceId, list);

        this.mContext = context;
        this.mResourceId = resourceId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResourceId, null);

            viewHolder.numberTextView = (TextView) view.findViewById(R.id.numberTextView);
            viewHolder.nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            viewHolder.headingTextView = (TextView) view.findViewById(R.id.headingTextView);
            viewHolder.stopTime1 = (TextView) view.findViewById(R.id.stoptime1);
            viewHolder.stopTime2 = (TextView) view.findViewById(R.id.stoptime2);
            viewHolder.stopTime3 = (TextView) view.findViewById(R.id.stoptime3);

            // Cache holder for performance reasons.
            view.setTag(viewHolder);
        } else {
            // Retrieve holder from Cache.
            viewHolder = (ViewHolder) view.getTag();
        }

        // Get object of list item.
        Favorite favorite = getItem(position);

        // Attach Route object to View so it can be retrieved from other areas
        view.setTag(R.id.tag_id_2, favorite);

        if (favorite.getNearestStopTimes() != null)
            displayNearestStopTimes(viewHolder, favorite.getNearestStopTimes());

        // Update titles of the view item.
        viewHolder.numberTextView.setText(favorite.getStop().getRoute().getRouteNumber());
        viewHolder.nameTextView.setText(favorite.getStop().getRoute().getRouteName());
        viewHolder.headingTextView.setText(favorite.getStop().getRoute().getRouteHeading());

        return view;
    }

    private void displayNearestStopTimes(ViewHolder viewHolder, List<StopTime> nearestStopTimes) {

        if (nearestStopTimes.size() == 0) {
            viewHolder.stopTime1.setText(R.string.no_stop_times_scheduled);
            viewHolder.stopTime2.setText("");
            viewHolder.stopTime3.setText("");
        } else if (nearestStopTimes.size() == 1) {
            viewHolder.stopTime1.setText(nearestStopTimes.get(0).getTitle());
            viewHolder.stopTime2.setText("");
            viewHolder.stopTime3.setText("");
        } else if (nearestStopTimes.size() == 2) {
            viewHolder.stopTime1.setText(nearestStopTimes.get(0).getTitle());
            viewHolder.stopTime2.setText("");
            viewHolder.stopTime3.setText(nearestStopTimes.get(1).getTitle());
        } else {
            viewHolder.stopTime1.setText(nearestStopTimes.get(0).getTitle());
            viewHolder.stopTime2.setText(nearestStopTimes.get(1).getTitle());
            viewHolder.stopTime3.setText(nearestStopTimes.get(2).getTitle());
        }
    }

    private static class ViewHolder {
        public TextView numberTextView;
        public TextView nameTextView;
        public TextView headingTextView;
        public TextView stopTime1;
        public TextView stopTime2;
        public TextView stopTime3;
    }
}