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
import ca.synx.miway.interfaces.IListItem;

public class ListItemAdapter<T extends IListItem> extends ArrayAdapter<T> {

    private List<T> list;
    private Context context;

    public ListItemAdapter(List<T> list, Context ctx) {
        super(ctx, R.layout.listview_item_basic, list);
        this.list = list;
        this.context = ctx;
    }

    public int getCount() {
        return list.size();
    }

    public T getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Holder holder = new Holder();

        // First let's verify the convertView is not null
        if (convertView == null) {

            // Inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_item_basic, null);

            // Now we can fill the layout with the right values
            TextView titleView = (TextView) v.findViewById(R.id.title);
            TextView subtitleView = (TextView) v.findViewById(R.id.subtitle);

            holder.title = titleView;
            holder.subtitle = subtitleView;

            v.setTag(holder);
        } else
            holder = (Holder) v.getTag();

        T t = list.get(position);
        holder.title.setText(t.getTitle());
        holder.subtitle.setText(t.getSubtitle());

        return v;
    }

    private static class Holder {
        public TextView title;
        public TextView subtitle;
    }
}