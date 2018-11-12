package kumagai.smartviewer.struts2;

import java.util.ArrayList;

import javax.servlet.ServletContext;

/**
 * SmartFilePathXの設定内容から構築可能なViewTargetのコレクション
 */
public class ViewTargetList
	extends ArrayList<ViewTarget>
{
	/**
	 * SmartFilePathXの設定内容からViewTargetのコレクションを構築
	 */
	public ViewTargetList(ServletContext context)
	{
		for (int i=1; ; i++)
		{
			String smartFilePath = context.getInitParameter("SmartFilePath" + i);
			if (smartFilePath != null)
			{
				// パラメータは存在する

				String [] fields = smartFilePath.split(",");
				String path = null;
				String type = null;
				String name = null;
				String pc = null;
				for (int j=0 ; j<fields.length ; j++)
				{
					if (fields[j].indexOf("path=") == 0)
					{
						path = fields[j].substring(5);
					}
					else if (fields[j].indexOf("type=") == 0)
					{
						type = fields[j].substring(5);
					}
					else if (fields[j].indexOf("name=") == 0)
					{
						name = fields[j].substring(5);
					}
					else if (fields[j].indexOf("pc=") == 0)
					{
						pc = fields[j].substring(3);
					}
				}

				if (path != null && type != null && name != null)
				{
					// 項目は足りている

					add(new ViewTarget(path, type, name, pc));
				}
			}
			else
			{
				// パラメータは存在しない

				break;
			}
		}
	}

}
