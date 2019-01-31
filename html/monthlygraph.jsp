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
		<div id="chart" style="width:1300px; height:700px"></div>
		<script type="text/javascript">
		function draw()
		{
			Highcharts.setOptions({ global: { useUTC: false } });
			options =
			{
				chart: {type: 'column', renderTo: "chart", zoomType:'xy', plotBackgroundColor: 'lightgray'},
				title: {text: 'S.M.A.R.T. - <s:property value='targetName' />'},
				plotOptions: {series:{marker:{enabled:true}}},
				xAxis: { categories: <s:property value='categories' /> },
				series: <s:property value='series' />
			};
			chart = new Highcharts.Chart(options);
		};
		document.body.onload = draw();
		</script>
	</body>
</html>
