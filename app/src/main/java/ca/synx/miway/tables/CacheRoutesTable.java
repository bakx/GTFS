/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.tables;

public final class CacheRoutesTable {
    public static final String TABLE_NAME = "cache_routes";
    public static final String COLUMN_SERVICE_DATE = "service_date";
    public static final String COLUMN_ROUTE_NUMBER = "route_number";
    public static final String COLUMN_ROUTE_NAME = "route_name";
    public static final String COLUMN_ROUTE_HEADING = "route_heading";

    public static String CREATE_SQL_ENTRIES() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_SERVICE_DATE + " TEXT," +
                COLUMN_ROUTE_NUMBER + " TEXT," +
                COLUMN_ROUTE_NAME + " TEXT," +
                COLUMN_ROUTE_HEADING + " TEXT" +
                ")";
    }

    public static String DELETE_SQL_ENTRIES() {
        return "DROP TABLE IF EXISTS '" + TABLE_NAME + "';";
    }
}