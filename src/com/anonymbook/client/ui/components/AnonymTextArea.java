package com.anonymbook.client.ui.components;

import com.anonymbook.client.event.AnonymTextAreaCallBack;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.lightface.LightFace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class AnonymTextArea extends Composite {

	private static AnonymTextAreaUiBinder uiBinder = GWT
			.create(AnonymTextAreaUiBinder.class);

	private String title;

	@UiField
	HTMLPanel submitPanel;

	@UiField
	TextArea textArea;

	@UiField
	Button anonymTextAreaButton;

	@UiField
	Label remainingChar;

	private int maxChar = 140;

	private AnonymTextAreaCallBack callBack;

	private boolean hasButton;
	
	private static final AnonymMessages messages = AnonymMessages.instance;

	interface AnonymTextAreaUiBinder extends UiBinder<Widget, AnonymTextArea> {
	}

	public AnonymTextArea() {
		this(null);
	}

	public AnonymTextArea(AnonymTextAreaCallBack callBack) {
		this(messages.answer(), 140, callBack);
	}

	public AnonymTextArea(String buttonText, int maxChar,
			AnonymTextAreaCallBack callBack) {
		initWidget(uiBinder.createAndBindUi(this));

		title = messages.whatsOnMind();

		this.hasButton = true;
		this.maxChar = maxChar;
		this.callBack = callBack;

		anonymTextAreaButton.setText(buttonText);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {

			}
		});

		initComponent();
	}

	@UiHandler("anonymTextAreaButton")
	void handleButtonClick(ClickEvent e) {
		if (getText().length() > maxChar) {
			LightFace.alert(messages.invalidMessage());
			return;
		}

		if (callBack != null) {
			callBack.onButtonClick(this);
		}
	}

	private void initComponent() {

		updateRemainingChar();

		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateRemainingChar();
			}
		});

		textArea.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				updateRemainingChar();
			}
		});

		textArea.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				updateRemainingChar();
			}
		});

		textArea.getElement().setId(DOM.createUniqueId());

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				setElasticArea(textArea.getElement().getId());
				textArea.setTitle(title);
				setBlankText(textArea.getElement().getId());
				textArea.setHeight(textArea.getElement().getClientHeight() / 2
						+ "px");
			}
		});
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private void updateRemainingChar() {
		int remainingCount = maxChar - textArea.getValue().length();
		remainingChar.setText(remainingCount + "");

		if (remainingCount < 0) {
			remainingChar.getElement().getStyle().setColor("#e00d13");
		} else {
			remainingChar.getElement().getStyle().setColor("#999999");
		}

		if (remainingCount < 0) {
			anonymTextAreaButton.setEnabled(false);
			anonymTextAreaButton.getElement().getStyle()
					.setCursor(Cursor.DEFAULT);

		} else {
			anonymTextAreaButton.setEnabled(true);
			anonymTextAreaButton.getElement().getStyle()
					.setCursor(Cursor.POINTER);
		}
	}

	private native void setElasticArea(String areaId)
	/*-{

		$wnd.$($wnd.document).ready(function() {
			$wnd.$("textarea#" + areaId).autoResize({
				// On resize:
				onResize : function() {
					$wnd.$(this).css({
						opacity : 0.8
					});
				},
				// After resize:
				animateCallback : function() {
					$wnd.$(this).css({
						opacity : 1
					});
				},
				// Quite slow animation:
				animate : false,
				// More extra space:
				extraSpace : 0
			});
		});
	}-*/;

	public void showSubmit() {
		updateRemainingChar();
		submitPanel.setVisible(true);
		callBack.onFocus();
		if(!hasButton){
			anonymTextAreaButton.setVisible(false);
		}
	}

	public void hideSubmit() {
		updateRemainingChar();
		submitPanel.setVisible(false);
		callBack.onLostFocus();
	}

	public String getText() {
		return textArea.getValue();
	}

	public void showInitialState() {
		textArea.setText("");
		hideSubmit();
		setBlankText(textArea.getElement().getId());
	}

	public native void setBlankText(String field)
	/*-{
		var instance = this;
		$wnd
				.$('#' + field)
				.focus(
						function() {
							if ($wnd.$(this).val() == $wnd.$(this)[0].title) {
								$wnd.$(this).css({
									'color' : '#000'
								});
								$wnd.$(this).val("");
								instance.@com.anonymbook.client.ui.components.AnonymTextArea::showSubmit()();
							}
						});

		$wnd
				.$('#' + field)
				.blur(
						function() {
							if ($wnd.$(this).val() == "") {
								$wnd.$(this).css({
									'color' : '#777'
								});
								$wnd.$(this).val($wnd.$(this)[0].title);
								instance.@com.anonymbook.client.ui.components.AnonymTextArea::hideSubmit()();
							}
						});

		$wnd.$('#' + field).blur();
	}-*/;

	public void setText(String text) {
		textArea.setValue(text);
		updateRemainingChar();
	}

	public void setButtonText(String buttonText) {
		anonymTextAreaButton.setText(buttonText);
	}

	public AnonymTextAreaCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(AnonymTextAreaCallBack callBack) {
		this.callBack = callBack;
	}

	public void setFontSize(Double size) {
		textArea.getElement().getStyle().setFontSize(size, Unit.PX);
	}

	public void hasButton(boolean hasButton) {
		this.hasButton = hasButton;
	}
	
	public void focus(boolean focus) {
		textArea.setFocus(focus);
	}

}
