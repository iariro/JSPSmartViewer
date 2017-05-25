package kumagai.smartviewer;

import ktool.datetime.*;

/**
 * 故障予測情報
 */
public class Prediction
{
	public int time1;
	public int value1;
	public int time2;
	public int value2;
	public int remainingHour;

	/**
	 * 予測故障日を算出
	 * @return 予測故障日
	 */
	public DateTime getDeadDate()
	{
		DateTime now = new DateTime();
		now.add(3600 * remainingHour);
		return now;
	}

	/**
	 * 指定の値をメンバーに割り当て
	 * @param time1 基準時間
	 * @param value1 基準値
	 * @param time2 現在時間
	 * @param value2 現在値
	 * @param remainingHour 残り時間
	 */
	public Prediction(int time1, int value1, int time2, int value2, int remainingHour)
	{
		this.time1 = time1;
		this.value1 = value1;
		this.time2 = time2;
		this.value2 = value2;
		this.remainingHour = remainingHour;
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
				remainingHour);
	}
}
