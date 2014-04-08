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
import ca.synx.miway.interfaces.ISpinnerItem;

public class SpinnerItemAdapter<T extends ISpinnerItem> extends ArrayAdapter<T> {

    private Context mContext;
    private List<T> mList;
    private int mResourceId;

    public SpinnerItemAdapter(Context context, List<T> list, int resourceId) {
        super(context, resourceId, list);

        this.mContext = context;
        this.mList = list;
        this.mResourceId = resourceId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        // Retrieve the viewHolder from the view object. If the view is null, the object gets created.
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag(R.id.tag_id_1);
        } else {
            // Inflate the new layout.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResourceId, null);

            // Create references to layout items used in adapter.
            TextView titleView = (TextView) view.findViewById(R.id.title);

            // Store references to layout items in view viewHolder.
            viewHolder.title = titleView;

            // Store references of layout items in view.
            view.setTag(R.id.tag_id_1, viewHolder);
        }

        //
        // Set up the view.
        //

        // Get T object from list.
        T t = mList.get(position);

        // Update title TextView of the view.
        viewHolder.title.setText(t.getSpinnerTitle());

        // Update tag of view with object reference of object T
        view.setTag(R.id.tag_id_2, (T) t);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        public TextView title;
    }
}