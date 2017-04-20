package org.ourgroup.provider.view.jsp;

import org.ourgroup.provider.controller.Controller;
import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.instance.InstanceStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomerServlet extends HttpServlet {
    ModelOperations mO = ModelOperations.getModelOperations();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        try {
            mO.demarshallModel();
        } catch (JAXBException e) {
            out.println("\n\nDemarshallException\n\n");
            e.printStackTrace(out);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerAction = (String) request.getParameter("customerAction");
        String instanceId = (String) request.getParameter("instanceId");
        String personId = (String) request.getParameter("personId");
        String templateName = (String) request.getParameter("templateName");

        try {
            switch (customerAction) {
                case ("connect"): {
                    Controller.parseCommand("connect " + templateName + " " + personId);
                    break;
                }
                case ("suspendActivate"): {
                    Instance instance = mO.getInstanceById(Long.parseLong(instanceId));
                    if (instance.getStatus() == InstanceStatus.ACTIVE) {
                        Controller.parseCommand("change_status suspend " + instanceId + " " + personId);
                    } else {
                        Controller.parseCommand("change_status restart " + instanceId + " " + personId);
                    }
                    break;
                }
                case ("change"): {
                    Instance instance = mO.getInstanceById(Long.parseLong(instanceId));
                    Controller.parseCommand("change_temp " + templateName + " " + instance.getId() + " " + personId);
                    break;
                }
                case ("stop"): {
                    Controller.parseCommand("change_status disconnect " + instanceId + " " + personId);
                    break;
                }
            }
        } catch (RuntimeException rE) {
            //Can be exception.JSP
        }

        getServletContext().getRequestDispatcher("/customer.jsp").forward(request, response);
    }
}
