package kumagai.smartviewer;

import java.util.Map;
import java.util.TreeMap;

/**
 * 折れ線グラフの頂点１点
 */
public class ChartPointList
	extends TreeMap<String, Long>
{
	public final String name;

	/**
	 * 項目名をフィールドに割り当て
	 * @param name 項目名
	 */
	public ChartPointList(String name)
	{
		this.name = name;
	}

	/**
	 * @see java.util.AbstractMap#toString()
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(String.format("{name: '%s',data: [", name));

		int count = 0;
		for (Map.Entry<String, Long> entry : entrySet())
		{
			if (count > 0)
			{
				// ２個目以降

				buffer.append(",");
			}

			buffer.append(String.format("[Date.parse('%s'), %d]", entry.getKey(), entry.getValue()));
			count++;
		}

		buffer.append("]}");

		return buffer.toString();
	}
}
