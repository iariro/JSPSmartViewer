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

			<table>
			<tr><th>Model</th><td><s:property value="modelName" /></td>
			<tr><th>Serial</th><td><s:property value="serialNumber" /></td>
			<tr><th>Firmware</th><td><s:property value="firmwareVersion" /></td>
			</table>

			<s:form action="currentlist" theme="simple">
				<s:submit value="最新の属性値" />
			</s:form>

			<s:form action="chronologygraph" theme="simple">
				<s:select name="ids" list="attributes" multiple="true" size="10" />
				<s:select name="field" list="#{ 'current':'カレント値', 'raw':'RAW値' }" />
				<s:submit value="属性値の遷移" />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
