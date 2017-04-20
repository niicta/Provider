package org.ourgroup.provider.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by friend_RU.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "internet100")
public class Internet100 implements ITemplate {

    private static final String NAME = "Internet100";
    private static final int SPEED = 100;
    private static final int LIMIT = 30;
    private static final int COST = 650;

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
        return NAME + "\n   Speed: " + SPEED + ", Limit: " + LIMIT + ", Cost: " + COST;
    }

}
