package kumagai.smartviewer.struts2;

/**
 * １属性分のSMART RAW値ビットカウント情報
 */
public class SmartRawBitCount
{
	public final int id;
	public final String name;
	public final String pointList;

	/**
	 * 指定の値をメンバーに割り当てる
	 * @param id 属性ID
	 * @param name 属性名
	 * @param pointList highcharts用値配列
	 */
	public SmartRawBitCount(int id, String name, String pointList)
	{
		this.id = id;
		this.name = name;
		this.pointList = pointList;
	}

	/**
	 * 表示用属性名を取得
	 * @return 表示用属性名
	 */
	public String getIdAndName()
	{
		return String.format("'SMART#%d %s'", id, name);
	}
}
