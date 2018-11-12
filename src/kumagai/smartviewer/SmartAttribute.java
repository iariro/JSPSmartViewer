package kumagai.smartviewer;

/**
 * SMART属性値
 */
public class SmartAttribute
{
	private final byte [] data;

	/**
	 * SMARTバイナリデータから属性値を構築
	 * @param data バイナリデータ
	 * @param offset 読み込み開始位置
	 */
	public SmartAttribute(byte [] data, int offset)
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
	 * 属性IDを１６進形式で取得
	 * @return 属性ID１６進形式
	 */
	public String getIdInHex()
	{
		return String.format("%2X", getId());
	}

	/**
	 * 対応する属性名を取得
	 * @return 属性名
	 */
	public String getAttributeName()
	{
		return SmartAttributeTable.getName(getId());
	}

	/**
	 * 現在値を取得
	 * @return 現在値
	 */
	public int getCurrent()
	{
		return data[3] & 0xff;
	}

	/**
	 * 最悪値を取得
	 * @return 最悪値
	 */
	public int getWorst()
	{
		return data[4] & 0xff;
	}

	/**
	 * RAW値を取得
	 * @return RAW値
	 */
	public long getRawValue()
	{
		return
			((data[10] & 0xffL) << 40) +
			((data[9] & 0xffL) << 32) +
			((data[8] & 0xffL) << 24) +
			((data[7] & 0xffL) << 16) +
			((data[6] & 0xffL) << 8) +
			((data[5] & 0xffL));
	}

	/**
	 * RAW値を取得
	 * @return RAW値
	 */
	public int getRawValue2()
	{
		return ((data[6] & 0xff) << 8) + ((data[5] & 0xff));
	}

	/**
	 * RAW値のダンプを取得
	 * @return RAW値のダンプ
	 */
	public String getRawValueDump()
	{
		String string = new String();

		for (int i=5 ;i<12 ; i++)
		{
			if (i > 5)
			{
				// ２バイト目以降

				string += " ";
			}

			string += String.format("%02X", data[i]);
		}
		return string;
	}

	/**
	 * 指定のbitを取得
	 * @return bit値
	 */
	public boolean getBit(int byteIndex, int bit)
	{
		return (data[byteIndex] & (1 << bit)) > 0;
	}
}
