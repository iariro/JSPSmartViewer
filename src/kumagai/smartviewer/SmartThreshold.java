package kumagai.smartviewer;

/**
 * SMARTしきい値
 */
public class SmartThreshold
{
	private final byte [] data;

	/**
	 * SMARTバイナリデータからしきい値を構築
	 * @param data バイナリデータ
	 * @param offset 読み込み開始位置
	 */
	public SmartThreshold(byte [] data, int offset)
	{
		this.data = new byte [12];
		System.arraycopy(data, offset, this.data, 0, 12);
	}

	/**
	 * 属性IDを取得
	 * @return 属性ID
	 */
	public int getId()
	{
		return data[0] & 0xff;
	}

	/**
	 * しきい値を取得
	 * @return 現在値
	 */
	public int getValue()
	{
		return data[1] & 0xff;
	}
}
