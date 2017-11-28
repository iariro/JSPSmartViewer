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

			<h2>時間帯</h2>
			<table>
				<tr>
					<th>時間</th>
					<th>カウント</th>
				</tr>
				<s:iterator value="usageStatistics.countByHour">
					<tr>
						<td align="right"><s:property value="key" /></td>
						<td align="right"><s:property value="value" /></td>
					</tr>
				</s:iterator>
			</table>

			<h2>曜日</h2>
			<table>
				<tr>
					<th>曜日</th>
					<th>カウント</th>
				</tr>
				<s:iterator value="usageStatistics.countByDayofweek">
					<tr>
						<td align="right"><s:property value="key" /></td>
						<td align="right"><s:property value="value" /></td>
					</tr>
				</s:iterator>
			</table>

			<h2>連続稼働時間</h2>
			<table>
				<tr>
					<th>時間</th>
					<th>カウント</th>
				</tr>
				<s:iterator value="usageStatistics.countByContinuousRunning">
					<tr>
						<td align="right"><s:property value="key" /></td>
						<td align="right"><s:property value="value" /></td>
					</tr>
				</s:iterator>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
