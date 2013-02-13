package com.anonymbook.client.ui.components;

import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FilterComponent extends Composite {

	@UiField
	Anchor askedByOthers;

	@UiField
	Label askedByOthersLabel;

	@UiField
	Anchor askedByMe;

	@UiField
	Label askedByMeLabel;

	@UiField
	Anchor allQuestions;

	@UiField
	Label allQuestionsLabel;

	@UiField
	Label separator1;

	@UiField
	Label separator2;

	private SimpleCallback askedByOthersCallback;

	private SimpleCallback askedByMeCallback;

	private SimpleCallback allQuestionsCallback;
	
	private AnonymMessages messages = AnonymMessages.instance;

	private static FilterComponentUiBinder uiBinder = GWT
			.create(FilterComponentUiBinder.class);

	interface FilterComponentUiBinder extends UiBinder<Widget, FilterComponent> {
	}

	public FilterComponent() {
		initWidget(uiBinder.createAndBindUi(this));
		selectAllQuestions();

		setAskedByOthersText(messages.askedByOthers());
		setAskedByMeText(messages.askedByMe());
		setAllQuestionsText(messages.allQuestions());
	}

	@UiHandler("askedByMe")
	void handleAskedByMe(ClickEvent event) {
		selectAskedByMe();
		call(askedByMeCallback);
	}

	@UiHandler("askedByOthers")
	void handleAskedByOthers(ClickEvent event) {
		selectAskedByOthers();
		call(askedByOthersCallback);
	}

	@UiHandler("allQuestions")
	void handleAllQuestions(ClickEvent event) {
		selectAllQuestions();
		call(allQuestionsCallback);
	}

	private void call(SimpleCallback callback) {
		if (callback != null) {
			callback.onEvent();
		}
	}

	public void hideSeparator1() {
		separator1.setVisible(false);
	}

	public void hideSeparator2() {
		separator2.setVisible(false);
	}

	public void hideAllQuestions() {
		allQuestions.setVisible(false);
		allQuestionsLabel.setVisible(false);

		allQuestions = null;
	}

	public void hideAskedByOthers() {
		askedByOthers.setVisible(false);
		askedByOthersLabel.setVisible(false);

		askedByOthers = null;
	}

	public void hideAskedByMe() {
		askedByMe.setVisible(false);
		askedByMeLabel.setVisible(false);

		askedByMe = null;
	}

	public void setAskedByOthersText(String text) {
		askedByOthers.setText(text);
		askedByOthersLabel.setText(text);
	}

	public void setAskedByMeText(String text) {
		askedByMe.setText(text);
		askedByMeLabel.setText(text);
	}

	public void setAllQuestionsText(String text) {
		allQuestions.setText(text);
		allQuestionsLabel.setText(text);
	}
	
	public void selectNone() {
		hideLabel(allQuestions, allQuestionsLabel);
		hideLabel(askedByOthers, askedByOthersLabel);
		hideLabel(askedByMe, askedByMeLabel);
	}

	public void selectAllQuestions() {
		showLabel(allQuestions, allQuestionsLabel);
		hideLabel(askedByOthers, askedByOthersLabel);
		hideLabel(askedByMe, askedByMeLabel);
	}

	public void selectAskedByOthers() {
		hideLabel(allQuestions, allQuestionsLabel);
		showLabel(askedByOthers, askedByOthersLabel);
		hideLabel(askedByMe, askedByMeLabel);
	}

	public void selectAskedByMe() {
		hideLabel(allQuestions, allQuestionsLabel);
		hideLabel(askedByOthers, askedByOthersLabel);
		showLabel(askedByMe, askedByMeLabel);
	}

	private void showLabel(Anchor anchor, Label label) {

		if (anchor == null) {
			return;
		}

		anchor.setVisible(false);
		label.setVisible(true);
	}

	private void hideLabel(Anchor anchor, Label label) {

		if (anchor == null) {
			return;
		}

		anchor.setVisible(true);
		label.setVisible(false);
	}

	public void setAskedByOthersCallback(SimpleCallback askedByOthersCallback) {
		this.askedByOthersCallback = askedByOthersCallback;
	}

	public void setAskedByMeCallback(SimpleCallback askedByMeCallback) {
		this.askedByMeCallback = askedByMeCallback;
	}

	public void setAllQuestionsCallback(SimpleCallback allQuestionsCallback) {
		this.allQuestionsCallback = allQuestionsCallback;
	}

}
