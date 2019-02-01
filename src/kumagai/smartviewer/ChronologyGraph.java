package kumagai.smartviewer;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

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
	static public final Dimension screen = new Dimension(1000, 580);

	/**
	 * エントリポイント
	 * @param args args[0]=ファイルがあるディレクトリのパス args[1]=specifyid/ascending args[2]=IDs(csv) args[3]=current/raw/raw2 args[4]=SVG/HighCharts/DriveSizeHighCharts args[5]=jsp-path args[6]=outfilepath
	 */
	public static void main(String[] args)
		throws IOException, ParseException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException
	{
		if (args.length < 7)
		{
			// 引数が不足

			System.out.println("Usage: filesDirPath specifyid/ascending IDs(csv) current/raw/raw2 SVG/HighCharts/DriveSizeHighCharts jsp-path outfilepath");
			return;
		}

		String filepath = args[0];
		String mode = args[1];
		String [] idsString = args[2].split(",");
		String field = args[3];
		String graphType = args[4];
		String jspPath = args[5];
		String outputPath = args[6];

		SmartDataList points = SmartDataList.getSmartDataFiles(filepath);

		int [] ids = null;
		if (mode.equals("specifyid"))
		{
			// ID指定モード

			ids = new int [idsString.length];
			for (int i=0;i<idsString.length ; i++)
			{
				ids[i] = Integer.valueOf(idsString[i]);
			}
		}
		else if (mode.equals("ascending") || mode.equals("descending"))
		{
			// 上昇・下降する属性絞り込みモード

			ids = points.getAscOrDescAttributeIds(mode);
			field = "raw";
		}

		if (points.size() <= 0)
		{
			System.out.println("data not found");
			return;
		}

		ISmartFieldGetter smartFieldGetter =
				ISmartFieldGetter.getSmartFieldGetter(field);

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
				createHighChartsPoints(ids, points, new ISmartFieldGetter[]{ smartFieldGetter}, (mode.equals("ascending") || mode.equals("descending")));

			// HTML出力
			String replaceTargetName = "<s:property value='targetName' />";
			String replaceTargetPoints = "<s:property value='chartPointLists' />";
			String replaceTargetIf = "<s:if test='%{mode.equals(\"ascending\") && scaling != null}'>yAxis: {max: 1000},</s:if>";
			int replaceFlag = 0;

			BufferedReader reader = new BufferedReader(new FileReader(jspPath));
			PrintWriter writer = new PrintWriter(new FileWriter(outputPath));
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
					else if (line.indexOf(replaceTargetIf) >= 0)
					{
						line = new String();
					}

					writer.println(line);
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
		else if (graphType.equals("DriveSizeHighCharts"))
		{
			// HighCharts

			ArrayList<String> partition = new ArrayList<>();
			partition.add("C");
			LinkedHashMap<String,String> chartPointLists2 = createDriveSizeHighChartsPoints(points, partition, false);
			String chartPointLists = null;
			for (Entry<String, String> element : chartPointLists2.entrySet())
			{
				chartPointLists = element.getValue();
				break;
			}

			// HTML出力
			String replaceTargetName = "<s:property value='targetName' />";
			String replaceTargetPoints = "<s:property value='chartPointLists' />";
			String replaceTargetIf = "<s:if test='%{mode.equals(\"ascending\") && scaling != null}'>yAxis: {max: 1000},</s:if>";
			int replaceFlag = 0;

			BufferedReader reader = new BufferedReader(new FileReader(jspPath));
			PrintWriter writer = new PrintWriter(new FileWriter(outputPath));
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
						line = line.replace(replaceTargetPoints, chartPointLists);
						replaceFlag |= 2;
					}
					else if (line.indexOf(replaceTargetIf) >= 0)
					{
						line = new String();
					}

					writer.println(line);
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
	 * @param ids 対象の属性ID
	 * @param smartDataList SMARTデータのリスト
	 * @param smartFieldGetters 属性のフィールド取得オブジェクト
	 * @param scaling true=スケーリングする／false=しない
	 * @return Highcharts用座標データ文字列
	 */
	static public StringBuffer createHighChartsPoints
		(int[] ids, SmartDataList smartDataList, ISmartFieldGetter [] smartFieldGetters, boolean scaling)
	{
		StringBuffer chartPointLists = new StringBuffer();

		for (int id : ids)
		{
			for (int fieldIndex=0 ; fieldIndex<smartFieldGetters.length ; fieldIndex++)
			{
				Long max = null;
				if (scaling)
				{
					// スケーリングする

					for (SmartData smartData : smartDataList)
					{
						for (SmartAttribute attribute : smartData.attributes)
						{
							if (attribute.getId() == id)
							{
								// 対象のIDの属性である

								long value = smartFieldGetters[fieldIndex].get(attribute);
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

				String name;
				if (fieldIndex <= 0)
				{
					name = String.format("#%d %s %s", id, SmartAttributeTable.getName(id), smartFieldGetters[fieldIndex].getFieldName());
				}
				else
				{
					name = smartFieldGetters[fieldIndex].getFieldName();
				}

				ChartPointList chartPointList = new ChartPointList(name);

				for (SmartData smartData : smartDataList)
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
									max > 0 ? smartFieldGetters[fieldIndex].get(attribute) * 1000 / max : smartFieldGetters[fieldIndex].get(attribute));
							}
							else
							{
								// スケーリングしない

								chartPointList.put(
									smartData.getDateTime(),
									smartFieldGetters[fieldIndex].get(attribute));
							}
							break;
						}
					}
				}

				chartPointLists.append(chartPointList.toString());
			}
		}
		return chartPointLists;
	}

	/**
	 * Highcharts用にドライブサイズの座標データ文字列を生成
	 * @param smartDataList SMARTデータのリスト
	 * @param partitions パーティション名
	 * @param unix true=Unixであり/devのみの絞り込みを行う／false=行わない
	 * @return Highcharts用座標データ文字列
	 */
	static public LinkedHashMap<String, String> createDriveSizeHighChartsPoints
		(SmartDataList smartDataList, ArrayList<String> partitions, boolean unix)
	{
		LinkedHashMap<String, String> pointsList = new LinkedHashMap<String, String>();

		for (String partition : partitions)
		{
			if (unix && (partition.indexOf("/dev") < 0))
			{
				// Unixであるが/devを含まない

				continue;
			}

			StringBuffer chartPointLists = new StringBuffer();

			ChartPointList chartPointList = new ChartPointList("Used size");
			for (SmartData smartData : smartDataList)
			{
				if (smartData.driveSizeArray != null)
				{
					// ドライブサイズ情報あり

					for (DriveSize driveSize : smartData.driveSizeArray)
					{
						if (driveSize.partition.equals(partition))
						{
							// 対象のドライブ

							long usedSize = driveSize.totalSize - driveSize.freeSize;
							chartPointList.put(smartData.getDateTime(), usedSize);
							break;
						}
					}
				}
			}
			chartPointLists.append(chartPointList.toString());

			chartPointLists.append(",");

			chartPointList = new ChartPointList("Total size");
			for (SmartData smartData : smartDataList)
			{
				if (smartData.driveSizeArray != null)
				{
					// ドライブサイズ情報あり

					for (DriveSize driveSize : smartData.driveSizeArray)
					{
						if (driveSize.partition.equals(partition))
						{
							// 対象のドライブ

							chartPointList.put(smartData.getDateTime(), driveSize.totalSize);
							break;
						}
					}
				}
			}

			chartPointLists.append(chartPointList.toString());
			pointsList.put(partition, chartPointLists.toString());
		}

		return pointsList;
	}
}
