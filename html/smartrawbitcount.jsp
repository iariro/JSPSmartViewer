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

			<h2><s:property value="datetime" /> SMART RAW値ビット集計</h2>
			<table>
				<tr>
					<th>属性</th>
					<th>Raw値</th>
				</tr>
				<s:iterator value="rawBitCount">
					<tr>
						<td><s:property value="key" /></td>
						<td align="right"><s:property value="value" /></td>
					</tr>
				</s:iterator>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
