package ca.synx.miway.models;

/**
 * Created by Gideon on 3/20/14.
 */
public class Route {

    public Route(int routeId, int routeNumber, String routeName, String routeHeading) {
        this.routeId = routeId;
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.routeHeading = routeHeading;
    }

    public int routeId;
    public int routeNumber;
    public String routeName;
    public String routeHeading;
}
