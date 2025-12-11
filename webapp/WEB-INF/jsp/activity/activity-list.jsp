<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Activities | Mini CRM</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-4">

    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3>Activities</h3>
        <a href="activity-add.action" class="btn btn-primary">
            + New Activity
        </a>
    </div>

    <!-- Errors -->
    <s:actionerror cssClass="alert alert-danger"/>

    <!-- Table -->
    <table class="table table-bordered table-hover bg-white">
        <thead class="table-light">
        <tr>
            <th>Deal</th>
            <th>Type</th>
            <th>Status</th>
            <th>Description</th>
            <th>Due Date</th>
            <th>Actions</th>
        </tr>
        </thead>

        <tbody>
        <s:iterator value="activities">
            <tr>
                <td><s:property value="dealTitle"/></td>

                <td>
                    <span class="badge bg-secondary">
                        <s:property value="type"/>
                    </span>
                </td>

                <td>
                    <s:if test="status.name() == 'PENDING'">
                        <span class="badge bg-warning">Pending</span>
                    </s:if>
                    <s:else>
                        <span class="badge bg-success">Done</span>
                    </s:else>
                </td>

                <td><s:property value="description"/></td>

                <td><s:property value="dueDate"/></td>

                <td>
                    <!-- Edit -->
                    <s:if test="status.name() == 'PENDING'">
                        <a href="activity-edit.action?id=<s:property value='id'/>"
                           class="btn btn-sm btn-outline-primary">
                            Edit
                        </a>
                    </s:if>

                    <!-- Mark Done -->
                    <s:if test="status.name() == 'PENDING'">
                        <a href="activity-done.action?id=<s:property value='id'/>"
                           class="btn btn-sm btn-outline-success"
                           onclick="return confirm('Mark activity as done?');">
                            Done
                        </a>
                    </s:if>

                    <!-- Delete -->
                    <s:if test="isAdmin() || isManager()">
                        <a href="activity-delete.action?id=<s:property value='id'/>"
                           class="btn btn-sm btn-outline-danger"
                           onclick="return confirm('Delete this activity?');">
                            Delete
                        </a>
                    </s:if>
                </td>
            </tr>
        </s:iterator>

        <s:if test="activities == null || activities.isEmpty()">
            <tr>
                <td colspan="6" class="text-center text-muted">
                    No activities found
                </td>
            </tr>
        </s:if>
        </tbody>
    </table>

    <a href="dashboard.action">← Back to Dashboard</a>

</div>

</body>
</html>
