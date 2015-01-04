package sk.drakkar.oar;

import java.util.HashMap;
import java.util.Map;

public class IssueTemplater extends AbstractTemplater {
	public String convert(Issue issue)  {
		Map<String, Object> model = new HashMap<>();
		model.put("issue", issue);
		
		return resolveTemplate("issue-index.html", model);
	}

}