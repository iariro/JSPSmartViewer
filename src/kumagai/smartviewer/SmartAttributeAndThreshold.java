package kumagai.smartviewer;

/**
 * SMART属性値としきい値
 */
public class SmartAttributeAndThreshold
{
	public SmartAttribute attribute;

	/**
	 * 属性IDを取得
	 * @return 属性ID
	 */
	public int getId()
	{
		return attribute.getId();
	}

	/**
	 * 属性名を取得
	 * @return 属性名
	 */
	public String getAttributeName()
	{
		return attribute.getAttributeName();
	}

	/**
	 * カレント値を取得
	 * @return カレント値
	 */
	public int getCurrent()
	{
		return attribute.getCurrent();
	}

	/**
	 * ワースト値を取得
	 * @return ワースト値
	 */
	public int getWorst()
	{
		return attribute.getWorst();
	}

	/**
	 * RAW値を取得
	 * @return RAW値
	 */
	public long getRawValue()
	{
		return attribute.getRawValue();
	}

	/**
	 * RAW値の16進ダンプを取得
	 * @return RAW値の16進ダンプ
	 */
	public String getRawValueDump()
	{
		return attribute.getRawValueDump();
	}

	public Integer threshold;

	/**
	 * 属性値・しきい値の組を構築
	 * @param attribute 属性値
	 * @param threshold しきい値
	 */
	public SmartAttributeAndThreshold(SmartAttribute attribute, SmartThreshold threshold)
	{
		this.attribute = attribute;

		if (threshold != null)
		{
			this.threshold = threshold.getValue();
		}
	}
}
