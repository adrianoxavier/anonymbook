package com.anonymbook.client.ui.question;

import java.util.List;

import com.anonymbook.client.controller.TwitterController;
import com.anonymbook.client.event.AnonymTextAreaCallBack;
import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.event.QuestionCallBack;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.ui.components.AnonymTextArea;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QuestionUI extends Composite {

	private static QuestionUiBinder uiBinder = GWT
			.create(QuestionUiBinder.class);

	interface QuestionUiBinder extends UiBinder<Widget, QuestionUI> {
	}

	Element ttelement;

	@UiField
	HTMLPanel rootPanel;

	@UiField
	HTMLPanel answerList;

	@UiField
	HTMLPanel notAnswered;

	@UiField
	Label questionText;

	@UiField
	Label anonymText;

	@UiField
	LabelElement questionTS;

	@UiField
	Anchor xButtonElem;

	@UiField
	Image userImageBig;

	@UiField
	Image questionUserImage;

	@UiField
	Image questionIconImage;

	@UiField
	HTMLPanel responseBlock;

	@UiField
	AnonymTextArea textArea;

	@UiField
	HTMLPanel tooltip;

	@UiField
	HTMLPanel tooltipText;

	@UiField
	AnchorElement twitter;

	@UiField
	Label reSendSeparator;

	@UiField
	Anchor reSendButton;

	@UiField
	Label shareLabel;

	@UiField
	Anchor shareButton;

	private QuestionModel model;

	private AnonymUser user;

	private String userId;

	private QuestionCallBack questionsCallBack;

	private String answer;

	private boolean isAnonym;

	private AnonymMessages messages = AnonymMessages.instance;

	public QuestionUI(AnonymUser user, String userdId,
			JSONObject questionObject, QuestionCallBack questionsCallBack) {

		initWidget(uiBinder.createAndBindUi(this));
		this.user = user;
		this.isAnonym = true;
		this.tooltip.setVisible(false);
		this.questionsCallBack = questionsCallBack;
		this.userId = userdId;
		this.model = new QuestionModel(questionObject);

		init();
	}

	private void init() {
		this.questionText.setWordWrap(true);
		this.xButtonElem.setVisible(false);
		this.questionUserImage.setVisible(false);
		this.questionIconImage.setVisible(false);

		this.reSendSeparator.setVisible(false);
		this.reSendButton.setVisible(false);

		this.xButtonElem.setTitle(messages.removeQuestion());
		this.textArea.setTitle(messages.writeAnswer());
		this.reSendButton.setText(messages.reSendText());
		this.reSendButton.setTitle(messages.reSendText());
		this.shareButton.setText(messages.share());
		this.shareButton.setTitle(messages.share());

		addListenters();
		fillContent();
	}

	@UiHandler("xButtonElem")
	void handleRemoveClick(ClickEvent event) {
		questionsCallBack.onDelete(this);
	}

	@UiHandler("userImageBig")
	void handleSelectUserImageClick(ClickEvent event) {
		questionsCallBack.onSelect(this);
	}

	@UiHandler("reSendButton")
	void handleResenderClick(ClickEvent event) {
		questionsCallBack.onReSend(this);
	}

	@UiHandler("shareButton")
	void handleShareClick(ClickEvent event) {
		questionsCallBack.onSharePublic(this);
	}

	private void addListenters() {

		textArea.setCallBack(new AnonymTextAreaCallBack() {
			@Override
			public void onFocus() {
			}

			@Override
			public void onLostFocus() {
			}

			@Override
			public void onButtonClick(AnonymTextArea textArea) {
				answer = textArea.getText();
				questionsCallBack.onAnswer(QuestionUI.this);
			}
		});

		rootPanel.getElement().setId(DOM.createUniqueId());

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

		MouseOverHandler mouseOverHandlerTooltip = new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				tooltip.setVisible(true);
			}
		};

		MouseOutHandler mouseOutHandlerTooltip = new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				tooltip.setVisible(false);
			}
		};

		userImageBig.addDomHandler(mouseOverHandlerTooltip,
				MouseOverEvent.getType());
		userImageBig.addDomHandler(mouseOutHandlerTooltip,
				MouseOutEvent.getType());
	}

	private void setImageVisible(boolean visible) {
		if (isAnonym) {
			questionUserImage.setVisible(visible);
		} else {
			questionIconImage.setVisible(visible);
		}
	}

	private void fillContent() {

		textArea.showInitialState();

		questionText.setText(model.getQuestion());
		questionTS.setInnerText(Anonym.getTimeAgo(model.getQuestionTsAgo()));

		answerList.clear();

		// configura o botão do twitter.
		TwitterController.share(twitter, userId, model);

		if (model.getRecipientId() == null) {

			reSendButton.setVisible(true);
			reSendSeparator.setVisible(true);

			isAnonym = false;

			for (int i = model.getAnswersList().size() - 1; i >= 0; i--) {
				AnswerModel answerModel = model.getAnswersList().get(i);

				AnswerUI answerUi = new AnswerUI(answerModel, true);
				answerUi.setRemoveListener(new DataCallback<AnswerUI>() {
					@Override
					public void onEvent(AnswerUI answer) {
						questionsCallBack.onAnswerDelete(QuestionUI.this,
								answer);
					}
				});

				if (model.getOwner() != null
						&& model.getOwner().equals(user.getId())) {
					answerUi.makeRemovable();
				}

				answerList.add(answerUi);
			}

			setUserImage(FB.image(userId, "square"));

			FB.user(userId, new JavaScriptCallback() {
				@Override
				public void onEvent(JavaScriptObject data) {
					String name = Anonym.get(data, "name");
					anonymText.setText(name);
					setUserName(name);
				}
			});
		} else {
			isAnonym = true;
			this.questionUserImage.setUrl(FB.image(userId, "square"));

			// remove o botão de tweet.
			twitter.removeFromParent();

			FB.user(model.getRecipientId(), new JavaScriptCallback() {
				@Override
				public void onEvent(JavaScriptObject data) {
					String name = Anonym.get(data, "name");
					setUserName(name);

					AnswerModel answerModel = new AnswerModel();
					List<AnswerModel> list = model.getAnswersList();
					if (list.size() > 0) {
						answerModel.setAnswer(list.get(0).getAnswer());
						answerModel
								.setAnswerTsAgo(list.get(0).getAnswerTsAgo());

						AnswerUI answer = new AnswerUI(answerModel, false);
						answer.setUserName(name);
						answer.showShare();
						answerList.add(answer);

						answer.setShareListener(new DataCallback<AnswerUI>() {
							@Override
							public void onEvent(AnswerUI data) {
								questionsCallBack.onShare(QuestionUI.this);
							}
						});
					}
				}
			});

			anonymText.setText("Anonymous");
			setUserImage(FB.image(model.getRecipientId(), "square"));

			if (model.getRecipientId().equals(userId)) {
				setAnswered(model.getAnswersList().size() > 0);
			} else {
				setAnswered(true);
			}
		}

		setImageVisible(true);
	}

	private void setAnswered(boolean isAnswered) {
		if (isAnswered) {
			answerList.setVisible(true);
			notAnswered.setVisible(false);
		} else {
			answerList.setVisible(false);
			notAnswered.setVisible(true);
		}
	}

	private void setUserName(String string) {
		tooltipText.getElement().setInnerText(string);
	}

	public void setReSendVisible(boolean visible) {
		reSendButton.setVisible(visible);
		reSendSeparator.setVisible(visible);
	}

	public void setUserImage(String imagePath) {
		userImageBig.setUrl(imagePath);
	}

	public void setReadOnly() {
		xButtonElem.removeFromParent();

		if (model.getAnswersList().size() == 0
				&& model.getRecipientId() != null) {
			responseBlock.setVisible(false);
		}
	}

	public void updateQuestion(JSONObject question) {
		updateQuestion(new QuestionModel(question));
	}

	public void updateQuestion(QuestionModel model) {
		this.model = model;
		fillContent();
	}

	public void removeAnswer(AnswerUI answerUI) {
		answerList.remove(answerUI);
	}

	public String getQuestionKey() {
		return model.getKey();
	}

	public String getQuestionAnswer() {
		return answer;
	}

	public QuestionModel getModel() {
		return model;
	}

}
