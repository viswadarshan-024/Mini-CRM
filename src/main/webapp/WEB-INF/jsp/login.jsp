<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>Login | Mini CRM</title>
</head>
<body>

<h2>Mini CRM Login</h2>

<s:actionerror />

<s:form action="login" method="post">

    <s:textfield
            name="username"
            label="Username"
            required="true"/>

    <s:password
            name="password"
            label="Password"
            required="true"/>

    <s:submit value="Login"/>

</s:form>

</body>
</html>
