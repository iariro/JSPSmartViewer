package kumagai.smartviewer;

import java.util.*;

public class SmartAttributeList
	extends ArrayList<SmartAttribute>
{
	public SmartAttributeList(byte [] data, int offset)
	{
		for (int i=0 ; i<512 ; i+=12)
		{
			SmartAttribute smartAttribute = new SmartAttribute(data, offset + 2 + i);
			if (smartAttribute.getId() == 0)
			{
				break;
			}
			add(smartAttribute);
		}
	}
}
