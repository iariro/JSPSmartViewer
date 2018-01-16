package kumagai.smartviewer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * smartctl -Aの出力から構築可能なSMARTしきい値コレクション
 */
public class SmartThresholdListFromSmartctl
extends ArrayList<SmartThreshold>
{
	static private final Pattern attributeLinePattern =
		Pattern.compile(" *([0-9]*) ([^ ]*)  *0x([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *(.*)");

	/**
	 * SMARTしきい値コレクションを構築
	 * @param lines smartctl -Aの出力
	 */
	public SmartThresholdListFromSmartctl(String[] lines)
	{
		boolean section = false;
		for (int i=0 ; i<lines.length ; i++)
		{
			if (!section)
			{
				// セクションに入っていない

				if (lines[i].equals("=== START OF READ SMART DATA SECTION ==="))
				{
					// セクションに入った

					section = true;
					i += 3;
				}
			}
			else
			{
				// セクションに入った

				Matcher matcher = attributeLinePattern.matcher(lines[i]);

				if (matcher.matches())
				{
					// 属性値を含む文字列

					int id;
					int value;

					id = Integer.valueOf(matcher.group(1));
					value = Integer.valueOf(matcher.group(6));

					byte [] binary = new byte [12];
					binary[ 0] = (byte)id;
					binary[ 1] = (byte)value;

					add(new SmartThreshold(binary, 0));
				}
			}
		}
	}
}
