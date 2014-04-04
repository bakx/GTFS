/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;

import ca.synx.miway.interfaces.IListItem;

public class Stop implements IListItem, Serializable {
    private String mStopId;
    private String mStopName;
    private double mStopLat;
    private double mStopLon;
    private int mStopSequence;
    private Route mRoute;

    public Stop() {
    }

    public Stop(String stopId, String stopName, String stopLat, String stopLon, int stopSequence) {
        this.mStopId = stopId;
        this.mStopName = stopName;
        this.mStopLat = Double.valueOf(stopLat);
        this.mStopLon = Double.valueOf(stopLon);
        this.mStopSequence = stopSequence;
    }

    public String getStopId() {
        return this.mStopId;
    }

    public String getStopName() {
        return this.mStopName;
    }

    public double getStopLat() {
        return this.mStopLat;
    }

    public double getStopLon() {
        return this.mStopLon;
    }

    public int getStopSequence() {
        return this.mStopSequence;
    }

    public Route getRoute() {
        return this.mRoute;
    }

    public void setRoute(Route route) {
        this.mRoute = route;
    }

    // ListView implementation.
    public String getTitle() {
        return this.mStopName;
    }

    public String getSubtitle() {
        return this.mStopId;
    }

    // Misc.
    public String getFull() {
        return this.mStopId + " - " + this.mStopName;
    }
}
