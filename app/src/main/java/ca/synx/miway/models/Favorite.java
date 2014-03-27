/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.models;

import ca.synx.miway.interfaces.IFavorite;

public class Favorite extends Stop implements IFavorite {

    private String favoriteId;

    public Favorite(String favoriteId, String stopId, String stopName, int stopSequence, Route route) {
        super(stopId, stopName, stopSequence);

        this.favoriteId = favoriteId;
        this.route = route;
    }

    @Override
    public String getTitle() {
        return this.route.routeNumber + " " + this.route.routeName;
    }

    @Override
    public String getSubtitle() {
        return this.route.routeHeading;
    }

    @Override
    public String getFull() {
        return this.route.routeNumber + this.route.routeHeading.substring(0, 1) + " - " + this.route.routeName;
    }

    public String getID() {
        return favoriteId;
    }
}
