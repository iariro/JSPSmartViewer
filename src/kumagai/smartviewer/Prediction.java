package kumagai.smartviewer;

import ktool.datetime.*;

/**
 * 故障予測情報
 */
public class Prediction
{
	public int time1;
	public DateTime datetime1;
	public int value1;
	public int time2;
	public DateTime datetime2;
	public int value2;
	public int remainingHour1;
	public int remainingSecond2;

	/**
	 * 予測故障日を算出
	 * @return 予測故障日
	 */
	public DateTime getDeadDate1()
	{
		DateTime now = new DateTime();
		now.add(3600 * remainingHour1);
		return now;
	}

	/**
	 * 予測故障日を算出
	 * @return 予測故障日
	 */
	public DateTime getDeadDate2()
	{
		DateTime now = new DateTime();
		now.add(remainingSecond2);
		return now;
	}

	/**
	 * 予測寿命を返却
	 * @return 予測寿命
	 */
	public int getRemainingHour2()
	{
		return remainingSecond2 / 3600;
	}

	/**
	 * 指定の値をメンバーに割り当て
	 * @param time1 基準稼働時間
	 * @param datetime1 基準日時
	 * @param value1 基準値
	 * @param time2 現在稼働時間
	 * @param datetime2 現在日時
	 * @param value2 現在値
	 * @param remainingHour1 残り時間
	 * @param remainingSecond2 残り時間
	 */
	public Prediction(int time1, DateTime datetime1, int value1, int time2, DateTime datetime2, int value2, int remainingHour1, int remainingSecond2)
	{
		this.time1 = time1;
		this.datetime1 = datetime1;
		this.value1 = value1;
		this.time2 = time2;
		this.datetime2 = datetime2;
		this.value2 = value2;
		this.remainingHour1 = remainingHour1;
		this.remainingSecond2 = remainingSecond2;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return
			String.format(
				"%d:%d -> %d:%d = %d:0",
				time1,
				value1,
				time2,
				value2,
				remainingHour1);
	}
}
