package sk.drakkar.oar.conversion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class IssueOverviewForm extends JFrame {
	private JTextArea metadataTextField;
	private JButton convertButton;

	public IssueOverviewForm() {
		metadataTextField = new JTextArea();
		add(metadataTextField, BorderLayout.CENTER);
		
		convertButton = new JButton("Convert");
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IssueOverviewForm.this.onConvertButtonActionPerformed(e);
			}
		});
		add(convertButton, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(800, 600));
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	protected void onConvertButtonActionPerformed(ActionEvent e) {
		ArticleSummaryPartitioner articleSummaryPartitioner = new ArticleSummaryPartitioner();
		articleSummaryPartitioner.convert(metadataTextField.getText(), new File("c:\\Temp\\drakkar"));
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
