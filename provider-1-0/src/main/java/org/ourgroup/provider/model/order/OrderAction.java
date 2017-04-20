package org.ourgroup.provider.model.order;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by friend_RU.
 */
@XmlType(name = "orderAction")
@XmlEnum
public enum OrderAction {
    @XmlEnumValue("CONNECT")
    CONNECT, 
    @XmlEnumValue("SUSPEND")
    SUSPEND, 
    @XmlEnumValue("RESTART")
    RESTART,
    @XmlEnumValue("STOP")
    STOP, 
    @XmlEnumValue("CHANGE_TEMPLATE")
    CHANGE_TEMPLATE
}
