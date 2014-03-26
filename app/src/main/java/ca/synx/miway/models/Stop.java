/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;

import ca.synx.miway.interfaces.IListItem;

public class Stop implements IListItem, Serializable {
    public String stopId;
    public String stopName;
    public int stopSequence;
    public Route route;

    public Stop(String stopId, String stopName, int stopSequence) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopSequence = stopSequence;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getTitle() {
        return this.stopName;
    }

    public String getSubtitle() {
        return this.stopId;
    }

    public String getFull() {
        return this.stopId + " - " + this.stopName;
    }
}
