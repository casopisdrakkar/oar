package sk.drakkar.oar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Issue {
	private File folder;
	
	private int number;

	private List<Article> articles = new ArrayList<Article>();
	
	private List<String> articleOrder = new ArrayList<String>();
	
	private String pdfFileName;
	
	private Comparator<Article> articleOrderComparator = new ArticleByOrderComparator();
	
	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public List<Article> getArticles() {
		return articles;
	}
	
	public void addArticle(Article article) {
		this.articles.add(article);
		Collections.sort(this.articles, articleOrderComparator);
	}
	
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}
	
	public String getPdfFileName() {
		return pdfFileName;
	}
	
	public List<String> getArticleOrder() {
		return articleOrder;
	}
	
	public void setArticleOrder(List<String> articleOrder) {
		this.articleOrder = articleOrder;
	}
	
	private class ArticleByOrderComparator implements Comparator<Article> {

		@Override
		public int compare(Article article1, Article article2) {
			int index1 = findOrder(article1);
			int index2 = findOrder(article2);
			return Integer.compare(index1, index2);
		}

		private int findOrder(Article article) {
			int index = 0;
			for (String articleName : Issue.this.articleOrder) {
				if(article.getSourceFile().getName().equals(articleName)) {
					return index;
				}
				index++;
			}
			return -1;
		}
		
	}
}
