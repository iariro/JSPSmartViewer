package kumagai.smartviewer;

/**
 * SMARTエラーログ
 */
public class SmartErrorLog
{
	private final byte [] data;

	/**
	 * 指定の値をメンバーに割り当て
	 * @param data エラーログを含むバイナリ
	 */
	public SmartErrorLog(byte [] data, int offset, int size)
	{
		this.data = new byte [size];
		System.arraycopy(data, offset, this.data, 0, size);
	}

	/**
	 * エラーカウントを取得
	 * @return エラーカウント
	 */
	public int getErrorCount()
	{
		return data[452] + (data[453] << 8);
	}
}
