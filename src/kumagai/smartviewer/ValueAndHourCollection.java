package kumagai.smartviewer;

import java.util.ArrayList;

import ktool.datetime.TimeSpan;

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
			int useTimeDiff = get(i).hour - get(0).hour;
			TimeSpan realTimeDiff = get(i).datetime.diff(get(0).datetime);
			long valueDiff = get(0).value - get(i).value;
			int remainingHour1 = (int)(useTimeDiff * (get(i).value / valueDiff));
			long remainingSecond2 = realTimeDiff.getTotalSecond() * (get(i).value / valueDiff);

			predictions.add(
				new Prediction(
					get(0).hour,
					get(0).datetime,
					get(0).value,
					get(i).hour,
					get(i).datetime,
					get(i).value,
					remainingHour1,
					remainingSecond2));
		}

		return predictions;
	}
}
