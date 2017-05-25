package kumagai.smartviewer;

import java.util.*;

/**
 * 値と時間の対のコレクション
 */
public class ValueAndHourCollection
	extends ArrayList<ValueAndHour>
{
	/**
	 * 各変動ポイントから残り寿命を求める
	 * @return 故障予測情報のコレクション
	 */
	public ArrayList<Prediction> predictFailure()
	{
		int size = size();
		ArrayList<Prediction> predictions = new ArrayList<Prediction>();

		for (int i=1 ; i<size ; i++)
		{
			//int useTimeDiff = get(i).hour - get(0).hour;
			int realTimeDiff = get(i).datetime.getHour() - get(0).datetime.getHour();
			int valueDiff = get(0).value - get(i).value;
			int remainingHour = realTimeDiff * (get(i).value / valueDiff);

			predictions.add(
				new Prediction(
					get(0).hour,
					get(0).value,
					get(i).hour,
					get(i).value,
					remainingHour));
		}

		return predictions;
	}
}
