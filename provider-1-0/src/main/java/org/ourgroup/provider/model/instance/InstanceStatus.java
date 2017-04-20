package org.ourgroup.provider.model.instance;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by friend_RU.
 */
@XmlType(name = "instanceStatus")
@XmlEnum
public enum InstanceStatus {
    @XmlEnumValue("ACTIVE")
    ACTIVE, 
    @XmlEnumValue("SUSPEND")
    SUSPEND, 
    @XmlEnumValue("UNACTIVE") // Why we need it? Instance will be deleted, how we can to take this status?
    UNACTIVE
}
