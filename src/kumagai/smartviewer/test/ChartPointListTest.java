package kumagai.smartviewer.test;

import java.text.*;
import junit.framework.*;
import kumagai.smartviewer.*;

public class ChartPointListTest
	extends TestCase
{
	public void test1() throws ParseException
	{
		ChartPointList pointList = new ChartPointList("weight");
		pointList.put("2017/04/20 00:00:00", 1);
		pointList.put("2017/04/21 00:00:00", 2);
		assertEquals("{name: 'weight',data: [[Date.parse('2017/04/20 00:00:00'), 1],[Date.parse('2017/04/21 00:00:00'), 2]]}", pointList.toString());
	}
}
