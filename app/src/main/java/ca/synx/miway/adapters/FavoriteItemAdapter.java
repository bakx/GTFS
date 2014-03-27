/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.synx.miway.interfaces.IListItem;

public class FavoriteItemAdapter<T extends IListItem> extends BaseAdapter<T> {

    public FavoriteItemAdapter(List<T> list, int listViewResourceID, boolean displayNextItemIcon, Context ctx) {
        super(list, listViewResourceID, displayNextItemIcon, ctx);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        return super.getView(position, view, parent);

    }
}

