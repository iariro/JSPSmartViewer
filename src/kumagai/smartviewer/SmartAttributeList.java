package kumagai.smartviewer;

import java.util.*;

/**
 * SMART属性値コレクション
 */
public class SmartAttributeList
	extends ArrayList<SmartAttribute>
{
	/**
	 * SMART属性値を含むログファイルバイナリから属性値コレクションを構築
	 * @param data SMART属性値を含むログファイルバイナリ
	 * @param offset 読み取り開始位置
	 */
	public SmartAttributeList(byte [] data, int offset)
	{
		for (int i=0 ; i<512 ; i+=12)
		{
			SmartAttribute smartAttribute = new SmartAttribute(data, offset + 2 + i);

			if (smartAttribute.getId() == 0)
			{
				// 属性0

				// 終端とみなす
				break;
			}

			add(smartAttribute);
		}
	}
}
