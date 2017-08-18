package kumagai.smartviewer;

import java.io.*;
import java.text.*;
import java.util.*;
import ktool.datetime.*;

/**
 * SMART情報一式オブジェクトのコレクション
 */
public class SmartDataList
	extends ArrayList<SmartData>
{
	/**
	 * テストコード
	 * @param args 未使用
	 * @throws IOException
	 */
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

	/**
	 * 複数のSMARTログファイルを一括読み込み
	 * @param filesPath SMARTログファイルパス
	 * @return SmartDataのリスト
	 */
	static public SmartDataList getSmartDataFiles(String filesPath)
			throws FileNotFoundException, IOException
	{
		SmartDataList points = new SmartDataList();
		String [] filenames = new File(filesPath).list();
		if (filenames != null)
		{
			// リストを取得できた
	
			for (String filename : filenames)
			{
				File file = new File(filesPath, filename);
				FileInputStream stream = new FileInputStream(file);
				int size = (int)file.length();
				byte [] data = new byte [size];
				stream.read(data);
				stream.close();
	
				points.addAll(new SmartDataList(data));
			}
		}
		return points;
	}

	/**
	 * 既定のコンストラクタ
	 */
	public SmartDataList()
	{
		// 何もしない
	}

	/**
	 * ログファイルの内容からSMART情報一式オブジェクトのコレクションを構築
	 * @param data ログファイル内容バイナリ
	 */
	public SmartDataList(byte [] data)
	{
		for (int offset=0 ; offset<data.length ; )
		{
			SmartData smartData = new SmartData(data, offset);
			add(smartData);
			offset += smartData.bufferSize;
		}
	}

	/**
	 * SMART値の変動ポイントを通算稼働時間とともに取得
	 * @param powerOnHoursId 通算稼働時間属性ID
	 * @param valueId 対象の値の属性ID
	 * @return 変動ポイントコレクション
	 */
	public ValueAndHourCollection getFluctuationPoint(int powerOnHoursId, int valueId)
		throws ParseException
	{
		ValueAndHourCollection valueAndHourCollection = new ValueAndHourCollection();
		int powerOnHours = -1;
		int loadUnloadSycleCount = -1;

		for (SmartData data : this)
		{
			int powerOnHours2 = -1;
			int loadUnloadSycleCount2 = -1;

			for (SmartAttribute attribute : data.attributes)
			{
				if (attribute.getId() == powerOnHoursId)
				{
					// 通算稼働時間の属性である

					powerOnHours2 = (int)attribute.getRawValue();
				}

				if (attribute.getId() == valueId)
				{
					// 対象の属性である

					loadUnloadSycleCount2 = attribute.getCurrent();
				}
			}

			if ((powerOnHours >= 0 && loadUnloadSycleCount >= 0) &&
				(powerOnHours2 >= 0 && loadUnloadSycleCount2 >= 0))
			{
				// 値は取得できた

				if (/*(powerOnHours < 0 && loadUnloadSycleCount < 0) ||*/
					(powerOnHours2 != powerOnHours && loadUnloadSycleCount2 != loadUnloadSycleCount))
				{
					// 変動している

					valueAndHourCollection.add(
						new ValueAndHour(
							powerOnHours2,
							loadUnloadSycleCount2,
							DateTime.parseDateTimeString(data.getDateTime())));
				}
			}

			powerOnHours = powerOnHours2;
			loadUnloadSycleCount = loadUnloadSycleCount2;
		}

		return valueAndHourCollection;
	}
}
