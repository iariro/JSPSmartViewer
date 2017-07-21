package kumagai.smartviewer.struts2;

/**
 * 閲覧対象のパス情報
 */
public class ViewTarget
{
	public final String path;
	public final String type;
	public final String name;

	/**
	 * 指定の値をフィールドに割り当てる
	 * @param path パス文字列
	 * @param type binary/smartctl
	 * @param name 名前
	 */
	public ViewTarget(String path, String type, String name)
	{
		this.path = path;
		this.type = type;
		this.name = name;
	}
}
