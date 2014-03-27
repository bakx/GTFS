/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.synx.miway.interfaces.IFavorite;

public class Favorites<T extends IFavorite> {
    private Context mContext;

    public Favorites(Context context) {
        this.mContext = context;
    }

    public void saveFavorite(T t) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext.getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(t.getID(), serializeObject(t));
        editor.commit();
    }

    public void removeFavorite(String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext.getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(key);
        editor.commit();
    }

    public List<T> getFavorites() {

        List<T> list = new ArrayList<T>();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext.getApplicationContext());

        Map<String, ?> keys = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {

            T t = deserializeObject((String) entry.getValue());
            if (null != t)
                list.add(t);
        }

        return list;
    }

    private String serializeObject(T t) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(t);
            so.flush();

            return bo.toString();
        } catch (Exception e) {
            Log.e("Error while saving object to preferences", e.getMessage());
        }

        return "";
    }

    private T deserializeObject(String s) {
        try {
            byte b[] = s.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            T t = (T) si.readObject();

            return t;
        } catch (Exception e) {
            Log.e("Error while saving object to preferences", e.getMessage());
        }

        return null;
    }
}