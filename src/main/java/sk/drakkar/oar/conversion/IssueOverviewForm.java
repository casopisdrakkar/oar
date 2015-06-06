package sk.drakkar.oar.conversion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class IssueOverviewForm extends JFrame {
	private JLabel targetFolderLabel = new JLabel("Target Folder:");
	
	private JTextField targetFolderTextField = new JTextField("C:\\Temp", 50);
	
	private JTextField issueNumberTextField = new JTextField(10);
	
	private JTextArea metadataTextField;
	
	private JTextArea editorialTextField = new JTextArea(60, 30);
	
	private JButton convertButton;

	private JScrollPane editorialScrollPane;
	
	private SummaryExporter summaryExporter = new SummaryExporter();

	private JScrollPane metadataScrollPane;

	public IssueOverviewForm() {
		setTitle("Drakkar");
		
		initializeTopPanel();
		
		metadataTextField = new JTextArea();
		metadataTextField.setBorder(BorderFactory.createEtchedBorder());
		metadataTextField.setLineWrap(true);
		metadataTextField.setWrapStyleWord(true);
		metadataTextField.setBorder(BorderFactory.createEtchedBorder());
		
		metadataScrollPane = new JScrollPane(metadataTextField);		
		add(metadataScrollPane, BorderLayout.CENTER);
		
		convertButton = new JButton("Convert");
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IssueOverviewForm.this.onConvertButtonActionPerformed(e);
			}
		});
		add(convertButton, BorderLayout.SOUTH);

		editorialTextField.setBorder(BorderFactory.createEtchedBorder());
		editorialTextField.setLineWrap(true);
		editorialTextField.setWrapStyleWord(true);
		
		editorialScrollPane = new JScrollPane(editorialTextField);
		add(editorialScrollPane, BorderLayout.WEST);

		setPreferredSize(new Dimension(800, 600));
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initializeTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.add(targetFolderLabel);
		topPanel.add(targetFolderTextField);
		topPanel.add(issueNumberTextField);
		
		add(topPanel, BorderLayout.NORTH);
	}

	protected void onConvertButtonActionPerformed(ActionEvent e) {
		File targetFolder = new File(targetFolderTextField.getText() + File.separator + issueNumberTextField.getText());
		if(! targetFolder.exists()) {
			targetFolder.mkdirs();
		}
		
		ArticleSummaryPartitioner articleSummaryPartitioner = new ArticleSummaryPartitioner();
		List<Summary> summaries = articleSummaryPartitioner.convert(metadataTextField.getText());
		
		addEditorial(summaries);
		
		summaryExporter.writeSummaries(summaries, targetFolder);
		summaryExporter.writeIssueMetadata(summaries, targetFolder);
		
	}

	private void addEditorial(List<Summary> summaries) {
		Summary summary = new Summary();
		summary.setTitle("Úvodník");
		summary.setAuthors("redakce");
		summary.setTags("úvodník");
		summary.setShortSummary("Úvodní slovo");
		summary.setColor("gray");
		summary.setSummary(this.editorialTextField.getText());

		summaries.add(0, summary);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				IssueOverviewForm issueOverviewForm = new IssueOverviewForm();
				issueOverviewForm.setVisible(true);
			}
		});
	}
}
