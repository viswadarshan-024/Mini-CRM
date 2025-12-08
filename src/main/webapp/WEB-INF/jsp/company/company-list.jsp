<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Companies</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h2 {
            margin-bottom: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
        }
        th {
            background-color: #f5f5f5;
        }
        .actions a {
            margin-right: 8px;
        }
        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .error {
            color: red;
        }
    </style>
</head>

<body>

<h2>Companies</h2>

<!-- ================= SEARCH ================= -->
<s:form action="company" method="get">
    <input type="text" name="keyword" placeholder="Search by name or email"
           value="<s:property value='keyword'/>"/>
    <input type="submit" value="Search"/>
</s:form>

<div class="top-bar">
    <div>
        <a href="dashboard.action">← Back to Dashboard</a>
    </div>

    <!-- ADD COMPANY -->
    <div>
        <s:if test="isAdmin() || isManager() || isSales()">
            <a href="company!add.action">+ Add Company</a>
        </s:if>
    </div>
</div>

<!-- ================= ERRORS ================= -->
<s:actionerror cssClass="error"/>

<!-- ================= TABLE ================= -->
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Created At</th>
        <th>Actions</th>
    </tr>
    </thead>

    <tbody>
    <s:iterator value="companies">
        <tr>
            <td><s:property value="name"/></td>
            <td><s:property value="email"/></td>
            <td><s:property value="phone"/></td>
            <td><s:property value="createdAt"/></td>

            <td class="actions">
                <!-- EDIT -->
                <s:if test="isAdmin() || isManager() || isSales()">
                    <a href="company!edit.action?id=<s:property value='id'/>">Edit</a>
                </s:if>

                <!-- DELETE -->
                <s:if test="isAdmin()">
                    |
                    <a href="company!delete.action?id=<s:property value='id'/>"
                       onclick="return confirm('Delete this company?');">
                        Delete
                    </a>
                </s:if>
            </td>
        </tr>
    </s:iterator>
    </tbody>
</table>

</body>
</html>
