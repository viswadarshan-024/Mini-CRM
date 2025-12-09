<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Users | Mini CRM</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        h2 { margin-bottom: 15px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f4f4f4; }
        .top-bar {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }
        .btn {
            padding: 6px 12px;
            text-decoration: none;
            border: 1px solid #333;
            border-radius: 4px;
            background: #f8f8f8;
        }
        .btn-primary {
            background: #007bff;
            color: white;
            border: none;
        }
    </style>
</head>

<body>

<h2>User Management</h2>

<div class="top-bar">
    <a href="dashboard.action">← Back to Dashboard</a>
    <a href="user!add.action" class="btn btn-primary">+ Add User</a>
</div>

<s:actionerror cssStyle="color:red"/>

<table>
    <thead>
    <tr>
        <th>Username</th>
        <th>Email</th>
        <th>Full Name</th>
        <th>Role</th>
    </tr>
    </thead>

    <tbody>
    <s:iterator value="users">
        <tr>
            <td><s:property value="username"/></td>
            <td><s:property value="email"/></td>
            <td><s:property value="fullName"/></td>
            <td><s:property value="roleName"/></td>
        </tr>
    </s:iterator>
    </tbody>
</table>

</body>
</html>
