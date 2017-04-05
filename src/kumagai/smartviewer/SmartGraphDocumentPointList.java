package kumagai.smartviewer;

import java.awt.*;
import java.text.*;
import java.util.*;
import ktool.datetime.*;

public class SmartGraphDocumentPointList
	extends ArrayList<SmartGraphDocumentPoint>
{
	int attributeId;
	float scaleX;
	float scaleY;
	int hourRange;
	int maxY = 255;
	DateTime maxHour;
	DateTime minHour;

	public SmartGraphDocumentPointList(ArrayList<SmartData> smartDataList, Dimension screen, int attributeId)
		throws ParseException
	{
		this.attributeId = attributeId;

		// 日時の幅を求める
		for (SmartData smartData : smartDataList)
		{
			DateTime datetime =
				DateTime.parseDateTimeString(smartData.getDateTime());
			
			if (maxHour == null || maxHour.compareTo(datetime) < 0)
			{
				maxHour = datetime;
			}
			
			if (minHour == null || minHour.compareTo(datetime) > 0)
			{
				minHour = datetime;
			}
		}

		hourRange = maxHour.diff(minHour).getHour();

		if (hourRange > 0)
		{
			// 更新あり

			scaleX = (float)screen.width / hourRange;
			scaleY = (float)screen.height / maxY;
		}
		else
		{
			// 更新なし

			scaleX = (float)screen.width;
			scaleY = (float)screen.height / maxY;

			// １件あることにする
			hourRange = 1;
		}
		
		for (SmartData smartData : smartDataList)
		{
			DateTime datetime =
				DateTime.parseDateTimeString(smartData.getDateTime());
			
			for (SmartAttribute attribute : smartData.attributes)
			{
				if (attribute.getId() == attributeId)
				{
					int diffHour = datetime.diff(minHour).getHour();

					add(
						new SmartGraphDocumentPoint(
							(int)(scaleX * diffHour),
							(int)(scaleY * (maxY - attribute.getCurrent()))));
				}
			}
		}
	}
}
