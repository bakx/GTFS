/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;

import ca.synx.miway.interfaces.IListItem;

public class StopTime implements IListItem, Serializable {
    private String mArrivalTime;
    private String mDepartureTime;
    private Stop mStop;

    public StopTime(String arrivalTime, String departureTime) {
        this.mArrivalTime = arrivalTime;
        this.mDepartureTime = departureTime;
    }

    public String getArrivalTime() {
        return this.mArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.mArrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return this.mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.mDepartureTime = departureTime;
    }

    public Stop getStop() {
        return this.mStop;
    }

    public void setStop(Stop stop) {
        this.mStop = stop;
    }

    public String getTitle() {
        return this.mDepartureTime;
    }

    public String getSubtitle() {
        return "";
    }
}
