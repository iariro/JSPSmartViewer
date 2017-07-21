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
		<h1>SMART Viewer - <s:property value='targetName' /></h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<table>
			<tr><th>Model</th><td><s:property value="modelName" /></td>
			<tr><th>Serial</th><td><s:property value="serialNumber" /></td>
			<tr><th>Firmware</th><td><s:property value="firmwareVersion" /></td>
			</table>

			<s:form action="currentlist" theme="simple">
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:submit value="最新の属性値" />
			</s:form>

			<s:form action="predictfailure" theme="simple">
				powerOnHoursID：<input type="text" name="powerOnHoursId" value="9" size="3">
				valueID：<input type="text" name="valueId" value="9" size="3">
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:submit value="故障予測" />
			</s:form>

			<s:form action="chronologygraph" theme="simple">
				<s:select name="ids" list="attributes" multiple="true" size="10" />
				<s:select name="graphType" list="#{ 'HighCharts':'HighCharts', 'SVG':'SVG' }" />
				<s:select name="field" list="#{ 'current':'カレント値', 'raw':'RAW値' , 'raw2':'RAW値2バイト' }" />
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:submit value="属性値の遷移" />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
