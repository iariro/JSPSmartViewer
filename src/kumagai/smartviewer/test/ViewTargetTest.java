package kumagai.smartviewer.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import kumagai.smartviewer.ChronologyGraph;
import kumagai.smartviewer.DriveSize;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.struts2.ViewTarget;

public class ViewTargetTest
{
	public static void main(String[] args)
		throws FileNotFoundException, IOException
	{
		ViewTarget viewTarget = new ViewTarget("C:/ProgramData/SMARTLogger/smart_Apart_Mac", "smartctl", "test", "note");
		SmartDataList smartDataList = viewTarget.loadSmartDataList(1);
		if (smartDataList != null)
		{
			// データ取得できた

			for (SmartData smart : smartDataList)
			{
				System.out.printf("%s %s\n", smart.getDateTime(), smart.driveSizeArray);
			}

			ArrayList<String> paritions = new ArrayList<>();
			for (int i=smartDataList.size() - 1 ; i>=0 ; i--)
			{
				ArrayList<DriveSize> driveSize = smartDataList.get(i).driveSizeArray;
				if (driveSize != null)
				{
					for (DriveSize size : driveSize)
					{
						paritions.add(size.partition);
					}
					break;
				}
			}
			System.out.println(paritions);

			LinkedHashMap<String,String> sizes =
				ChronologyGraph.createDriveSizeHighChartsPoints
					(smartDataList, paritions, true);
			for (Entry<String, String> size : sizes.entrySet())
			{
				System.out.println(size.getKey());
			}

			LinkedHashMap<Integer,int[]> rawBitCount = smartDataList.getRawBitCount();
			for(Entry<Integer, int[]> attribute : rawBitCount.entrySet())
			{
				System.out.printf("#%d :", attribute.getKey());
				for (int count : attribute.getValue())
				{
					System.out.printf(" %d", count);
				}
				System.out.println();
			}
		}
	}
}
