<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script type="text/javascript" src="http://code.highcharts.com/highcharts.js"></script>
		<title>SMART Viewer</title>
	</head>

	<body>
		<h1>SMART Viewer - <s:property value='targetName' /></h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<h2>時間帯</h2>
			<div id="chart" style="width:800px; height:400px"></div>
			<script type="text/javascript">
			function draw()
			{
				Highcharts.setOptions({ global: { useUTC: false } });
				options =
				{
					chart: {renderTo: 'chart', type:'column'},
					title: {text: '時間帯'},
					xAxis : { tickInterval:1 },
					series: [ {data:[<s:property value="usageStatistics.countByHour" />]} ]
				};
				chart = new Highcharts.Chart(options);
			};
			document.body.onload = draw();
			</script>

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
