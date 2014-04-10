/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import java.io.Serializable;

import ca.synx.miway.interfaces.IListItem;
import ca.synx.miway.interfaces.ISpinnerItem;

public class Route implements IListItem, ISpinnerItem, Serializable {
    private String mRouteNumber;
    private String mRouteName;
    private String mRouteHeading;
    private int mHashCode = -1;

    public Route(String routeNumber, String routeName, String routeHeading) {
        this.mRouteNumber = routeNumber;
        this.mRouteName = routeName;
        this.mRouteHeading = routeHeading;
    }

    public String getRouteName() {
        return this.mRouteName;
    }

    public String getRouteNumber() {
        return this.mRouteNumber;
    }

    public String getRouteHeading() {
        return this.mRouteHeading;
    }

    public String getTitle() {
        return this.mRouteNumber + " " + this.mRouteName;
    }

    public String getSubtitle() {
        return this.mRouteHeading;
    }

    public String getSpinnerTitle() {
        return this.mRouteNumber + " " + this.mRouteName + " (" + this.mRouteHeading + ")";
    }

    public String getFull() {
        return this.mRouteNumber + this.mRouteHeading.substring(0, 1) + " - " + this.mRouteName;
    }

    public int hashCode() {

        if (mHashCode != -1)
            mHashCode = mRouteNumber.hashCode() +
                    mRouteHeading.hashCode() +
                    mRouteName.hashCode();

        return mHashCode;
    }

    public boolean isEqual(Route route) {
        boolean isMatch = (route.hashCode() == this.hashCode());

        return isMatch;
    }
}