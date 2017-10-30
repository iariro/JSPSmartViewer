package kumagai.smartviewer;

import java.util.ArrayList;

/**
 * バイナリから構築可能なSMARTしきい値コレクション
 */
public class SmartThresholdListFromBinary
	extends ArrayList<SmartThreshold>
{
	/**
	 * SMARTしきい値を含むログファイルバイナリからしきい値コレクションを構築
	 * @param data SMARTしきい値を含むログファイルバイナリ
	 * @param offset 読み取り開始位置
	 */
	public SmartThresholdListFromBinary(byte [] data, int offset)
	{
		for (int i=0 ; i<512 ; i+=12)
		{
			SmartThreshold threshold = new SmartThreshold(data, offset + 2 + i);
	
			if (threshold.getId() == 0)
			{
				// 属性0
	
				// 終端とみなす
				break;
			}
	
			add(threshold);
		}
	}
}
