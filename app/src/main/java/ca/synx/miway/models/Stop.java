package ca.synx.miway.models;

import ca.synx.miway.interfaces.IListItem;

/**
 * Created by Gideon on 3/20/14.
 */
public class Stop implements IListItem {
    public String stopId;
    public String stopName;
    public int stopSequence;

    public String getTitle() {
        return this.stopName;
    }

    public String getSubtitle() {
        return this.stopId;
    }
}
