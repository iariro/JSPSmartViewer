package kumagai.smartviewer;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class SmartDataList
	extends ArrayList<SmartData>
{
	private static final int blockSize = 16 + 512 * 4;

	public static void main(String[] args)
		throws IOException
	{
		String basePath = "C:\\temp\\smart\\";

		for (String filename : new File(basePath).list())
		{
			File file = new File(basePath, filename);
			FileInputStream stream = new FileInputStream(file);
			int size = (int)file.length();
			byte [] data = new byte [size];
			stream.read(data);
			SmartDataList smartDataList = new SmartDataList(data);
			System.out.printf("%s %d %d\n", filename, size, smartDataList.size());
			
			for (SmartData smartData : smartDataList)
			{
				System.out.printf(smartData.getDateTime());
				
				for (SmartAttribute attribute : smartData.attributes)
				{
					System.out.printf(
						" %d=%d",
						attribute.getId(),
						attribute.getCurrent());
				}
				System.out.println();
			}
		}
	}

	public SmartDataList()
	{		
	}

	public SmartDataList(byte [] data)
	{
		for (int offset=0 ; offset<data.length ; offset+=blockSize)
		{
			add(new SmartData(data, offset));
		}
	}

	public SmartGraphDocumentPointList getSmartGraphDocumentPointList(Dimension screen)
		throws ParseException
	{
		SmartGraphDocumentPointList points = new SmartGraphDocumentPointList(this, screen);
		
		return points;
	}
}
