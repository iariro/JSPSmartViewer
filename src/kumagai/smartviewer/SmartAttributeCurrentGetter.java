package kumagai.smartviewer;

/**
 * 属性値の現在値取得オブジェクト
 */
public class SmartAttributeCurrentGetter
	implements ISmartFieldGetter
{
	/**
	 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
	 */
	@Override
	public int get(SmartAttribute attribute)
	{
		return attribute.getCurrent();
	}
}
