package kumagai.smartviewer;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import ktool.datetime.DateTime;

/**
 * 月ごとの増分グラフ用データ
 */
public class MonthlyAscendList
	extends LinkedHashMap<String, Long>
{
	private final int id;

	/**
	 * 月ごとの増分グラフ用データを構築
	 * @param id 対象の属性ID
	 * @param smartDataList SMARTデータのリスト
	 * @param smartFieldGetter 属性のフィールド取得オブジェクト
	 */
	public MonthlyAscendList(int id, SmartDataList smartDataList, ISmartFieldGetter smartFieldGetter)
		throws ParseException
	{
		this.id = id;

		DateTime firstTime = DateTime.parseDateTimeString(smartDataList.get(0).getDateTime());
		DateTime firstMonth = new DateTime(firstTime.getYear(), firstTime.getMonth(), 1, 0, 0, 0);
		DateTime now = new DateTime();

		for (DateTime month = new DateTime(firstTime.getYear(), 1, 1, 0, 0, 0) ; month.compareTo(now) <= 0 ; month.addMonth(1))
		{
			put(String.format("%d/%02d", month.getYear(), month.getMonth()), 0L);
		}
	
		for (SmartData smartData : smartDataList)
		{
			for (SmartAttribute attribute : smartData.attributes)
			{
				if (attribute.getId() == id)
				{
					// 対象のIDの属性である
	
					String yearMonth = smartData.getDateTime().substring(0, 4 + 1 + 2);
					if (get(yearMonth) < smartFieldGetter.get(attribute))
					{
						put(yearMonth, smartFieldGetter.get(attribute));
					}
					break;
				}
			}
		}
	
		for (DateTime month = new DateTime(now.getYear(), now.getMonth(), 1, 0, 0, 0) ; month.compareTo(firstMonth) >= 0 ; month.addMonth(-1))
		{
			String yearMonth = String.format("%d/%02d", month.getYear(), month.getMonth());
			DateTime month2 = new DateTime(month);
			month2.addMonth(-1);
			String yearMonth2 = String.format("%d/%02d", month2.getYear(), month2.getMonth());
	
			if (month.getYear() == firstMonth.getYear() &&
				month.getMonth() == firstMonth.getMonth())
			{
				put(yearMonth, 0L);
			}
			else
			{
				if (get(yearMonth) >= get(yearMonth2))
				{
					put(yearMonth, get(yearMonth) - get(yearMonth2));
				}
			}
		}
	}

	/**
	 * X軸となる月名の配列文字列を生成
	 * @return X軸となる月名の配列文字列
	 */
	public String createCategoriesString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		int count = 0;
		for (Map.Entry<String, Long> entry : entrySet())
		{
			if (count > 0)
			{
				// ２個目以降

				buffer.append(",");
			}

			buffer.append(String.format("'%s'", entry.getKey()));
			count++;
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * 月々の増分値の配列文字列を生成
	 * @return 月々の増分値の配列文字列
	 */
	public String createSeriesString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("[{name: 'SMART#%d - %s', data: [", id, SmartAttributeTable.getName(id)));
		int count = 0;
		for (Map.Entry<String, Long> entry : entrySet())
		{
			if (count > 0)
			{
				// ２個目以降

				buffer.append(",");
			}

			buffer.append(entry.getValue());
			count++;
		}

		buffer.append("]}]");
		return buffer.toString();
	}
}
