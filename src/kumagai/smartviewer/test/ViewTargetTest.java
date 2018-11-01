package kumagai.smartviewer.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import kumagai.smartviewer.DriveSize;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.struts2.ViewTarget;

public class ViewTargetTest
{
	public static void main(String[] args)
		throws FileNotFoundException, IOException
	{
		ViewTarget viewTarget = new ViewTarget("testdata", "smartctl", "test");
		SmartDataList smartDataList = viewTarget.loadSmartDataList2(10);
		for (SmartData smartData : smartDataList)
		{
			System.out.println(smartData.getDateTime());
			for(DriveSize size : smartData.driveSizeArray)
			{
				System.out.printf("\t%s\n", size);
			}
		}
	}
}
