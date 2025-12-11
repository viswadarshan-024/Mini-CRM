<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Company Form</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h2 {
            margin-bottom: 15px;
        }
        form {
            width: 400px;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        input[type="text"], input[type="email"] {
            width: 100%;
            padding: 6px;
            margin-top: 4px;
        }
        .buttons {
            margin-top: 15px;
        }
        .error {
            color: red;
        }
    </style>
</head>

<body>

<h2>
    <s:if test="id == null">
        Add Company
    </s:if>
    <s:else>
        Edit Company
    </s:else>
</h2>

<s:actionerror cssClass="error"/>

<s:form action="company!save" method="post">

    <s:hidden name="id"/>

    <label>Name *</label>
    <s:textfield name="name" required="true"/>

    <label>Email</label>
    <s:textfield name="email"/>

    <label>Phone</label>
    <s:textfield name="phone"/>

    <label>Address</label>
    <s:textfield name="address"/>

    <div class="buttons">
        <input type="submit" value="Save"/>
        <a href="company.action">Cancel</a>
    </div>

</s:form>

</body>
</html>
