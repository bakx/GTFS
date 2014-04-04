/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.tables;

public final class CacheStopsTable {
    public static final String TABLE_NAME = "cache_stops";
    public static final String COLUMN_SERVICE_DATE = "service_date";
    public static final String COLUMN_STOP_ID = "stop_id";
    public static final String COLUMN_STOP_NAME = "stop_name";
    public static final String COLUMN_STOP_LAT = "stop_lat";
    public static final String COLUMN_STOP_LON = "stop_lon";
    public static final String COLUMN_STOP_SEQUENCE = "stop_sequence";

    public static String CREATE_TABLE() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_SERVICE_DATE + " TEXT," +
                COLUMN_STOP_ID + " TEXT," +
                COLUMN_STOP_NAME + " TEXT," +
                COLUMN_STOP_LAT + " TEXT," +
                COLUMN_STOP_LON + " TEXT," +
                COLUMN_STOP_SEQUENCE + " INT" +
                ")";
    }

    public static String DROP_TABLE() {
        return "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    }
}