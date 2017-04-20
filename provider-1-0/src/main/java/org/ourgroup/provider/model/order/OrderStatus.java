package org.ourgroup.provider.model.order;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by friend_RU.
 */
@XmlType(name = "orderStatus")
@XmlEnum
public enum OrderStatus {
    @XmlEnumValue("NOT_STARTED")
    NOT_STARTED, 
    
    @XmlEnumValue("IN_PROCESS")
    IN_PROCESS, 
    
    @XmlEnumValue("COMPLETE")
    COMPLETE
}
