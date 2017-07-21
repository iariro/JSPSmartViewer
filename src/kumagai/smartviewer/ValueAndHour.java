package kumagai.smartviewer;

import ktool.datetime.DateTime;

/**
 * 値と時間の対
 */
public class ValueAndHour
{
	public int hour;
	public long value;
	public DateTime datetime;

	/**
	 * 指定の値をメンバーに割り当てる
	 * @param hour 時間
	 * @param value 値
	 */
	public ValueAndHour(int hour, long value, DateTime datetime)
	{
		this.hour = hour;
		this.value = value;
		this.datetime = datetime;
	}
}
