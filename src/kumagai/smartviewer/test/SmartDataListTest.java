package kumagai.smartviewer.test;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

import kumagai.smartviewer.*;

public class SmartDataListTest
{
	public static void main(String[] args)
		throws IOException, ParseException
	{
		String smartFilePath = "C:/Temp/smart_5087/";

		SmartDataList points = new SmartDataList();
		String [] filenames = new File(smartFilePath).list();

		if (filenames != null)
		{
			// リストを取得できた

			for (String filename : filenames)
			{
				File file = new File(smartFilePath, filename);
				FileInputStream stream = new FileInputStream(file);
				int size = (int)file.length();
				byte [] data = new byte [size];
				stream.read(data);
				stream.close();

				points.addAll(new SmartDataList(data));
			}

			ValueAndHourCollection valueAndHourCollection =
				points.getFluctuationPoint(9, 9);
			ArrayList<Prediction> predictFailure =
				valueAndHourCollection.predictFailure();
			for (Prediction prediction : predictFailure)
			{
				System.out.println(prediction);
			}
		}
	}
}
