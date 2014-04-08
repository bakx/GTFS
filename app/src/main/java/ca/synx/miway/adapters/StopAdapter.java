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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.app.R;
import ca.synx.miway.models.Stop;

public class StopAdapter<T extends Stop> extends ArrayAdapter<Stop> implements Filterable {

    public Context mContext;
    public List<Stop> mList;
    public List<Stop> mFilteredList;
    public int mResourceId;
    public boolean mShowNextIcon = true;
    public Filter mFilter;

    public StopAdapter(Context context, List<Stop> list, int resourceId, boolean showNextIcon) {
        super(context, resourceId, list);

        this.mContext = context;

        this.mList = list;
        this.mFilteredList = list;
        this.mResourceId = resourceId;
        this.mShowNextIcon = showNextIcon;
    }

    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new StopFilter();

        return mFilter;
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @Override
    public Stop getItem(int position) {
        return mFilteredList.get(position);
    }

    public View getView(int position, View view, ViewGroup parent) {

        Holder holder = new Holder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResourceId, null);

            if (!mShowNextIcon) {
                ImageView imageView = (ImageView) view.findViewById(R.id.nextitem);
                imageView.setVisibility(View.GONE);
            }

            holder.title = (TextView) view.findViewById(R.id.title);
            holder.subtitle = (TextView) view.findViewById(R.id.subtitle);

            // Cache holder for performance reasons.
            view.setTag(R.id.tag_id_1, holder);
        } else {
            // Retrieve holder from Cache.
            holder = (Holder) view.getTag(R.id.tag_id_1);
        }

        // Get a reference to the object that's placed in the ArrayAdapter<T> at a position specified
        // by Android (ListView control takes care of handling what position should be shown).

        Stop stop = getItem(position);

        // Update tag of view with object reference of object T
        view.setTag(R.id.tag_id_2, stop);

        // Update titles of the view item.
        holder.title.setText(stop.getTitle());
        holder.subtitle.setText(stop.getSubtitle());

        return view;
    }

    private static class Holder {
        public TextView title;
        public TextView subtitle;
    }

    private class StopFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults filterResults = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                filterResults.values = mList;
                filterResults.count = mList.size();
            } else {
                List<Stop> filteredList = new ArrayList<Stop>();

                for (Stop stop : mList) {
                    if (stop.getStopId().toUpperCase()
                            .contains(charSequence.toString().toUpperCase()) ||
                            stop.getStopName().toUpperCase()
                                    .contains(charSequence.toString().toUpperCase())
                            ) {
                        filteredList.add(stop);
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredList = (List<Stop>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}