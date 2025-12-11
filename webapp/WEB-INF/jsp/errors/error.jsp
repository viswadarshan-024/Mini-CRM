<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
</head>
<body>
<h3>Something went wrong</h3>

<p style="color:red">
    <s:property value="exception.message"/>
</p>

<a href="dashboard.action">Go back to Dashboard</a>
</body>
</html>
