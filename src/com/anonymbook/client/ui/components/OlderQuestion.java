package com.anonymbook.client.ui.components;

import com.anonymbook.client.resources.AnonymMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class OlderQuestion extends Composite {

	private static OlderQuestionUiBinder uiBinder = GWT
			.create(OlderQuestionUiBinder.class);

	interface OlderQuestionUiBinder extends UiBinder<Widget, OlderQuestion> {
	}

	public static interface Style extends CssResource {

		public String noMoreOlderQuestions();
	}

	@UiField
	Anchor seeMore;
	
	@UiField
	LabelElement seeMoreLabel;

	@UiField
	Anchor noMore;

	@UiField
	Image seeMoreLoading;

	@UiField
	HTMLPanel seeMoreContainer;

	@UiField
	Style style;
	
	private AnonymMessages messages = AnonymMessages.instance;

	public OlderQuestion() {
		initWidget(uiBinder.createAndBindUi(this));

		noMore.setText(messages.pagingWithoutMore());
		seeMoreLabel.setInnerText(messages.pagingWithMore());

		setStateEndOlderQuestions();
	}

	public void setStateLoading() {
		this.seeMore.setVisible(false);
		this.noMore.setVisible(false);
		this.seeMoreLoading.setVisible(true);
	}

	public void setStateOlderQuestion() {
		this.seeMore.setVisible(true);
		this.noMore.setVisible(false);
		this.seeMoreLoading.setVisible(false);

		this.seeMoreContainer.removeStyleName(style.noMoreOlderQuestions());
	}

	public void setStateEndOlderQuestions() {
		this.seeMore.setVisible(false);
		this.noMore.setVisible(true);
		this.seeMoreLoading.setVisible(false);

		this.seeMoreContainer.addStyleName(style.noMoreOlderQuestions());
	}

	public void addClickHandler(ClickHandler clickHandler) {
		seeMore.addDomHandler(clickHandler, ClickEvent.getType());
	}

}
