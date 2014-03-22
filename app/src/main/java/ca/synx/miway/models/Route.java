package ca.synx.miway.models;

import ca.synx.miway.interfaces.IListItem;

public class Route implements IListItem {

    public int routeId;
    public int routeNumber;
    public String routeName;
    public String routeHeading;

    public Route(int routeId, int routeNumber, String routeName, String routeHeading) {
        this.routeId = routeId;
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.routeHeading = routeHeading;
    }

    public String getTitle() {
        return this.routeName;
    }

    public String getSubtitle() {
        return this.routeHeading;
    }
}