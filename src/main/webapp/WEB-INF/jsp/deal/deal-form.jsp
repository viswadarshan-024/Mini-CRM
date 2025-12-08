<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Deal Form | Mini CRM</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-4">

    <div class="card">
        <div class="card-header">
            <h4>
                <s:if test="id == null">Create Deal</s:if>
                <s:else>Edit Deal</s:else>
            </h4>
        </div>

        <div class="card-body">

            <s:actionerror cssClass="alert alert-danger"/>

            <s:form action="deal!save" method="post">

                <s:hidden name="id"/>

                <div class="mb-3">
                    <label class="form-label">Title *</label>
                    <s:textfield name="title" cssClass="form-control"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Amount *</label>
                    <s:textfield name="amount" cssClass="form-control"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Company *</label>
                    <s:select name="companyId"
                              list="companies"
                              listKey="id"
                              listValue="name"
                              headerKey=""
                              headerValue="-- Select Company --"
                              cssClass="form-select"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Contact</label>
                    <s:select name="contactId"
                              list="contacts"
                              listKey="id"
                              listValue="name"
                              headerKey=""
                              headerValue="-- Optional --"
                              cssClass="form-select"/>
                </div>

                <s:if test="isAdmin() || isManager()">
                    <div class="mb-3">
                        <label class="form-label">Assign To</label>
                        <s:select name="assignedUserId"
                                  list="users"
                                  listKey="id"
                                  listValue="fullName"
                                  cssClass="form-select"/>
                    </div>
                </s:if>

                <s:if test="id != null">
                    <div class="mb-3">
                        <label class="form-label">Status</label>
                        <s:select name="status"
                                  list="statuses"
                                  cssClass="form-select"/>
                    </div>
                </s:if>

                <div class="d-flex justify-content-between">
                    <a href="deal.action" class="btn btn-secondary">Cancel</a>
                    <button class="btn btn-primary">Save</button>
                </div>

            </s:form>
        </div>
    </div>

</div>

</body>
</html>
