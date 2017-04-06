package kumagai.smartviewer;

/**
 * 属性値のRAW値取得オブジェクト
 */
public class SmartAttributeRawValueGet
	implements ISmartFieldGetter
{
	/**
	 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
	 */
	@Override
	public int get(SmartAttribute attribute)
	{
		return attribute.getRawValue();
	}
}
