/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;

import ca.synx.miway.interfaces.IListItem;

public class Route implements IListItem, Serializable {

    public String routeNumber;
    public String routeName;
    public String routeHeading;

    public Route(String routeNumber, String routeName, String routeHeading) {
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.routeHeading = routeHeading;
    }

    public String getTitle() {
        return this.routeNumber + " " + this.routeName;
    }

    public String getSubtitle() {
        return this.routeHeading;
    }

    public String getFull() {
        return this.routeNumber + this.routeHeading.substring(0, 1) + " - " + this.routeName;
    }
}