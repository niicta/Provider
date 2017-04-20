package org.ourgroup.provider.view.jsp;

import org.ourgroup.provider.controller.Controller;
import org.ourgroup.provider.model.ModelOperations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;

public class EmployeeServlet extends HttpServlet {
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
        String employeeAction = (String) request.getParameter("employeeAction");
        String orderId = (String) request.getParameter("orderId");
        String personId = (String) request.getParameter("personId");

        try {
            switch (employeeAction) {
                case ("accept"): {
                    Controller.parseCommand("employee_complete_order " + orderId + " " + personId);
                    break;
                }
                case ("take"): {
                    Controller.parseCommand("employee_start_order " + orderId + " " + personId);
                    break;
                }
            }
        } catch (RuntimeException rE) {
            //Can be exception.JSP
        }
        getServletContext().getRequestDispatcher("/employee.jsp").forward(request, response);
    }
}
