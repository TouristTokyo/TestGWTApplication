package com.atollis.client;

import java.util.ArrayList;
import java.util.List;

import com.atollis.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestApplication implements EntryPoint {

	private static final int REFRESH_INTERVAL = 1000;
	private static final int COUNT_TEST = 5;

	private VerticalPanel mainPanel = new VerticalPanel();

	private VerticalPanel checkBoxPanel = new VerticalPanel();
	private CheckBox[] arrCheckBox = new CheckBox[COUNT_TEST];
	private Label labelCheckBox = new Label();

	private HorizontalPanel listBoxPanel = new HorizontalPanel();
	private ListBox dropBox = new ListBox();
	private ListBox multiBox = new ListBox(true);
	private Label labelDropBox = new Label();
	private Label labelMultiBox = new Label();

	private VerticalPanel textBoxPanel = new VerticalPanel();
	private TextBox textBox = new TextBox();
	private Label labelTextBox = new Label();
	private Label labelLastUpdate = new Label();
	private String lastValueForTextBox;
	private String lastUpdateForTextBox;

	private VerticalPanel richTextPanel = new VerticalPanel();
	private RichTextArea area = new RichTextArea();
	private Label labelRichText = new Label();
	private Label labelRichTextLastUdate = new Label();
	private String lastValueForRichTextArea;
	private String lastUpdateForRichTextArea;

	private Tree staticTree = new Tree();
	private ScrollPanel staticTreeWrapper = new ScrollPanel();
	private DecoratorPanel staticDecorator = new DecoratorPanel();
	private Label labelStaticTree = new Label();

	private Grid gridTable = new Grid(COUNT_TEST, 4);

	private Button openButton = new Button("Open");
	private DialogBox dialogBox = new DialogBox();
	private HorizontalPanel dialogContents = new HorizontalPanel();
	private Button answerPositiveButton = new Button("Yes");
	private Button answerNegativeButton = new Button("No");

	@Override
	public void onModuleLoad() {
		mainPanel.add(labelCheckBox);
		mainPanel.add(checkBoxPanel);

		mainPanel.add(new HTML("<hr>"));

		mainPanel.add(labelDropBox);
		mainPanel.add(labelMultiBox);
		mainPanel.add(listBoxPanel);

		mainPanel.add(new HTML("<hr>"));

		mainPanel.add(labelTextBox);
		mainPanel.add(labelLastUpdate);
		mainPanel.add(textBoxPanel);

		mainPanel.add(new HTML("<hr>"));

		mainPanel.add(labelRichText);
		mainPanel.add(labelRichTextLastUdate);
		mainPanel.add(richTextPanel);

		mainPanel.add(new HTML("<hr>"));

		mainPanel.add(labelStaticTree);
		mainPanel.add(staticDecorator);

		mainPanel.add(new HTML("<hr>"));

		mainPanel.add(gridTable);

		mainPanel.add(new HTML("<hr>"));

		mainPanel.add(openButton);

		initCheckboxes();
		initTimer();
		initListBox();
		initTextBox();
		initRichText();
		initTree();
		initGridTable();
		initButton();
		initDialogBox();

		RootPanel.get("stockList").add(mainPanel);
	}

	private void initCheckboxes() {
		for (int i = 0; i < COUNT_TEST; i++) {
			arrCheckBox[i] = new CheckBox("test " + (i + 1));
			checkBoxPanel.add(arrCheckBox[i]);
		}
		refreshCheckBoxes();
	}

	private void initTimer() {
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshCheckBoxes();
				refreshListBoxes();
				refreshChangeTextBox();
				refreshChangeRichText();
				refreshTreeElement();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}

	private void refreshCheckBoxes() {
		List<String> elements = new ArrayList<>();
		for (int i = 0; i < COUNT_TEST; i++) {
			CheckBox currentCheckBox = arrCheckBox[i];
			if (currentCheckBox.getValue()) {
				elements.add(currentCheckBox.getText());
			}
		}
		String textForLabel = "";
		if (elements.size() > 0) {
			for (String name : elements) {
				textForLabel += name + " ";
			}
			textForLabel += "is Selected";
		} else {
			textForLabel += "Nothing selected yet";
		}
		labelCheckBox.setStyleName("labelForElements");
		labelCheckBox.setText(textForLabel);
	}

	private void initListBox() {
		for (int i = 0; i < COUNT_TEST; i++) {
			dropBox.addItem("test category " + (i + 1));
		}
		VerticalPanel dropBoxPanel = new VerticalPanel();
		dropBoxPanel.setSpacing(20);
		dropBoxPanel.setWidth("11em");
		dropBoxPanel.add(dropBox);
		listBoxPanel.add(dropBoxPanel);

		VerticalPanel multiBoxPanel = new VerticalPanel();
		multiBoxPanel.setSpacing(20);
		multiBox.setVisibleItemCount(COUNT_TEST * 2);
		multiBox.setWidth("15em");
		multiBoxPanel.add(multiBox);
		listBoxPanel.add(multiBoxPanel);

		dropBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				showCategory(multiBox, dropBox.getSelectedIndex());
			}
		});

		showCategory(multiBox, 0);
		refreshListBoxes();
	}

	private void showCategory(ListBox listBox, int category) {
		listBox.clear();
		for (int i = 0; i < COUNT_TEST; i++) {
			listBox.addItem("test data " + (i + 1) + ", with " + (category + 1) + " category ");
		}
	}

	private void refreshListBoxes() {
		labelDropBox.setStyleName("labelForElements");
		labelDropBox.setText(dropBox.getSelectedValue() + " is Selected");

		labelMultiBox.setStyleName("labelForElements");
		labelMultiBox.setText("");
		if (multiBox.getSelectedValue() != null) {
			labelMultiBox.setText(multiBox.getSelectedValue() + " is Selected");
		}
	}

	private void initTextBox() {
		textBoxPanel.add(textBox);
		textBox.setText("");
		lastValueForTextBox = "null";
		lastUpdateForTextBox = "null";
		refreshChangeTextBox();
	}

	private void refreshChangeTextBox() {
		String updatedValue = textBox.getText();
		if (updatedValue == "") {
			updatedValue = "null";
		}
		labelTextBox.setStyleName("labelForElements");
		labelLastUpdate.setStyleName("labelForElements");
		if (updatedValue == lastValueForTextBox) {
			labelTextBox.setText("Nothing changed yet");
		} else {
			labelTextBox.setText("Update: " + lastValueForTextBox + " ---> " + updatedValue);
			lastUpdateForTextBox = "Last update: " + lastValueForTextBox + " ---> " + updatedValue;
			lastValueForTextBox = updatedValue;
			labelLastUpdate.setText(lastUpdateForTextBox);
		}
	}

	private void initRichText() {
		richTextPanel.add(area);
		area.setText("");
		lastValueForRichTextArea = "null";
		lastUpdateForRichTextArea = "null";
		refreshChangeRichText();
	}

	private void refreshChangeRichText() {
		String updatedValue = area.getText();
		if (updatedValue == "") {
			updatedValue = "null";
		}
		labelRichText.setStyleName("labelForElements");
		labelRichTextLastUdate.setStyleName("labelForElements");
		if (updatedValue == lastValueForRichTextArea) {
			labelRichText.setText("Nothing changed yet");
		} else {
			labelRichText.setText("Update: " + lastValueForRichTextArea + " ---> " + updatedValue);
			lastUpdateForRichTextArea = "Last update: " + lastValueForRichTextArea + " ---> " + updatedValue;
			lastValueForRichTextArea = updatedValue;
			labelRichTextLastUdate.setText(lastUpdateForRichTextArea);
		}
	}

	private void initTree() {
		createStaticTree();
		staticTree.setAnimationEnabled(true);
		staticTreeWrapper.add(staticTree);
		staticTreeWrapper.setSize("300px", "250px");
		staticDecorator.setWidget(staticTreeWrapper);
		refreshTreeElement();

	}

	private void createStaticTree() {
		TreeItem firstLevelTest1 = staticTree.addTextItem("level 1 (test 1)");
		TreeItem firstLevelTest2 = staticTree.addTextItem("level 1 (test 2)");
		TreeItem firstLevelTest3 = staticTree.addTextItem("level 1 (test 3)");

		addDataForLevel(firstLevelTest1, "level 2 (test 1)");
		addDataForLevel(firstLevelTest1, "level 2 (test 2)");
		addDataForLevel(firstLevelTest1, "level 2 (test 3)");

		addDataForLevel(firstLevelTest2, "level 2 (test 1)");
		addDataForLevel(firstLevelTest2, "level 2 (test 2)");
		addDataForLevel(firstLevelTest2, "level 2 (test 3)");

		addDataForLevel(firstLevelTest3, "level 2 (test 1)");
		addDataForLevel(firstLevelTest3, "level 2 (test 2)");
		addDataForLevel(firstLevelTest3, "level 2 (test 3)");
	}

	private void addDataForLevel(TreeItem parent, String nameForNextLevel) {
		TreeItem section = parent.addTextItem(nameForNextLevel);
		for (int i = 0; i < COUNT_TEST; i++) {
			section.addTextItem("level 3 (test " + (i + 1) + ")");
		}
	}

	private void refreshTreeElement() {
		labelStaticTree.setStyleName("labelForElements");
		if (staticTree.getSelectedItem() != null) {
			labelStaticTree.setText(staticTree.getSelectedItem().getText() + " is Selected");
		} else {
			labelStaticTree.setText("Nothing selected yet");
		}
	}

	private void initGridTable() {
		gridTable.setText(0, 0, "Company");
		gridTable.setText(0, 1, "Field of activity");
		gridTable.setText(0, 2, "Count employees");
		gridTable.setText(0, 3, "Date of formation");

		gridTable.getRowFormatter().setStyleName(0, "tableHeader");

		for (int row = 1; row < gridTable.getRowCount(); row++) {
			for (int col = 0; col < gridTable.getColumnCount(); col++) {
				gridTable.setWidget(row, col, new Label("test data"));
				gridTable.getCellFormatter().setStyleName(row, col, "tableCell");
			}
		}
	}

	private void initButton() {

		openButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.setPopupPosition(Window.getClientWidth()/2, Window.getClientHeight());
				dialogBox.show();

			}
		});

		answerPositiveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				Window.alert("you chose \"YES\" answer");

			}
		});

		answerNegativeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				Window.alert("you chose \"NO\" answer");

			}
		});

	}

	public void initDialogBox() {
		dialogBox.setWidget(dialogContents);
		dialogBox.setSize("100px", "50px");
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		dialogContents.add(answerPositiveButton);
		dialogContents.setSpacing(10);
		dialogContents.add(answerNegativeButton);
	}
}
