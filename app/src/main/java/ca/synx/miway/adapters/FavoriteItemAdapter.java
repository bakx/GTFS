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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.synx.miway.app.R;
import ca.synx.miway.interfaces.IListItem;

public class FavoriteItemAdapter<T extends IListItem> extends BaseAdapter<T> {

    public FavoriteItemAdapter(List<T> list, int listViewResourceID, boolean displayNextItemIcon, Context ctx) {
        super(list, listViewResourceID, displayNextItemIcon, ctx);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        T t = null;
        Holder holder = new Holder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(listViewResourceID, null);

            if (!showNextIcon) {
                ImageView imageView = (ImageView) v.findViewById(R.id.nextitem);
                imageView.setVisibility(View.GONE);
            }

            holder.title = (TextView) v.findViewById(R.id.title);
            holder.subtitle = (TextView) v.findViewById(R.id.subtitle);
            holder.stoptime1 = (TextView) v.findViewById(R.id.stoptime1);
            holder.stoptime2 = (TextView) v.findViewById(R.id.stoptime2);
            holder.stoptime3 = (TextView) v.findViewById(R.id.stoptime3);

            // Cache holder for performance reasons.
            v.setTag(R.id.tag_id_1, holder);
        } else {
            // Retrieve holder from Cache.        }
            holder = (Holder) v.getTag(R.id.tag_id_1);
        }

        // Get position of list item.
        t = list.get(position);

        // Update tag of view with object reference of object T
        v.setTag(R.id.tag_id_2, (T) t);

        // Update title of the view item.
        holder.title.setText(t.getTitle());
        holder.subtitle.setText(t.getSubtitle());

        return v;
    }

    private static class Holder {
        public TextView title;
        public TextView subtitle;
        public TextView stoptime1;
        public TextView stoptime2;
        public TextView stoptime3;
    }
}