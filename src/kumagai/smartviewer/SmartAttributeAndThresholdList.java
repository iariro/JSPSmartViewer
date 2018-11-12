package kumagai.smartviewer;

import java.util.ArrayList;

/**
 * SMART属性値としきい値のコレクション
 */
public class SmartAttributeAndThresholdList
	extends ArrayList<SmartAttributeAndThreshold>
{
	/**
	 * SMART属性値としきい値からコレクションを生成
	 * @param attributes 属性値
	 * @param thresholds しきい値
	 */
	public SmartAttributeAndThresholdList(ArrayList<SmartAttribute> attributes, ArrayList<SmartThreshold> thresholds)
	{
		if (attributes != null && thresholds != null)
		{
			if (attributes.size() == thresholds.size())
			{
				// 数は同じ

				for (int i=0 ; i<attributes.size() ; i++)
				{
					if (attributes.get(i).getId() == thresholds.get(i).getId())
					{
						// 属性は同じ

						add(new SmartAttributeAndThreshold(attributes.get(i), thresholds.get(i)));
					}
				}
			}
		}
	}
}
