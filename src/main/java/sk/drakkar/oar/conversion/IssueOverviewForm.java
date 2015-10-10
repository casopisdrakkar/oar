package sk.drakkar.oar.conversion;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
	
	private File issueFile;

	public IssueOverviewForm() {
		setTitle("Drakkar");
		
		initializeTopPanel();
		enableDropSupport();
		
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
		summaryExporter.writeIssueMetadata(summaries, this.issueFile, targetFolder);
		
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

	private void enableDropSupport() {
		DropTargetListener listener = new DropTargetAdapter() {
			@Override
			public void dragOver(DropTargetDragEvent event) {
				if (!event.getTransferable().isDataFlavorSupported(
						DataFlavor.javaFileListFlavor)) {
					event.rejectDrag();
				}
			}

			@Override
			public void drop(DropTargetDropEvent event) {
				Transferable transferable = event.getTransferable();

				try {
					DataFlavor dataFlavor = DataFlavor.javaFileListFlavor;
					// see
					// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6759788
					event.acceptDrop(event.getDropAction());
					List<File> files = (List<File>) transferable
							.getTransferData(dataFlavor);
					File droppedFile = files.get(0);

					handleDroppedFile(droppedFile);

					event.dropComplete(true);
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Component currentWindow = this;
		DropTarget dropTarget = new DropTarget(currentWindow, listener);
		currentWindow.setDropTarget(dropTarget);
	}

	
	protected void handleDroppedFile(File droppedFile) {
		try {
			this.issueFile = droppedFile;
			this.targetFolderTextField.setText(this.issueFile.getParentFile().getParent());
			this.issueNumberTextField.setText(parseIssueNumber(this.issueFile));
		} catch (ParseException e) {
			JOptionPane.showConfirmDialog(this, "Zlý názov súboru s PDF", "Chyba", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String parseIssueNumber(File issueFile) throws ParseException {
		//drakkar_2012_35_prosinec.pdf
		String fileName = issueFile.getName();
		String[] components = fileName.split("_");
		if(components.length < 3) {
			throw new ParseException("Illegal filename", 0);
		}
		
		return components[2];
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
