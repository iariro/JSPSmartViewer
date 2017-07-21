<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>SMART Viewer</title>
	</head>

	<body>
		<h1>SMART Viewer</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

		<s:form action="disktop" theme="simple">
			対象：
			<s:select name="targetName" list="targets" listKey="name" listValue="name" />
			<s:submit value="表示" />
		</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
