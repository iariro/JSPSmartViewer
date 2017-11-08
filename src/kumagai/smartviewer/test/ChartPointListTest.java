package kumagai.smartviewer.test;

import java.text.ParseException;

import junit.framework.TestCase;
import kumagai.smartviewer.ChartPointList;

public class ChartPointListTest
	extends TestCase
{
	public void test1() throws ParseException
	{
		ChartPointList pointList = new ChartPointList("weight");
		pointList.put("2017/04/20 00:00:00", 1L);
		pointList.put("2017/04/21 00:00:00", 2L);
		assertEquals("{name: 'weight',data: [[Date.parse('2017/04/20 00:00:00'), 1],[Date.parse('2017/04/21 00:00:00'), 2]]}", pointList.toString());
	}
}
