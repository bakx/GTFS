package ca.synx.miway.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.synx.miway.app.R;
import ca.synx.miway.models.Route;

public class RouteAdapter extends ArrayAdapter<Route> {

    private List<Route> routesList;
    private Context context;

    public RouteAdapter(List<Route> routesList, Context ctx) {
        super(ctx, R.layout.listview_item_basic, routesList);
        this.routesList = routesList;
        this.context = ctx;
    }

    public int getCount() {
        return routesList.size();
    }

    public Route getItem(int position) {
        return routesList.get(position);
    }

    public long getItemId(int position) {
        return routesList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        RouteHolder holder = new RouteHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {

            // Inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_item_basic, null);

            // Now we can fill the layout with the right values
            TextView titleView = (TextView) v.findViewById(R.id.title);
            TextView subtitleView = (TextView) v.findViewById(R.id.subtitle);

            holder.routeName = titleView;
            holder.routeHeading = subtitleView;

            v.setTag(holder);
        } else
            holder = (RouteHolder) v.getTag();

        Route p = routesList.get(position);
        holder.routeName.setText(p.routeName);
        holder.routeHeading.setText(p.routeHeading);

        return v;
    }

    private static class RouteHolder {
        public TextView routeName;
        public TextView routeHeading;
    }
}