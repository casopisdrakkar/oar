package sk.drakkar.oar;

import java.util.Comparator;

public class IssueByReversedNumberComparator implements Comparator<Issue> {
	public static final Comparator<Issue> INSTANCE = new IssueByReversedNumberComparator();

	@Override
	public int compare(Issue issue1, Issue issue2) {
		return -Integer.compare(issue1.getNumber(), issue2.getNumber());
	}

}
