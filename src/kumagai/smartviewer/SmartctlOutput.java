package kumagai.smartviewer;

import java.util.ArrayList;

/**
 * smartctl -Aの出力
 */
public class SmartctlOutput
{
	private final String[] lines;

	/**
	 * 指定の値をメンバーに割り当て
	 * @param lines 文字列配列形式のコマンド出力
	 */
	public SmartctlOutput(String[] lines)
	{
		this.lines = lines;
	}

	/**
	 * 文字列から属性コレクションを生成
	 * @return 属性コレクション
	 */
	public ArrayList<SmartAttribute> getSmartAttributeList()
	{
		return new SmartAttributeListFromSmartctl(lines);
	}
}
