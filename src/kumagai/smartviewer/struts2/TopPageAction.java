package kumagai.smartviewer.struts2;

import java.util.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;

/**
 * トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/index.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class TopPageAction
{
	public ArrayList<ViewTarget> targets;

	/**
	 * トップページ表示アクション。
	 * @return 処理結果
	 */
	@Action("index")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		targets = new ViewTargetList(context);

		return "success";
	}
}
