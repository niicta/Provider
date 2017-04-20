<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import = "org.ourgroup.provider.model.*" %>
<%@ page import = "org.ourgroup.provider.model.person.*" %>
<%@ page import = "org.ourgroup.provider.model.instance.*" %>
<%@ page import = "org.ourgroup.provider.model.templates.*" %>
<%@ page import = "org.ourgroup.provider.view.jsp.*" %>
<%@ page import = "java.util.Date" %>

<%
%>

<%	ModelOperations mO = ModelOperations.getModelOperations();
	Customer customer = mO.getCustomerById(Long.parseLong(request.getParameter("personId")));
ServletHelper sH = new ServletHelper();%>

<!DOCTYPE html>
<html>

	<head>
		<title>Customer: <%=customer.getName()%> <%=customer.getSurname()%></title>
		<link rel="stylesheet" href="customer.css" type="text/css">
		<script>
            var modal = document.getElementById('myModal');

            var span = document.getElementsByClassName("close")[0];

            function changeTemplate(instanceId) {
                document.getElementById("instanceId").value = instanceId;
                document.getElementById('myModal').style.display = "block";
            }

            span.onclick = function() {
                modal.style.display = "none";
            }


            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }

            function send(customerAction,instanceId,templateName) {

                console.log(customerAction,instanceId,templateName);

                document.getElementById("customerAction").value = customerAction;
                document.getElementById("instanceId").value = instanceId;
                document.getElementById("templateName").value = templateName;

                document.getElementById("customerForm").submit();
            }
        </script>
	</head>

	<body style="font-family: Arial;background-image: url(Background.jpg);">
        <form action="/provider/providerServlet" method="post" name="exitForm" id="exitForm">
            <input type="hidden" name="personId" id="personId" value="-1" >

            <div id="topper">
                <table>
                    <tr>
                        <td>
                            <strong>Provider&#153 - the best Internet provider</strong>
                        </td>
                        <td align="right">
                            <text>You logged in as </text>
                            <text style="color: #FFF;"><%=customer.getName()%> <%=customer.getSurname()%></text>
                            <a href=javascript:exit();>Exit</a>
                        </td>
                    </tr>

                    <tr>
                        <td>

                        </td>

                        <td align="right">
                            <text>Your balance: <%= customer.getBalance() %> rub</text>
                        </td>
                    </tr>
                </table>
            </div>

            <script>
                function exit() {
                    document.getElementById("exitForm").submit();
                }
            </script>

        </form>

        <hr style="min-width: 770;opacity: 0;">

        <form action="/provider/customerServlet" method="post" name="customerForm" id="customerForm">
			<input type="hidden" name="customerAction" id="customerAction">
			<input type="hidden" name="instanceId" id="instanceId">
			<input type="hidden" name="personId" id="personId" value="<%= customer.getId() %>" >
			<input type="hidden" name="templateName" id="templateName">

			<div id="midder">
				<table>
					<tr id="header">
						<td id="yourIns">Your instances</td>
						<td id="allIns">All instances</td>
					</tr>

					<tr id="innerTables">
						<td valign="top">
							<table class="table1">
								<tr>

									<td class="visibleField" align="left">
										Name
									</td>

									<td class="visibleField" align="left">
										Status
									</td>

									<td class="visibleField" align="left">
										Days
									</td>

									<td></td>

									<td></td>

									<td></td>

								</tr>

								<% for (Instance instance : mO.getInstances(customer.getId())) { %>
								<tr>
									<td id="instanceName">
										<%= instance.getTemplate().getName() %>
									</td>

									<td id="instanceStatus">
										<%= sH.reformat(instance.getStatus()) %>
									</td>

									<td id="instanceLeft">
										<%= sH.getTimeOfAction(instance) %>
									</td>

									<% if (sH.isFreeInstance(instance)) {%>
									<td id="buttonSuspendActivate">
										<a class="activeLabel" href=javascript:send("suspendActivate","<%= instance.getId() %>","-1");><%= instance.getStatus()==InstanceStatus.ACTIVE? "Suspend":"Activate" %></a>
									</td>

									<td id="buttonChange">

										<a class="activeLabel" id="myBtn" href=javascript:changeTemplate(<%= instance.getId() %>);>Change</a>

									</td>

									<td id="buttonStop">
										<a class="activeLabel" href=javascript:send("stop","<%= instance.getId() %>","-1");>Stop</a>
									</td>
									<% } else { %>
								    <td id="buttonSuspendActivate">
                                        <a class="inactiveLabel"><%= instance.getStatus()==InstanceStatus.ACTIVE? "Suspend":"Activate" %></a>
									</td>

                                    <td id="buttonChange">

                                        <a class="inactiveLabel">Change</a>

									</td>

                                    <td id="buttonStop">
                                        <a class="inactiveLabel">Stop</a>
									</td>
									<% } %>
								</tr>
								<% } %>
							</table>
						</td>

						<td valign="top">
							<table class="table1">

							<tr>

									<td class="visibleField" align="left">
										Name
									</td>

									<td class="visibleField" align="left">
										Speed(MB)
									</td>

									<td class="visibleField" align="left">
										Limit(Gb)
									</td>

									<td class="visibleField" align="left">
										Cost(Rub)
									</td>

									<td>

									</td>

								</tr>

								<% for (ITemplate template : mO.getTemplates()) { %>
								<tr>

									<td id="templateName">
										<%= template.getName() %>
									</td>

									<td id="templateSpeed">
										<%= template.getSpeed() %> Mb
									</td>

									<td id="templateLimit">
										<%= template.getLimit() %> Gb
									</td>

									<td id="templateCost">
										<%= template.getCost() %> rub
									</td>

									<td id="buttonConnect">
										<a class="activeLabel" href=javascript:send("connect","-1","<%= template.getName() %>");>Connect</a>
									</td>
								</tr>
								<% } %>
							</table>
						</td>
					</tr>
				</table>
			</div>



            <!-- The Modal -->
            <div id="myModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <span class="close">x</span>
                        <h3>Please, choose new template</h3>
                    </div>
                    <div class="modal-body">
                        <table>
                            <tr>

                                <td class="visibleField" align="left">
                                    Name
                                </td>

                                <td class="visibleField" align="left">
                                    Speed(MB)
                                </td>

                                <td class="visibleField" align="left">
                                    Limit(Gb)
                                </td>

                                <td class="visibleField" align="left">
                                    Cost(Rub)
                                </td>

                                <td>

                                </td>

                            </tr>

                            <% for (ITemplate template : mO.getTemplates()) { %>
                            <tr>

                                <td id="templateName">
                                    "<%= template.getName() %>"
                                </td>

                                <td id="templateSpeed">
                                    <%= template.getSpeed() %> Mb
                                </td>

                                <td id="templateLimit">
                                    <%= template.getLimit() %> Gb
                                </td>

                                <td id="templateCost">
                                    <%= template.getCost() %> rub
                                </td>

                                <td id="buttonChangeModal">
                                    <a class="activeLabel" href=javascript:send("change",document.getElementById("instanceId").value,"<%= template.getName() %>");>Change</a>
                                </td>
                            </tr>
                            <% } %>
                        </table>
                    </div>
                </div>
            </div>
		</form>
	</body>
</html>
