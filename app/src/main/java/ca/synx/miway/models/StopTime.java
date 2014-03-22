package ca.synx.miway.models;

import ca.synx.miway.interfaces.IListItem;

/**
 * Created by Gideon on 3/20/14.
 */
public class StopTime implements IListItem {
    public String arrivalTime, departureTime, stop_id;

    public String getTitle() {
        return "";
    }

    public String getSubtitle() {
        return "";
    }
}
