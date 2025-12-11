<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Deals | Mini CRM</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        .badge-NEW { background-color: #6c757d; }
        .badge-IN_PROGRESS { background-color: #0d6efd; }
        .badge-QUALIFIED { background-color: #20c997; }
        .badge-DELIVERED { background-color: #198754; }
        .badge-CLOSED { background-color: #dc3545; }
    </style>
</head>

<body class="bg-light">

<div class="container mt-4">

    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3>Deals</h3>

        <s:if test="isAdmin() || isManager() || isSales()">
            <a href="deal!add.action" class="btn btn-primary">
                + New Deal
            </a>
        </s:if>
    </div>

    <!-- Search -->
    <s:form action="deal" method="get" cssClass="row g-2 mb-3">
        <div class="col-md-10">
            <input type="text" name="keyword" value="<s:property value='keyword'/>"
                   class="form-control" placeholder="Search by deal, company or contact"/>
        </div>
        <div class="col-md-2">
            <button class="btn btn-outline-secondary w-100">Search</button>
        </div>
    </s:form>

    <!-- Table -->
    <div class="card">
        <div class="card-body p-0">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                <tr>
                    <th>Title</th>
                    <th>Company</th>
                    <th>Owner</th>
                    <th>Status</th>
                    <th>Amount</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                <s:iterator value="deals">
                    <tr>
                        <td><s:property value="title"/></td>
                        <td><s:property value="companyName"/></td>
                        <td><s:property value="assignedUserName"/></td>

                        <td>
                            <span class="badge badge-${status}">
                                <s:property value="status"/>
                            </span>
                        </td>

                        <td>₹ <s:property value="amount"/></td>

                        <td>
                            <a href="deal!edit.action?id=<s:property value='id'/>"
                               class="btn btn-sm btn-outline-primary">
                                Edit
                            </a>

                            <s:if test="isAdmin()">
                                <a href="deal!delete.action?id=<s:property value='id'/>"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('Delete this deal?')">
                                    Delete
                                </a>
                            </s:if>
                        </td>
                    </tr>
                </s:iterator>

                <s:if test="deals == null || deals.isEmpty()">
                    <tr>
                        <td colspan="6" class="text-center text-muted p-4">
                            No deals found.
                        </td>
                    </tr>
                </s:if>

                </tbody>
            </table>
        </div>
    </div>

    <a href="dashboard.action" class="btn btn-link mt-3">← Back to Dashboard</a>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger">
            <s:actionerror/>
        </div>
    </s:if>
</div>

</body>
</html>
