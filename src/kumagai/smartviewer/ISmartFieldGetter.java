package kumagai.smartviewer;

/**
 * SMART属性値取得オブジェクトI/F
 */
public interface ISmartFieldGetter
{
	long get(SmartAttribute attribute);

	/**
	 * フィールド取得オブジェクト取得
	 * @param field フィールド名
	 * @return 取得オブジェクト
	 */
	static public ISmartFieldGetter getSmartFieldGetter(String field)
	{
		ISmartFieldGetter smartFieldGetter;
		if (field == null || field.equals("raw"))
		{
			// raw指定の場合

			smartFieldGetter = smartAttributeRawValue;
		}
		else if (field == null || field.equals("raw12"))
		{
			// raw12指定の場合

			smartFieldGetter = smartAttributeRawValue12;
		}
		else if (field == null || field.equals("raw5"))
		{
			// raw5指定の場合

			smartFieldGetter = smartAttributeRawValue5;
		}
		else if (field == null || field.equals("raw56"))
		{
			// raw56指定の場合

			smartFieldGetter = smartAttributeRawValue56;
		}
		else if (field == null || field.equals("raw567"))
		{
			// raw567指定の場合

			smartFieldGetter = smartAttributeRawValue567;
		}
		else // if (field == null || field.equals("current"))
		{
			// current指定、またはフィールド指定なしの場合

			smartFieldGetter = smartAttributeCurrent;
		}
		return smartFieldGetter;
	}

	static public final ISmartFieldGetter smartAttributeCurrent =
		new ISmartFieldGetter.SmartAttributeCurrentGetter();
	static public final ISmartFieldGetter smartAttributeRawValue =
		new ISmartFieldGetter.SmartAttributeRawValueGet();
	static public final ISmartFieldGetter smartAttributeRawValue12 =
		new ISmartFieldGetter.SmartAttributeRawValue12Getter();
	static public final ISmartFieldGetter smartAttributeRawValue5 =
		new ISmartFieldGetter.SmartAttributeRawValue5Getter();
	static public final ISmartFieldGetter smartAttributeRawValue56 =
		new ISmartFieldGetter.SmartAttributeRawValue56Getter();
	static public final ISmartFieldGetter smartAttributeRawValue567 =
		new ISmartFieldGetter.SmartAttributeRawValue567Getter();
	
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
		public long get(SmartAttribute attribute)
		{
			return attribute.getCurrent();
		}
	}

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
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue();
		}
	}

	/**
	 * RAW値２バイト分を取得
	 */
	public class SmartAttributeRawValue12Getter
		implements ISmartFieldGetter
	{
		/**
		 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
		 */
		@Override
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue12();
		}
	}

	/**
	 * RAW値5バイト分を取得
	 */
	public class SmartAttributeRawValue5Getter
		implements ISmartFieldGetter
	{
		/**
		 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
		 */
		@Override
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue5();
		}
	}

	/**
	 * RAW値6バイト分を取得
	 */
	public class SmartAttributeRawValue6Getter
		implements ISmartFieldGetter
	{
		/**
		 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
		 */
		@Override
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue6();
		}
	}

	/**
	 * RAW値7バイト分を取得
	 */
	public class SmartAttributeRawValue7Getter
		implements ISmartFieldGetter
	{
		/**
		 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
		 */
		@Override
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue7();
		}
	}

	/**
	 * RAW値5-6バイト分を取得
	 */
	public class SmartAttributeRawValue56Getter
		implements ISmartFieldGetter
	{
		/**
		 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
		 */
		@Override
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue56();
		}
	}

	/**
	 * RAW値5-7バイト分を取得
	 */
	public class SmartAttributeRawValue567Getter
		implements ISmartFieldGetter
	{
		/**
		 * @see kumagai.smartviewer.ISmartFieldGetter#get(kumagai.smartviewer.SmartAttribute)
		 */
		@Override
		public long get(SmartAttribute attribute)
		{
			return attribute.getRawValue567();
		}
	}
}
