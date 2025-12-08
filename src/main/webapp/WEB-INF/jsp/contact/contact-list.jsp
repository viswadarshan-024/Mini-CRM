<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contacts | Mini CRM</title>

    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { margin-bottom: 10px; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
        }
        th { background-color: #f5f5f5; }
        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .actions a { margin-right: 10px; }
        .error { color: red; }
    </style>
</head>

<body>

<h2>Contacts</h2>

<s:form action="contact" method="get">
    <input type="text" name="keyword"
           placeholder="Search name, email or phone"
           value="<s:property value='keyword'/>"/>
    <input type="submit" value="Search"/>
</s:form>

<div class="top-bar">
    <div>
        <a href="dashboard.action">← Back to Dashboard</a>
    </div>

    <!-- ADD CONTACT -->
    <div>
        <s:if test="isAdmin() || isManager() || isSales()">
            <a href="contact!add.action">+ Add Contact</a>
        </s:if>
    </div>
</div>

<s:actionerror cssClass="error"/>

<!-- ================= TABLE ================= -->
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Job Title</th>
        <th>Company</th>
        <th>Created At</th>
        <th>Actions</th>
    </tr>
    </thead>

    <tbody>
    <s:iterator value="contacts">
        <tr>
            <td><s:property value="name"/></td>
            <td><s:property value="email"/></td>
            <td><s:property value="phone"/></td>
            <td><s:property value="jobTitle"/></td>
            <td>
                <s:if test="companyName != null">
                    <s:property value="companyName"/>
                </s:if>
                <s:else>-</s:else>
            </td>
            <td><s:property value="createdAt"/></td>

            <td class="actions">
                <!-- EDIT -->
                <s:if test="isAdmin() || isManager() || isSales()">
                    <a href="contact!edit.action?id=<s:property value='id'/>">Edit</a>
                </s:if>

                <!-- DELETE -->
                <s:if test="isAdmin() || isManager()">
                    |
                    <a href="contact!delete.action?id=<s:property value='id'/>"
                       onclick="return confirm('Delete this contact?');">
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
