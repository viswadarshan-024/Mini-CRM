<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Create User | Mini CRM</title>

    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        h2 { margin-bottom: 15px; }
        form { width: 350px; }
        label { display: block; margin-top: 10px; }
        input, select {
            width: 100%;
            padding: 6px;
            margin-top: 4px;
        }
        .actions {
            margin-top: 15px;
        }
        .error {
            color: red;
        }
    </style>
</head>

<body>

<h2>Create User</h2>

<s:actionerror cssClass="error"/>

<s:form action="user!save" method="post">

    <label>Username *</label>
    <s:textfield name="username" required="true"/>

    <label>Email *</label>
    <s:textfield name="email" required="true"/>

    <label>Password *</label>
    <s:password name="password" required="true"/>

    <label>Full Name *</label>
    <s:textfield name="fullName" required="true"/>

    <label>Role</label>
    <s:select
            name="roleId"
            list="roleOptions"
            listKey="key"
            listValue="value"
            headerKey=""
            headerValue="-- Select Role --"
    />


    <div class="actions">
        <input type="submit" value="Create User"/>
        <a href="user.action">Cancel</a>
    </div>

</s:form>

</body>
</html>
