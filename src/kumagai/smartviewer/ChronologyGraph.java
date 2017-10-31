package kumagai.smartviewer;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * 時系列グラフ出力
 * @author kumagai
 */
public class ChronologyGraph
{
	static public final ISmartFieldGetter smartAttributeCurrent =
		new SmartAttributeCurrentGetter();
	static public final ISmartFieldGetter smartAttributeRawValue =
		new SmartAttributeRawValueGet();
	static public final ISmartFieldGetter smartAttributeRawValue2 =
		new SmartAttributeRawValue2Getter();
	static public final Dimension screen = new Dimension(1000, 580);

	/**
	 * エントリポイント
	 * @param args args[0]=ファイルがあるディレクトリのパス args[1]=IDs(csv) args[2]=current/raw/raw2 args[3]=SVG/HighCharts args[4]=jsp-path args[5]=outfilepath
	 */
	public static void main(String[] args)
		throws IOException, ParseException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException
	{
		if (args.length < 6)
		{
			// 引数が不足

			System.out.println("Usage: filesDirPath current/raw/raw2 IDs(csv) SVG/HighCharts jsp-path outfilepath");
		}

		String filepath = args[0];
		String [] idsString = args[1].split(",");
		String field = args[2];
		String graphType = args[3];
		String jspPath = args[4];
		String outputPath = args[5];

		int [] ids = new int [idsString.length];
		for (int i=0;i<idsString.length ; i++)
		{
			ids[i] = Integer.valueOf(idsString[i]);
		}

		SmartDataList points = SmartDataList.getSmartDataFiles(filepath);

		if (points.size() <= 0)
		{
			System.out.println("data not found");
			return;
		}

		ISmartFieldGetter smartFieldGetter =
			ChronologyGraph.getSmartFieldGetter(field);

		if (graphType.equals("SVG"))
		{
			// SVG

			ArrayList<SmartGraphDocumentPointList> smartGraphDocumentPointLists =
				new ArrayList<SmartGraphDocumentPointList>();
			for (int id : ids)
			{
				smartGraphDocumentPointLists.add(
					new SmartGraphDocumentPointList
						(points, screen, id, smartFieldGetter));
			}

			SmartGraphDocument document =
				new SmartGraphDocument(smartGraphDocumentPointLists);

			FileOutputStream stream = new FileOutputStream("../out.svg");
			Transformer transformer =
				TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			document.write(transformer, new OutputStreamWriter(stream));
			stream.close();
		}
		else if (graphType.equals("HighCharts"))
		{
			// HighCharts

			StringBuffer chartPointLists =
				createHighChartsPoints(ids, points, smartFieldGetter, false);

			// HTML出力
			String replaceTargetName = "<s:property value='targetName' />";
			String replaceTargetPoints = "<s:property value='chartPointLists' />";
			int replaceFlag = 0;

			BufferedReader reader = new BufferedReader(new FileReader(jspPath));
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
			String line = reader.readLine();
			while ((line = reader.readLine()) != null)
			{
				if (! line.startsWith("<%@"))
				{
					// HTMLとして有効な行

					if (line.indexOf(replaceTargetName) >= 0)
					{
						line = line.replace(replaceTargetName, filepath);
						replaceFlag |= 1;
					}
					else if (line.indexOf(replaceTargetPoints) >= 0)
					{
						line = line.replace(replaceTargetPoints, chartPointLists.toString());
						replaceFlag |= 2;
					}

					writer.write(line);
				}
			}
			reader.close();
			writer.close();
			if (replaceFlag == 3)
			{
				System.out.println("Success");
			}
			else
			{
				System.out.println(String.format("Error %d", replaceFlag));
			}
		}
	}

	/**
	 * Highcharts用に座標データ文字列を生成
	 * @param ids
	 * @param points
	 * @param smartFieldGetter
	 * @param scaling true=スケーリングする／false=しない
	 * @return Highcharts用座標データ文字列
	 */
	static public StringBuffer createHighChartsPoints(int[] ids, SmartDataList points, ISmartFieldGetter smartFieldGetter, boolean scaling)
	{
		StringBuffer chartPointLists = new StringBuffer();

		for (int id : ids)
		{
			Long max = null;
			if (scaling)
			{
				// スケーリングする

				for (SmartData smartData : points)
				{
					for (SmartAttribute attribute : smartData.attributes)
					{
						if (attribute.getId() == id)
						{
							// 対象のIDの属性である
	
							long value = smartFieldGetter.get(attribute);
							if (max == null || max < value)
							{
								// 初回または現状の最大を上回る
	
								max = value;
							}
							break;
						}
					}
				}
			}

			if (chartPointLists.length() > 0)
			{
				// ２個目以降

				chartPointLists.append(",");
			}

			ChartPointList chartPointList =
				new ChartPointList(String.format("#%d %s", id, SmartAttributeTable.getName(id)));

			for (SmartData smartData : points)
			{
				for (SmartAttribute attribute : smartData.attributes)
				{
					if (attribute.getId() == id)
					{
						// 対象のIDの属性である

						if (scaling)
						{
							// スケーリングする

							chartPointList.put(
								smartData.getDateTime(),
								(int)(smartFieldGetter.get(attribute) * 1000 / max));
						}
						else
						{
							// スケーリングしない

							chartPointList.put(
								smartData.getDateTime(),
								(int)smartFieldGetter.get(attribute));
						}
						break;
					}
				}
			}

			chartPointLists.append(chartPointList.toString());
		}
		return chartPointLists;
	}

	/**
	 * フィールド取得オブジェクト取得
	 * @param field フィールド名
	 * @return 取得オブジェクト
	 */
	static public ISmartFieldGetter getSmartFieldGetter(String field)
	{
		ISmartFieldGetter smartFieldGetter;
		if (field == null || field.equals("current"))
		{
			// current指定、またはフィールド指定なしの場合

			smartFieldGetter = smartAttributeCurrent;
		}
		else if (field == null || field.equals("raw"))
		{
			// raw指定の場合

			smartFieldGetter = smartAttributeRawValue;
		}
		else
		{
			// current,raw以外指定の場合

			smartFieldGetter = smartAttributeRawValue2;
		}
		return smartFieldGetter;
	}
}
