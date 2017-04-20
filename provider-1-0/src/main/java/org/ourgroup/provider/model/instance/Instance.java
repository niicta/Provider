package org.ourgroup.provider.model.instance;

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
@XmlType(name = "instance ")
public class Instance {

    //ILGO I see getId() method, but I cannot find id setting.
    //ILGO id только считывается, нигде не заполняется. Он всегда будет равен 0 в таком случае.
    @XmlElement(name = "id")
    private long id;
    @XmlElements({
            @XmlElement(name = "internet50", type = Internet50.class),
            @XmlElement(name = "internet100", type = Internet100.class),
            @XmlElement(name = "internetVIP", type = InternetVIP.class)
    })
    private ITemplate template;
    @XmlElement(name = "customerId")
    private long customerId;
    @XmlElement(name = "connectionDate")
    private Date connectionDate;
    @XmlElement(name = "status")
    private InstanceStatus status;

    //информация о последнем прерывании и возобновлении услуги
    @XmlElement(name = "suspendionDate")
    private Date suspendionDate;
    @XmlElement(name = "resumeDate")
    private Date resumeDate;
    @XmlElement(name = "delta")
    private int delta;


    public Instance(ITemplate template, long customerId) {

        this.template = template;
        this.customerId = customerId;
        this.connectionDate = new Date();
        this.status = InstanceStatus.ACTIVE;
        this.suspendionDate = null;
        this.resumeDate = null;
        this.delta = 0;
    }

    //данный конструктор необходим для загрузки класса из xml
    private Instance() {
    }

    public long getId() {
        return id;
    }

    public ITemplate getTemplate() {
        return template;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public InstanceStatus getStatus() {
        return status;
    }

    public void setTemplate(ITemplate template) {
        this.template = template;
    }

    public void setStatus(InstanceStatus status) {
        if (status == InstanceStatus.SUSPEND) {
            suspendionDate = new Date();
        }
        if (this.status == InstanceStatus.SUSPEND && status == InstanceStatus.ACTIVE) {
            resumeDate = new Date();
            delta += (int) (resumeDate.getTime() - suspendionDate.getTime()) / 1000 / 60 / 60 / 24;
        }
        this.status = status;

    }

    public int getDelta() {
        return delta;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public Date getSuspendionDate() {
        return suspendionDate;
    }

    public Date getResumeDate() {
        return resumeDate;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public void setSuspendionDate(Date suspendionDate) {
        this.suspendionDate = suspendionDate;
    }

    public void setResumeDate(Date resumeDate) {
        this.resumeDate = resumeDate;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }


    public String toStrings() {
        return "Name: " + template.getName() + "; Speed: " + template.getSpeed()
                + "; Limit: " + template.getLimit() + "; Cost: " + template.getCost()
                + "; Status: " + status + "; Conn.Date: " + connectionDate;
    }

}
