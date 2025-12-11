<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contact Form | Mini CRM</title>

    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { margin-bottom: 15px; }
        form { width: 420px; }
        label { display: block; margin-top: 10px; }
        input[type="text"], input[type="email"] {
            width: 100%;
            padding: 6px;
            margin-top: 4px;
        }
        .buttons { margin-top: 15px; }
        .error { color: red; }
    </style>
</head>

<body>

<h2>
    <s:if test="id == null">Add Contact</s:if>
    <s:else>Edit Contact</s:else>
</h2>

<s:actionerror cssClass="error"/>

<s:form action="contact!save" method="post">

    <s:hidden name="id"/>

    <label>Company ID *</label>
    <s:textfield name="companyId" required="true"/>

    <label>Name *</label>
    <s:textfield name="name" required="true"/>

    <label>Email</label>
    <s:textfield name="email"/>

    <label>Phone</label>
    <s:textfield name="phone"/>

    <label>Job Title</label>
    <s:textfield name="jobTitle"/>

    <div class="buttons">
        <input type="submit" value="Save"/>
        <a href="contact.action">Cancel</a>
    </div>

</s:form>

</body>
</html>
