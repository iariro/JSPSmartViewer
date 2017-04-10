package kumagai.smartviewer;

/**
 * SMARTデータ一式
 */
public class SmartData
{
	/**
	 * byte配列からint値の取得
	 * @param data byte配列
	 * @param offset 読取開始位置
	 * @return int値
	 */
	static public int getIntFromBytes(byte [] data, int offset)
	{
		return
			(data[offset] & 0xff) +
			((data[offset + 1] & 0xff) << 8) +
			((data[offset + 2] & 0xff) << 16) +
			((data[offset + 3] & 0xff) << 24);
	}

	byte [] datetime;
	public IdentifySector identify;
	public SmartAttributeList attributes;
	int bufferSize;

	/**
	 * SMARTデータ一式を構築
	 * @param data SMARTデータ一式を含むバイナリ
	 * @param offset 読み込み開始位置
	 */
	public SmartData(byte [] data, int offset)
	{
		datetime = new byte [16];
		System.arraycopy(data, offset + 48, datetime, 0, 16);
		int num = getIntFromBytes(data, offset + 64);
		offset += 68;
		bufferSize = 68;
		for (int i=0 ; i<num ; i++)
		{
			int type1 = getIntFromBytes(data, offset);
			int type2 = getIntFromBytes(data, offset + 4);
			int blockSize = getIntFromBytes(data, offset + 8);

			if ((type1 == 10) && ((type2 & 0xffff00) == 0x00ec00))
			{
				// IDENTIFY

				identify = new IdentifySector(data, offset + 12);
			}
			else if ((type1 == 10) && ((type2 & 0xffff00) == 0xd0b000))
			{
				// SMART Value

				attributes = new SmartAttributeList(data, offset + 12);
			}

			offset += 12 + blockSize;
			bufferSize += 12 + blockSize;
		}
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
