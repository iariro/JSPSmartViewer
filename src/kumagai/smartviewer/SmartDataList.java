package kumagai.smartviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import ktool.datetime.DateTime;

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
	 * @throws ParseException 
	 */
	public static void main(String[] args)
		throws IOException, ParseException
	{
		String basePath = "C:\\ProgramData\\SMARTLogger\\smart_TOPS5366\\";
		int filenumlimit = 50;

		SmartDataList smartDataListAll = new SmartDataList();
		String [] filenames = new File(basePath).list();
		Arrays.sort(filenames);
		for (int i=0 ; i<filenames.length && i<filenumlimit ;i++)
		{
			String filename = filenames[filenames.length > filenumlimit ? filenames.length - filenumlimit + i : i];
			File file = new File(basePath, filename);
			FileInputStream stream = new FileInputStream(file);
			int size = (int)file.length();
			byte [] data = new byte [size];
			stream.read(data);
			stream.close();

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
			smartDataListAll.addAll(smartDataList);
		}

		int [] ids = smartDataListAll.getAscOrDescAttributeIds("ascending");
		for (int id : ids)
		{
			System.out.println(id);
		}

		UsageStatistics usageStatistics = smartDataListAll.getUsageStatistics();
		for (Map.Entry<Integer, Integer> kv : usageStatistics.countByHour.entrySet())
		{
			System.out.printf("%s : %s", kv.getKey(), kv.getValue());
			System.out.println();
		}
		for (Map.Entry<Integer, Integer> kv : usageStatistics.countByDayofweek.entrySet())
		{
			System.out.printf("%s : %s", kv.getKey(), kv.getValue());
			System.out.println();
		}
		for (Map.Entry<Integer, Integer> kv : usageStatistics.countByContinuousRunning.entrySet())
		{
			System.out.printf("%s : %s", kv.getKey(), kv.getValue());
			System.out.println();
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
			if (smartData.attributes.size() > 0)
			{
				// 取得失敗データ

				add(smartData);
			}
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

	/**
	 * 上昇・下降値のIDを取得
	 * @param mode ascending / descending
	 * @return 上昇値のID
	 */
	public int [] getAscOrDescAttributeIds(String mode)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<Integer> ascIds = new ArrayList<Integer>();
		if (size() > 0)
		{
			SmartData smartData0 = get(0);
			for (SmartAttribute attribute : smartData0.attributes)
			{
				ids.add(attribute.getId());
			}

			for (int id : ids)
			{
				int incCount = 0;
				int decCount = 0;
				Long pvalue = null;

				for (SmartData smartData : this)
				{
					for (SmartAttribute attribute : smartData.attributes)
					{
						if (attribute.getId() == id)
						{
							// 対象の属性

							if (pvalue != null)
							{
								// 比較対象あり

								if (pvalue < attribute.getRawValue())
								{
									// 増加

									incCount++;
								}
								else if (pvalue > attribute.getRawValue())
								{
									// 減少

									decCount++;
								}
							}
							pvalue = attribute.getRawValue();
							break;
						}
					}
				}

				if (!((mode.equals("ascending") ^ ((incCount > 0) && (decCount <= 0)))))
				{
					// 上昇が１件でもあり、下降がない、またはその逆

					ascIds.add(id);
				}
			}
		}

		int [] ascIds2 = new int [ascIds.size()];
		for(int i=0;i<ascIds.size() ; i++)
		{
			ascIds2[i] = ascIds.get(i);
		}
		return ascIds2;
	}

	/**
	 * PC使用状況の統計情報を取得
	 * @return PC使用状況の統計情報
	 * @throws ParseException 
	 */
	public UsageStatistics getUsageStatistics()
		throws ParseException
	{
		UsageStatistics statistics = new UsageStatistics(); 
		DateTime pdatetime = null;
		int continueHour = 0;
		for (SmartData data : this)
		{
			DateTime datetime = DateTime.parseDateTimeString(data.getDateTime());
			statistics.countByHour.put(datetime.getHour(), statistics.countByHour.get(datetime.getHour()) + 1);
			statistics.countByDayofweek.put(datetime.getDayOfWeek(), statistics.countByDayofweek.get(datetime.getDayOfWeek()) + 1);
			if (pdatetime != null)
			{
				int second = datetime.diff(pdatetime).getTotalSecond();
				if ((second >= 3600) && ((second % 3600) < 10))
				{
					// 連続稼働しているとみなす

					continueHour += second / 3600;
				}
				else
				{
					// 連続稼働しているとみなさない

					statistics.countByContinuousRunning.put(continueHour, statistics.countByContinuousRunning.get(continueHour) + 1);
					continueHour = 0;
				}
			}
			pdatetime = datetime;
		}
		return statistics;
	}
}
