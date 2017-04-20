package org.ourgroup.provider.view.jsp;

import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.instance.InstanceStatus;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.order.OrderStatus;

import java.util.Date;

public class ServletHelper {

    ModelOperations mO = ModelOperations.getModelOperations();

    public int getTimeOfAction(Instance instance) {
        Date date = new Date();
        int delta = instance.getDelta();
        return 29 - (int) ((date.getTime() - instance.getConnectionDate().getTime()) / 1000 / 60 / 60 / 24) + delta;
    }

    public String reformat(Order order) {
        String action = null;
        switch (order.getAction().toString()) {
            case ("CONNECT"): {
                action = "Connecting";
                break;
            }
            case ("SUSPEND"): {
                action = "Suspending";
                break;
            }
            case ("RESTART"): {
                action = "Restarting";
                break;
            }
            case ("STOP"): {
                action = "Stopping";
                break;
            }
            case ("CHANGE_TEMPLATE"): {
                action = "Changing";
                break;
            }
            default: {
                action = "Unknown action";
                break;
            }
        }
        return action;
    }

    public String reformat(OrderStatus orderStatus) {
        String status = null;
        switch (orderStatus.toString()) {
            case ("COMPLETE"): {
                status = "Complete";
                break;
            }
            case ("IN_PROCESS"): {
                status = "In process";
                break;
            }
            case ("NOT_STARTED"): {
                status = "Not started";
                break;
            }
            default: {
                status = "Unknown status";
                break;
            }
        }
        return status;
    }

    public String reformat(InstanceStatus instanceStatus) {
        String  status = null;

        switch (String.valueOf(instanceStatus)) {
            case ("ACTIVE"): {
                status = "Active";
                break;
            }
            case ("UNACTIVE"): {
                status = "Not active";
                break;
            }
            case ("SUSPEND"): {
                status = "Suspended";
                break;
            }
        }
        return status;
    }

    public boolean isFreeInstance(Instance instance) {
        for (Order order : mO.getOrders()) {//(order.getAction() != OrderAction.CONNECT) ||
            if (!(order.getStatus().equals(OrderStatus.COMPLETE)))  {

                if (order.getInstance() != null && order.getInstance().getId() == (instance.getId())) {
                    return false;
                }
            }
        }
        return true;
    }
}
