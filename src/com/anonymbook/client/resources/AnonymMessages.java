package com.anonymbook.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface AnonymMessages extends Messages {

	public static final AnonymMessages instance = GWT
			.create(AnonymMessages.class);

	public String secondsAgo(@PluralCount int seconds);

	public String minutesAgo(@PluralCount int minutes);

	public String hoursAgo(@PluralCount int hours);

	public String daysAgo(@PluralCount int days);

	public String monthsAgo(@PluralCount int months);

	public String inviteTitle();

	public String inviteMessage();

	public String friendNotInAnonymMessage(String name);

	public String friendNotInAnonymYesButton();

	public String shareMessage();

	public String welcomeMessage();

	public String confirmRemoveFriend(String friend);

	public String confirmRemoveQuestion();

	public String confirmRemoveAnswer();

	public String errorMessage();
	
	public String loginText();

	public String removeAnswer();

	public String removeFriend();

	public String removeQuestion();

	public String whatsOnMind();

	public String writeAnswer();

	public String askAndShare();

	public String askQuestionAnonymously();

	public String askAnonymous();

	public String questionReSend();
	
	public String questionSent(String name);

	public String questionWithAnonymousAnswers();

	public String answerAnonymously();

	public String invalidMessage();

	public String answeredBy(String name);
	
	public String arrowTip();

	public String pagingWithMore();

	public String pagingWithoutMore();

	public String searchByName();

	public String typeFriendName();

	public String cancel();

	public String question();

	public String questions();

	public String answer();

	public String quickQuestion();

	public String friends();

	public String share();

	public String anonymous();

	public String home();
	
	public String help();

	public String skip();

	public String askedByMe();

	public String askedByOthers();

	public String allQuestions();

	public String publicQuestions();

	public String rating();
	
	public String reSendText();

	public String adHeader();
	
}
