<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*,java.util.*" %>
<html>
	<head>
		<title>Login</title>
	</head>
    <body style="font-family: Arial;background-image: url(Background.jpg);">
		<form action="/provider/providerServlet" method="post">
            <p align="center">
                <img src="Provider.png" alt="Provider" width="260" height="260">
            </p>
			<p align="center">
				<strong>Enter your Id</strong>
			</p>
			<p align="center">
				<input maxlength="25" size="40" id="personId" name="personId" value="Your ID...">
				<input type = "submit" value = "Log in"/>
			</p>
			<script>
				personId.onblur = function() {
					if (isNaN(this.value)) {
						alert( "ID must be is NUMBER" );
						this.value="Your ID...";
					}
					if (this.value=="")
					this.value="Your ID..."
				};
				personId.onfocus = function() {
					if (this.value=="Your ID...")
					this.value=""
				};
			</script>
		</form>
	</body>
</html>
