package kumagai.smartviewer;

/**
 * 遷移グラフの頂点情報
 */
public class SmartGraphDocumentPoint
{
	final int x;
	final int y;

	/**
	 * 指定の値をフィールドに割り当てる
	 * @param x X軸値
	 * @param y Y軸値
	 */
	public SmartGraphDocumentPoint(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
