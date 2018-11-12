package kumagai.smartviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
		String basePath = "C:\\ProgramData\\SMARTLogger\\smart_Jikka_Mouse\\";
		int filenumlimit = 500;

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

		UsageStatistics usageStatistics = smartDataListAll.getUsageStatistics(true, true);
		for (Map.Entry<Integer, Integer> kv : usageStatistics.countByHour.entrySet())
		{
			System.out.printf("%s : %s", kv.getKey(), kv.getValue());
			System.out.println();
		}
		for (Map.Entry<Integer, Integer> kv : usageStatistics.countByDayOfWeek.entrySet())
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
			if (smartData.attributes != null && smartData.attributes.size() > 0)
			{
				// 取得データあり

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
	 * @param interpolateHour 時間ごとカウントを補完
	 * @param includeZeroHour 連続稼働０時間をカウントする
	 * @return PC使用状況の統計情報
	 */
	public UsageStatistics getUsageStatistics(boolean interpolateHour, boolean includeZeroHour)
		throws ParseException
	{
		UsageStatistics statistics = new UsageStatistics();

		// 曜日集計
		DateTime pdatetime = null;
		for (SmartData data : this)
		{
			DateTime datetime = DateTime.parseDateTimeString(data.getDateTime());
			if (pdatetime == null ||
				!(datetime.getYear() == pdatetime.getYear() &&
				datetime.getMonth() == pdatetime.getMonth() &&
				datetime.getDay() == pdatetime.getDay()))
			{
				// 初めの日または異なる日

				statistics.countByDayOfWeek.put(datetime.getDayOfWeek(), statistics.countByDayOfWeek.get(datetime.getDayOfWeek()) + 1);
			}
			pdatetime = datetime;
		}

		// 時間集計
		pdatetime = null;
		for (SmartData data : this)
		{
			DateTime datetime = DateTime.parseDateTimeString(data.getDateTime());
			if (pdatetime != null)
			{
				int diff = datetime.diff(pdatetime).getTotalSecond() + 3;
				if (interpolateHour && (diff >= 3600 * 2) && (diff % 3600 < 6))
				{
					DateTime datetime2 = new DateTime(pdatetime);
					for (int i=1 ; i<diff / 3600 ; i++)
					{
						datetime2.add(3600);
						statistics.countByHour.put(datetime2.getHour(), statistics.countByHour.get(datetime2.getHour()) + 1);
					}
				}
			}

			if ((pdatetime == null) || (datetime.getHour() != pdatetime.getHour()))
			{
				// 初めの日または異なる時間

				statistics.countByHour.put(datetime.getHour(), statistics.countByHour.get(datetime.getHour()) + 1);
			}
			pdatetime = datetime;
		}

		// 連続稼働計算
		LinkedHashMap<Integer, Integer> countByContinuousRunning = new LinkedHashMap<>();
		int continueHour = 0;
		pdatetime = null;
		int max = 0;
		for (SmartData data : this)
		{
			DateTime datetime = DateTime.parseDateTimeString(data.getDateTime());
			if (pdatetime == null)
			{
				// １個目のデータ

				pdatetime = datetime;
			}
			else
			{
				// ２個目以降のデータ

				int diff = datetime.diff(pdatetime).getTotalSecond() + 3;
				if (diff % 3600 < 6)
				{
					// 連続とみなす

					continueHour = diff / 3600;
				}
				else
				{
					// 非連続

					if (!countByContinuousRunning.containsKey(continueHour))
					{
						countByContinuousRunning.put(continueHour, 0);
						if (max < continueHour)
						{
							max = continueHour;
						}
					}
					countByContinuousRunning.put(continueHour, countByContinuousRunning.get(continueHour) + 1);
					continueHour = 0;
					pdatetime = datetime;
				}
			}
		}

		for (int i=0 ; i<=max ; i++)
		{
			if ((i > 0) || includeZeroHour)
			{
				// ０時間より多いかカウントするか＝０時間をカウントしない場合ではない

				if (countByContinuousRunning.containsKey(i))
				{
					// 値は存在する

					statistics.countByContinuousRunning.put(i, countByContinuousRunning.get(i));
				}
				else
				{
					// 値は存在しない

					statistics.countByContinuousRunning.put(i, 0);
				}
			}
		}

		return statistics;
	}

	/**
	 * Raw部分のbit=1の数を集計する。
	 * @return Raw部分のbit=1の数の集計
	 */
	public LinkedHashMap<Integer, int []> getRawBitCount()
	{
		LinkedHashMap<Integer, int []> rawBitCountList = new LinkedHashMap<>();

		for (SmartData smartData : this)
		{
			for (SmartAttribute attribute : smartData.attributes)
			{
				if (!rawBitCountList.containsKey(attribute.getId()))
				{
					// 初出

					rawBitCountList.put(attribute.getId(), new int [8 * 7]);
				}

				for (int i=0 ; i<7 ; i++)
				{
					for (int j=0 ; j<8 ; j++)
					{
						if (attribute.getBit(5 + i, j))
						{
							// bit=1

							rawBitCountList.get(attribute.getId())[i * 8 + j]++;
						}
					}
				}
			}
		}

		return rawBitCountList;
	}
}
