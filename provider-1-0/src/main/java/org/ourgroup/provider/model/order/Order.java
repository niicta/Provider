package org.ourgroup.provider.model.order;

import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.templates.ITemplate;
import org.ourgroup.provider.model.templates.Internet100;
import org.ourgroup.provider.model.templates.Internet50;
import org.ourgroup.provider.model.templates.InternetVIP;

import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 * Created by friend_RU.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "order")
public class Order {
    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "employeeId")
    private long employeeId;
    @XmlElement(name = "customerId")
    private long customerId;

    @XmlElements({
        @XmlElement(name = "internet50", type = Internet50.class),
        @XmlElement(name = "internet100",type = Internet100.class),
        @XmlElement(name = "internetVIP",type = InternetVIP.class)
    })
    private ITemplate template;
    @XmlElement(name = "instance")
    private Instance instance;
    @XmlElement(name = "requestDate")
    private Date requestDate;
    @XmlElement(name = "completeDate")
    private Date completeDate;
    @XmlElement(name = "status")
    private OrderStatus status;
    @XmlElement(name = "action")
    private OrderAction action;


    public Order(long customerId, ITemplate template, Instance instance, OrderAction action) {
        //ID
        this.requestDate = new Date();
        this.customerId = customerId;
        this.action = action;
        this.status = OrderStatus.NOT_STARTED;
        this.template = template;
        this.instance = instance;
        this.employeeId = -1;
    }

    // This constructor is need for XML-loading
    public Order() {        
    }

    public long getId() {
        return id;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public ITemplate getTemplate() {
        return template;
    }

    public Instance getInstance() {
        return instance;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderAction getAction() {
        return action;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setTemplate(ITemplate template) {
        this.template = template;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public void setAction(OrderAction action) {
        this.action = action;
    }
    
    

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

}
