<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import = "org.ourgroup.provider.model.*" %>
<%@ page import = "org.ourgroup.provider.model.person.*" %>
<%@ page import = "org.ourgroup.provider.model.instance.*" %>
<%@ page import = "org.ourgroup.provider.model.order.*" %>
<%@ page import = "org.ourgroup.provider.model.templates.*" %>
<%@ page import = "org.ourgroup.provider.view.jsp.*" %>
<%@ page import = "java.util.Date" %>

<%	ModelOperations mO = ModelOperations.getModelOperations();
	Employee employee = mO.getEmployeeById(Long.parseLong(request.getParameter("personId")));
ServletHelper sH = new ServletHelper();%>

<html>
	<head>
		<title>Employee: <%=employee.getName()%> <%=employee.getSurname()%></title>
        <link rel="stylesheet" href="employee.css" type="text/css">

        <script>

		</script>
	</head>

    <body style="font-family: Arial;background-image: url(Background.jpg);">

		<table>
			<form action="/provider/providerServlet" method="post" name="exitForm" id="exitForm">
			    <input type="hidden" name="personId" id="personId" value="-1" >

				<tr>
					<td align="right">
						<text>You logged in as </text>
						<text style="color: #FFF;"><%=employee.getName()%> <%=employee.getSurname()%></text>
						<a href=javascript:exit(); style="margin-right: 20;">Exit</a>
					</td>
				</tr>

				<script>
                    function exit() {
                        document.getElementById("exitForm").submit();
                    }
                </script>
			</form>
			
			<form action="/provider/employeeServlet" method="post" name="employeeForm" id="employeeForm">
			    <input type="hidden" name="employeeAction" id="employeeAction"> <!-- employee_start_order oId eId   employee_complete_order-->
                <input type="hidden" name="orderId" id="orderId">
                <input type="hidden" name="personId" id="personId" value="<%= employee.getId() %>" >

				<tr>
					<td>
						<div class="notebook2">
							<input type="radio" name="notebook2a" id="notebook2a_1" checked="checked">
							<input type="radio" name="notebook2a" id="notebook2a_2">
							<input type="radio" name="notebook2a" id="notebook2a_3">
							<input type="radio" name="notebook2a" id="notebook2a_4">

							<div style="font-family: Arial;">
							<%
							int employeeOrders = 0;
							for (Order order : mO.getOrders()) {
                                if ((order.getEmployeeId() == employee.getId())
                                && (!order.getStatus().equals(OrderStatus.COMPLETE))) {
                                    employeeOrders++;
                                }
                            }%>
								<label for="notebook2a_1">Your orders (<%= employeeOrders%>)</label>

								<table class="table1">
									<tr>
										<td class="field1" align="left">
											ID
										</td>

										<td class="field1" align="left">
											Action
										</td>

										<td class="field1" align="left">
											Template name
										</td>

										<td class="field1" align="left">
											Customer ID
										</td>

										<td class="field1" align="left">
											Status
										</td>


										<td class="field1" align="left">
											Request Date
										</td>

										<td></td>
									</tr>
									<%for (Order order : mO.getOrders()) {
										if ((order.getEmployeeId() == employee.getId())
										&& (!order.getStatus().equals(OrderStatus.COMPLETE))) {

									%>
									<tr>
										<td align="left">
											<%= order.getId()%>
										</td>

										<td align="left">
											<%= sH.reformat(order)%>
										</td>

										<% switch (order.getAction().toString()) {
										case("CONNECT"): {%>


										<td align="left">
											"<%= order.getTemplate().getName()%>"
										</td>
										<%break;
											}
										case("CHANGE_TEMPLATE"): {%>
										<td align="left">
											"<%= order.getInstance().getTemplate().getName()%>" on "<%= order.getTemplate().getName()%>"
										</td>

										<%break;
											}
										default: {%>
										<td align="left">
											"<%= order.getInstance().getTemplate().getName()%>"
										</td>
										<% break;
											}
										}%>

										<td>
											<a href=javascript:customerModal();><%= order.getCustomerId()%></a>
										</td>

										<td align="left">
											<%= sH.reformat(order.getStatus())%>
										</td>

										<td align="left">
											<%= order.getRequestDate()%>
										</td>

										<td align="right">
											<%if (!(order.getStatus().equals(OrderStatus.COMPLETE))) {%>
											<a class="activeLabel" href=javascript:accept(<%= order.getId()%>);>Accept</a>
											<%}%>
										</td>
									</tr>
									<%}
									}%>
								</table>
								<hr style="
								min-width: 770;
								opacity: 0;
								">

							</div>

							<div style="font-family: Arial;">
								<label for="notebook2a_2">All orders (<%= mO.getOrders().size()%>)</label>
								<table class="table1">

									<tr>
										<td class="field1" align="left">
                                        ID
										</td>

											<td class="field1" align="left">
												Action
											</td>

											<td class="field1" align="left">
												Template name
											</td>

											<td class="field1" align="left">
												Customer ID
											</td>

											<td class="field1" align="left">
												Status
											</td>


											<td class="field1" align="left">
												Request Date
											</td>

											<td></td>
										</tr>
										<%for (Order order : mO.getOrders()) {%>
										<tr>
											<td  align="left">
												<%= order.getId()%>
											</td>

											<td id="orderAction" align="left">
												<%= sH.reformat(order)%>
											</td>

											<% switch (order.getAction().toString()) {
											case("CONNECT"): {%>


											<td align="left">
												"<%= order.getTemplate().getName()%>"
											</td>
											<%break;
												}
											case("CHANGE_TEMPLATE"): {%>
											<td align="left">
												"<%= order.getInstance().getTemplate().getName()%>" on "<%= order.getTemplate().getName()%>"
											</td>

											<%break;
												}
											default: {%>
											<td align="left">
												"<%= order.getInstance().getTemplate().getName()%>"
											</td>
											<% break;
												}
											}%>

											<td>
												<a href=javascript:customerModal();><%= order.getCustomerId()%></a>
											</td>

											<td align="left">
												<%= sH.reformat(order.getStatus())%>
											</td>

											<td align="left">
												<%= order.getRequestDate()%>
											</td>

											<td align="right">
												<%if (!(order.getEmployeeId() > -1)) {%>
												<a class="activeLabel" href=javascript:take(<%= order.getId() %>);>Take to process</a>
												<%}%>
											</td>
										</tr>
										<%}%>
									</table>
									<hr style="min-width: 770;opacity: 0;">
								</div>

								<div style="font-family: Arial;">
									<label for="notebook2a_3">All instances</label>
									<table class="table1">

										<tr class="field1">
											<td align="left">
												ID
											</td>

											<td align="left">
												Customer ID
											</td>

											<td align="left">
												Template name
											</td>

											<td align="left">
												Status
											</td>

											<td align="left">
												Connection date
											</td>
										</tr>


										<%for (Instance instance : mO.getInstances()) {%>
										<tr>
											<td align="left">
												<%= instance.getId()%>
											</td>

											<td align="left">
												<%= instance.getCustomerId()%>
											</td>

											<td align="left">
												"<%= instance.getTemplate().getName()%>"
											</td>

											<td align="left">
												<%= instance.getStatus().toString()%>
											</td>

											<td align="left">
												<%= instance.getConnectionDate()%>
											</td>
										</tr>
										<%}%>
									</table>
									<hr style="min-width: 770;opacity: 0;">

								</div>
								<div style="font-family: Arial;">
									<label for="notebook2a_4">Customers</label>
									<table class="table1">

										<tr class="field1">
											<td align="left">
												ID
											</td>

											<td align="left">
												Full name
											</td>

											<td align="left">
												Balance
											</td>

											<td align="left">
												Instances
											</td>

											<td align="left">
												Orders
											</td>

										</tr>

										<%for (Customer customer: mO.getCustomers()) {%>

										<tr>
											<td align="left">
												<%= customer.getId()%>
											</td>

											<td align="left">
												<%= customer.getName()%> <%= customer.getSurname()%>
											</td>

											<td align="left">
												<%= customer.getBalance()%>
											</td>

											<td align="left">
												<%= mO.getInstances(customer.getId()).size()%>
											</td>

											<td align="left">
												<%= mO.getOrders(customer.getId()).size()%>
											</td>

										</tr>
										<%}%>
									</table>
									<hr style="min-width: 770;opacity: 0;">
								</div>
							</div>
						</td>
					</tr>




					<script>
					    function accept(orderId) {
					        console.log(orderId);

                            document.getElementById("employeeAction").value = "accept";
                            document.getElementById("orderId").value = orderId;

                            document.getElementById("employeeForm").submit();
                        }

                        function take(orderId) {
                            console.log(orderId);

                            document.getElementById("employeeAction").value = "take";
                            document.getElementById("orderId").value = orderId;

                            document.getElementById("employeeForm").submit();
                        }
					</script>
				</form>
			</table>
		</body>
	</html>