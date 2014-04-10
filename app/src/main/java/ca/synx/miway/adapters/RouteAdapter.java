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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.app.R;
import ca.synx.miway.models.Route;

public class RouteAdapter<T extends Route> extends ArrayAdapter<Route> implements Filterable {

    public Context mContext;
    public List<Route> mList;
    public List<Route> mFilteredList;
    public int mResourceId;
    public Filter mFilter;

    public RouteAdapter(Context context, List<Route> list, int resourceId) {
        super(context, resourceId, list);

        this.mContext = context;

        this.mList = list;
        this.mFilteredList = list;
        this.mResourceId = resourceId;
    }

    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new RouteFilter();

        return mFilter;
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @Override
    public Route getItem(int position) {
        return mFilteredList.get(position);
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

            // Cache holder for performance reasons.
            view.setTag(viewHolder);
        } else {
            // Retrieve holder from Cache.
            viewHolder = (ViewHolder) view.getTag();
        }

        // Get object of list item.
        Route route = getItem(position);

        // Attach Route object to View so it can be retrieved from other areas
        view.setTag(R.id.tag_id_2, route);

        // Update titles of the view item.
        viewHolder.numberTextView.setText(route.getRouteNumber());
        viewHolder.nameTextView.setText(route.getRouteName());
        viewHolder.headingTextView.setText(route.getRouteHeading());

        return view;
    }

    private static class ViewHolder {
        public TextView numberTextView;
        public TextView nameTextView;
        public TextView headingTextView;
    }

    private class RouteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults filterResults = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                filterResults.values = mList;
                filterResults.count = mList.size();
            } else {
                List<Route> filteredList = new ArrayList<Route>();

                for (Route route : mList) {
                    if (route.getRouteNumber().toUpperCase()
                            .contains(charSequence.toString().toUpperCase()) ||
                            route.getRouteName().toUpperCase()
                                    .contains(charSequence.toString().toUpperCase()) ||
                            route.getRouteHeading().toUpperCase()
                                    .contains(charSequence.toString().toUpperCase())
                            ) {
                        filteredList.add(route);
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredList = (List<Route>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}