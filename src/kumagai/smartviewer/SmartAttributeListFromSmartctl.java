package kumagai.smartviewer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * smartctl -Aの出力から構築可能なSMART属性値コレクション
 */
public class SmartAttributeListFromSmartctl
	extends ArrayList<SmartAttribute>
{
	static private final Pattern attributeLinePattern =
		Pattern.compile(" *([0-9]*) ([^ ]*)  *0x([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *([^ ]*)  *(.*)");

	/**
	 * SMART属性値コレクションを構築
	 * @param lines smartctl -Aの出力
	 */
	public SmartAttributeListFromSmartctl(String[] lines)
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
					int flag;
					int value;
					int worst;
					long raw = 0;

					id = Integer.valueOf(matcher.group(1));
					flag = Integer.parseInt(matcher.group(3), 16);
					value = Integer.valueOf(matcher.group(4));
					worst = Integer.valueOf(matcher.group(5));
					try
					{
						raw = Long.valueOf(matcher.group(10));
					}
					catch (NumberFormatException e)
					{
					}

					byte [] binary = new byte [12];
					binary[ 0] = (byte)id;
					binary[ 1] = (byte)(flag & 0xff);
					binary[ 2] = (byte)((flag & 0xff00) >> 8);
					binary[ 3] = (byte)value;
					binary[ 4] = (byte)worst;
					binary[ 5] = (byte)((raw & 0xffL));
					binary[ 6] = (byte)((raw & 0xff00L) >> 8);
					binary[ 7] = (byte)((raw & 0xff0000L) >> 16);
					binary[ 8] = (byte)((raw & 0xff000000L) >> 24);
					binary[ 9] = (byte)((raw & 0xff00000000L) >> 32);
					binary[10] = (byte)((raw & 0xff0000000000L) >> 40);

					add(new SmartAttribute(binary, 0));
				}
			}
		}
	}
}
