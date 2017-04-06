package kumagai.smartviewer;

import java.awt.*;
import java.text.*;
import java.util.*;
import ktool.datetime.*;

/**
 * 遷移グラフの線１本分の頂点情報のコレクション
 */
public class SmartGraphDocumentPointList
	extends ArrayList<SmartGraphDocumentPoint>
{
	int attributeId;
	float scaleX;
	float scaleY;
	int hourRange;
	int valueRange;
	int maxY;
	int minY;
	DateTime maxHour;
	DateTime minHour;

	/**
	 * SMART情報とスケール情報を受けて、属性値の遷移グラフ頂点情報コレクションを構築する
	 * @param smartDataList SMART情報
	 * @param screen グラフサイズ
	 * @param attributeId 対象の属性ID
	 * @param fieldGetter 属性のフィールド取得オブジェクト
	 * @throws ParseException
	 */
	public SmartGraphDocumentPointList(ArrayList<SmartData> smartDataList,
		Dimension screen, int attributeId, ISmartFieldGetter fieldGetter)
		throws ParseException
	{
		this.attributeId = attributeId;

		// 日時の幅を求める
		for (SmartData smartData : smartDataList)
		{
			DateTime datetime =
				DateTime.parseDateTimeString(smartData.getDateTime());

			if (maxHour == null || maxHour.compareTo(datetime) < 0)
			{
				// maxを更新すべき時

				maxHour = datetime;
			}

			if (minHour == null || minHour.compareTo(datetime) > 0)
			{
				// minを更新すべき時

				minHour = datetime;
			}

			for (SmartAttribute attribute : smartData.attributes)
			{
				if (attribute.getId() == attributeId)
				{
					// 対象のIDの属性である

					int value = fieldGetter.get(attribute);
					if ((maxY == 0) || (maxY < value))
					{
						// maxを更新すべき時

						maxY = value;
					}

					if ((minY == 0) || (minY > value))
					{
						// minを更新すべき時

						minY = value;
					}
					break;
				}
			}
		}

		hourRange = maxHour.diff(minHour).getHour();

		if (maxY <= 255 && minY <= 255)
		{
			// Current値用

			minY = 0;
			maxY = 255;
		}
		else
		{
			// RAW値用

			// 幅を持たせる
			minY -= minY % 10;
			maxY += maxY % 10;
		}

		valueRange = maxY - minY;

		if (hourRange > 0)
		{
			// 更新あり

			scaleX = (float)screen.width / hourRange;
			scaleY = (float)screen.height / valueRange;
		}
		else
		{
			// 更新なし

			scaleX = (float)screen.width;
			scaleY = (float)screen.height / valueRange;

			// １件あることにする
			hourRange = 1;
		}

		for (SmartData smartData : smartDataList)
		{
			DateTime datetime =
				DateTime.parseDateTimeString(smartData.getDateTime());

			for (SmartAttribute attribute : smartData.attributes)
			{
				if (attribute.getId() == attributeId)
				{
					// 対象のIDの属性である

					int diffHour = datetime.diff(minHour).getHour();

					add(
						new SmartGraphDocumentPoint(
							(int)(scaleX * diffHour),
							(int)(scaleY * (maxY - fieldGetter.get(attribute)))));
					break;
				}
			}
		}
	}
}
