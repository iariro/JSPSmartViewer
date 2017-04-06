package kumagai.smartviewer;

/**
 * SMARTデータ一式
 */
public class SmartData
{
	byte [] datetime;
	public SmartAttributeList attributes;

	/**
	 * SMARTデータ一式を構築
	 * @param data SMARTデータ一式を含むバイナリ
	 * @param offset 読み込み開始位置
	 */
	public SmartData(byte [] data, int offset)
	{
		datetime = new byte [16];
		System.arraycopy(data, offset, datetime, 0, 16);
		attributes = new SmartAttributeList(data, offset + 16 + 512);
	}

	/**
	 * 取得日時を取得
	 * @return 取得日時
	 */
	public String getDateTime()
	{
		String plane = new String(datetime, 0, 14);

		return
			String.format(
				"%s/%s/%s %s:%s:%s",
				plane.substring(0, 4),
				plane.substring(4, 6),
				plane.substring(6, 8),
				plane.substring(8, 10),
				plane.substring(10, 12),
				plane.substring(12, 14));
	}
}
