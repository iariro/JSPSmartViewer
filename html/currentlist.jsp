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


			<h2>最新のSMART属性値 <s:property value="datetime" /></h2>
			<table>
				<tr>
					<th>ID</th>
					<th>名前</th>
					<th>カレント値</th>
					<th>ワースト値</th>
					<th>Raw値</th>
					<th>Raw値</th>
				</tr>
				<s:iterator value="attributes">
					<tr>
						<td align="right"><s:property value="id" /></td>
						<td><s:property value="attributeName" /></td>
						<td align="right"><s:property value="current" /></td>
						<td align="right"><s:property value="worst" /></td>
						<td align="right"><s:property value="rawValue" /></td>
						<td align="right"><s:property value="rawValueDump" /></td>
					</tr>
				</s:iterator>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
