/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.synx.miway.tables.CacheRouteStopsTable;
import ca.synx.miway.tables.CacheRoutesTable;
import ca.synx.miway.tables.CacheStopTimesTable;
import ca.synx.miway.tables.CacheStopsTable;
import ca.synx.miway.tables.FavoriteTable;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "GTFS.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CacheRoutesTable.CREATE_TABLE());
        db.execSQL(CacheRouteStopsTable.CREATE_TABLE());
        db.execSQL(CacheStopsTable.CREATE_TABLE());
        db.execSQL(CacheStopTimesTable.CREATE_TABLE());
        db.execSQL(FavoriteTable.CREATE_TABLE());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(CacheRoutesTable.DROP_TABLE());
        db.execSQL(CacheRouteStopsTable.DROP_TABLE());
        db.execSQL(CacheStopsTable.DROP_TABLE());
        db.execSQL(CacheStopTimesTable.DROP_TABLE());
        db.execSQL(FavoriteTable.DROP_TABLE());

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
