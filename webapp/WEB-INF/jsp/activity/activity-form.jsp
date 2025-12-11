<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Activity Form | Mini CRM</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-4">

    <h3>
        <s:if test="activity != null">Edit Activity</s:if>
        <s:else>Add Activity</s:else>
    </h3>

    <s:actionerror cssClass="alert alert-danger"/>

    <!-- ADD -->
    <s:if test="activity == null">
        <s:form action="activity-save" cssClass="card p-4 bg-white">

            <s:hidden name="createRequest.dealId"/>

            <div class="mb-3">
                <label class="form-label">Type</label>
                <s:select
                        name="createRequest.type"
                        list="{ 'TASK', 'CALL' }"
                        cssClass="form-select"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <s:textarea name="createRequest.description"
                            cssClass="form-control"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Due Date</label>
                <s:textfield name="createRequest.dueDate"
                             type="date"
                             cssClass="form-control"/>
            </div>

            <button class="btn btn-success">Save</button>
            <a href="activity.action" class="btn btn-secondary">Cancel</a>

        </s:form>
    </s:if>

    <!-- EDIT -->
    <s:else>
        <s:form action="activity-update" cssClass="card p-4 bg-white">

            <s:hidden name="id" value="%{activity.id}"/>

            <div class="mb-3">
                <label class="form-label">Type</label>
                <s:select
                        name="updateRequest.type"
                        list="{ 'TASK', 'CALL', 'EMAIL','MEETING' }"
                        value="%{activity.type}"
                        cssClass="form-select"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <s:textarea
                        name="updateRequest.description"
                        value="%{activity.description}"
                        cssClass="form-control"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Due Date</label>
                <s:textfield
                        name="updateRequest.dueDate"
                        type="date"
                        value="%{activity.dueDate}"
                        cssClass="form-control"/>
            </div>

            <button class="btn btn-success">Update</button>
            <a href="activity.action" class="btn btn-secondary">Cancel</a>

        </s:form>
    </s:else>

</div>

</body>
</html>
