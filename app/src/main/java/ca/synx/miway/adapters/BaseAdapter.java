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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.synx.miway.app.R;
import ca.synx.miway.interfaces.IListItem;

public class BaseAdapter<T extends IListItem> extends ArrayAdapter<T> {

    public Context mContext;
    public int mResourceId;
    public boolean mShowNextIcon = true;
    public List<T> mList;

    public BaseAdapter(Context context, List<T> list, int resourceId, boolean showNextIcon) {
        super(context, resourceId, list);

        this.mContext = context;
        this.mList = list;
        this.mResourceId = resourceId;
        this.mShowNextIcon = showNextIcon;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        Holder holder = new Holder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mResourceId, null);

            if (!mShowNextIcon) {
                ImageView imageView = (ImageView) v.findViewById(R.id.nextitem);
                imageView.setVisibility(View.GONE);
            }

            holder.title = (TextView) v.findViewById(R.id.title);
            holder.subtitle = (TextView) v.findViewById(R.id.subtitle);

            // Cache holder for performance reasons.
            v.setTag(R.id.tag_id_1, holder);
        } else {
            // Retrieve holder from Cache.
            holder = (Holder) v.getTag(R.id.tag_id_1);
        }

        // Get a reference to the object that's placed in the ArrayAdapter<T> at a position specified
        // by Android (ListView control takes care of handling what position should be shown).
        T t = mList.get(position);

        // Update tag of view with object reference of object T
        v.setTag(R.id.tag_id_2, (T) t);

        // Update titles of the view item.
        holder.title.setText(t.getTitle());
        holder.subtitle.setText(t.getSubtitle());

        return v;
    }

    private static class Holder {
        public TextView title;
        public TextView subtitle;
    }
}