package kumagai.smartviewer;

/**
 * RAW値２バイト分を取得
 */
public class SmartAttributeRawValue2Getter
	implements ISmartFieldGetter
{
	/**
	 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
	 */
	@Override
	public int get(SmartAttribute attribute)
	{
		return attribute.getRawValue2();
	}
}
