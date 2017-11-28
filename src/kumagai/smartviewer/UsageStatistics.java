package kumagai.smartviewer;

import java.util.LinkedHashMap;

/**
 * PC使用状況の統計情報
 */
public class UsageStatistics
{
	// 時間帯
	public LinkedHashMap<Integer, Integer> countByHour = new LinkedHashMap<>();
	// 曜日
	public LinkedHashMap<Integer, Integer> countByDayofweek = new LinkedHashMap<>();
	// 連続稼働時間
	public LinkedHashMap<Integer, Integer> countByContinuousRunning = new LinkedHashMap<>();

	/**
	 * メンバーの初期化
	 */
	public UsageStatistics()
	{
		for (int i=0 ; i<24 ; i++)
		{
			countByHour.put(i, 0);
		}

		for (int i=0 ; i<7 ; i++)
		{
			countByDayofweek.put(i + 1, 0);
		}

		for (int i=0 ; i<30 ; i++)
		{
			countByContinuousRunning.put(i, 0);
		}
	}
}
