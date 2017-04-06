package kumagai.smartviewer;

/**
 * SMART IDENTIFYセクター情報
 */
public class IdentifySector
{
	/**
	 * ワードバイトオーダーの文字列化
	 * @param data バイト列
	 * @param offset 読み込み開始位置
	 * @param length バイト数
	 * @return 文字列
	 */
	static public String getWordString(byte [] data, int offset, int length)
	{
		byte [] plane = new byte [length];

		for (int i=0 ; i<length ; i++)
		{
			plane[i] = data[offset + i + (i % 2 == 0 ? 1 : -1)];
		}
		return new String(plane);
	}

	private final byte [] data;

	/**
	 * 読み込みデータからIDENTIFY情報を構築する
	 * @param data 元データ
	 * @param offset 読み込み開始位置
	 */
	public IdentifySector(byte [] data, int offset)
	{
		this.data = new byte [512];
		System.arraycopy(data, offset, this.data, 0, this.data.length);
	}

	/**
	 * シリアル番号を取得
	 * @return シリアル番号
	 */
	public String getSerialNumber()
	{
		return getWordString(data, 20, 20);
	}

	/**
	 * ファームウェアバージョンを取得
	 * @return ファームウェアバージョン
	 */
	public String getFirmwareVersion()
	{
		return getWordString(data, 46, 8);
	}

	/**
	 * モデル名を取得
	 * @return モデル名
	 */
	public String getModelName()
	{
		return getWordString(data, 54, 40);
	}
}
