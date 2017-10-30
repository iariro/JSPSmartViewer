package kumagai.smartviewer;

/**
 * SMART属性値としきい値
 */
public class SmartAttributeAndThreshold
{
	public int id;
	public String attributeName;
	public int current;
	public int worst;
	public long rawValue;
	public String rawValueDump;
	public int threshold;

	/**
	 * 属性値・しきい値の組を構築
	 * @param attribute 属性値
	 * @param threshold しきい値
	 */
	public SmartAttributeAndThreshold(SmartAttribute attribute, SmartThreshold threshold)
	{
		this.id = attribute.getId();
		this.attributeName = attribute.getAttributeName();
		this.current = attribute.getCurrent();
		this.worst = attribute.getWorst();
		this.rawValue = attribute.getRawValue();
		this.rawValueDump = attribute.getRawValueDump();
		this.threshold = threshold.getValue();
	}
}
