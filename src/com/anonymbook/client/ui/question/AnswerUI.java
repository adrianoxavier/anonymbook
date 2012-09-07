package com.anonymbook.client.ui.question;

import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.util.Anonym;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AnswerUI extends Composite {

	private static AnswerUIUiBinder uiBinder = GWT
			.create(AnswerUIUiBinder.class);

	interface AnswerUIUiBinder extends UiBinder<Widget, AnswerUI> {
	}

	@UiField
	HTMLPanel rootPanel;

	@UiField
	Anchor xButtonElem;

	@UiField
	Anchor userName;

	@UiField
	Label answerText;

	@UiField
	Label answerTS;

	@UiField
	Label shareSeparator;

	@UiField
	Image userImage;

	@UiField
	Image iconImage;

	@UiField
	Anchor buttonShare;

	private AnswerModel model;

	private DataCallback<AnswerUI> removeCallback;
	
	private DataCallback<AnswerUI> shareCallback;
	
	private AnonymMessages messages = AnonymMessages.instance;

	public AnswerUI(AnswerModel model, boolean isAnonym) {
		initWidget(uiBinder.createAndBindUi(this));

		this.model = model;
		this.answerText.setWordWrap(true);
		this.xButtonElem.setVisible(false);

		setAnswer(model.getAnswer());
		setTimeAgo(Anonym.getTimeAgo(model.getAnswerTsAgo()));
		setAnonym(isAnonym);

		xButtonElem.setTitle(messages.removeAnswer());
		buttonShare.setText(messages.share());
		buttonShare.setTitle(AnonymMessages.instance.shareMessage());

		buttonShare.setVisible(false);
		shareSeparator.setVisible(false);

		if (model.isAuthor()) {
			addHandlers();
		}
	}
	
	public void makeRemovable() {
		addHandlers();
	}

	public AnswerModel getModel() {
		return model;
	}

	private void addHandlers() {
		MouseOverHandler mouseOverHandler = new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				xButtonElem.setVisible(true);
			}
		};

		MouseOutHandler mouseOutHandler = new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				xButtonElem.setVisible(false);
			}
		};

		rootPanel.addDomHandler(mouseOverHandler, MouseOverEvent.getType());
		rootPanel.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
	}

	private void setAnonym(boolean anonym) {
		if (anonym) {
			userImage.setVisible(false);
			iconImage.setVisible(true);

			setUserName(messages.anonymous());
		} else {
			userImage.setVisible(model.getAnswer() == null);
			iconImage.setVisible(false);
		}
	}

	public void setTimeAgo(String timeAgo) {
		answerTS.setText(timeAgo);
	}

	public String getAnswer() {
		return answerText.getText();
	}

	public void setAnswer(String answer) {
		answerText.setText(answer);
	}

	public void setUserName(String string) {
		userName.setText(string);
	}

	public void setUserImage(String imagePath) {
		userImage.setUrl(imagePath);
	}

	@UiHandler("userName")
	void handleSelectUserNameClick(ClickEvent event) {
	}

	@UiHandler("buttonShare")
	void handleShareClick(ClickEvent event) {
		if (shareCallback != null) {
			shareCallback.onEvent(this);
		}
	}

	@UiHandler("xButtonElem")
	void handleRemove(ClickEvent event) {
		if (removeCallback != null) {
			removeCallback.onEvent(this);
		}
	}

	public void showShare() {
		buttonShare.setVisible(true);
		shareSeparator.setVisible(true);
	}

	public void setShareListener(DataCallback<AnswerUI> Callback) {
		this.shareCallback = Callback;
	}

	public void setRemoveListener(DataCallback<AnswerUI> Callback) {
		this.removeCallback = Callback;
	}
}
