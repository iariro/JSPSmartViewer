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
			<div id="chart_hour" style="width:800px; height:400px"></div>
			<script type="text/javascript">
			function draw()
			{
				Highcharts.setOptions({ global: { useUTC: false } });
				options =
				{
					chart: {renderTo: 'chart_hour', type:'column'},
					title: {text: '時間帯'},
					xAxis : { tickInterval:1 },
					series: [ {data:[<s:property value="usageStatistics.countByHour" />]} ]
				};
				chart = new Highcharts.Chart(options);
			};
			document.body.onload = draw();
			</script>

			<h2>曜日</h2>
			<div id="chart_dayofweek" style="width:800px; height:400px"></div>
			<script type="text/javascript">
			function draw()
			{
				Highcharts.setOptions({ global: { useUTC: false } });
				options =
				{
					chart: {renderTo: 'chart_dayofweek', type:'column'},
					title: {text: '曜日'},
					xAxis : { tickInterval:1, categories:['日','月','火','水','木','金','土'] },
					series: [ {data:[<s:property value="usageStatistics.countByDayOfWeek" />]} ]
				};
				chart = new Highcharts.Chart(options);
			};
			document.body.onload = draw();
			</script>

			<h2>連続稼働時間</h2>
			<div id="chart_continuousrunning" style="width:800px; height:400px"></div>
			<script type="text/javascript">
			function draw()
			{
				Highcharts.setOptions({ global: { useUTC: false } });
				options =
				{
					chart: {renderTo: 'chart_continuousrunning', type:'column'},
					title: {text: '連続稼働時間'},
					series: [ {data:[<s:property value="usageStatistics.countByContinuousRunning" />]} ]
				};
				chart = new Highcharts.Chart(options);
			};
			document.body.onload = draw();
			</script>

		</div>
		</div>
		</div>

	</body>
</html>
