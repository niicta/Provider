package org.ourgroup.provider.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by friend_RU.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "internetVIP")
public class InternetVIP implements ITemplate {

    private static final String NAME = "InternetVIP";
    private static final int SPEED = 175;
    private static final int LIMIT = 0;
    private static final int COST = 990;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSpeed() {
        return SPEED;
    }

    @Override
    public int getLimit() {
        return LIMIT;
    }

    @Override
    public int getCost() {
        return COST;
    }

    public String toString() {
        return NAME + "\n   Speed: " + SPEED + ", Limit: not limited" + ", Cost: " + COST;
    }

}
