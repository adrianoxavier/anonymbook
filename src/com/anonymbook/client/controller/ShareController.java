package com.anonymbook.client.controller;

import com.anonymbook.client.event.BooleanCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.ui.question.QuestionModel;

public final class ShareController {

	private static AnonymMessages messages = AnonymMessages.instance;

	private ShareController() {
	}

	public static void share(QuestionModel model, String userId) {

		share(model, userId, null);
	}

	public static void share(QuestionModel model, String userId,
			BooleanCallback shareCallback) {

		String link = "http://apps.facebook.com/anonymbook?anonym_question="
				+ model.getKey() + "&anonym_user=" + userId;
		String picture = "https://lh5.googleusercontent.com/_n1Zhj7j8juY/TbHJtMRTIFI/AAAAAAAAAGM/V7hYBiRF97k/s800/anonym-answer.png";
		String detail = messages.questionWithAnonymousAnswers();
		String actionName = messages.answerAnonymously();

		sendFeed(model.getQuestion(), detail, picture, link, actionName,
				shareCallback);
	}

	private static native void sendFeed(String title, String caption,
			String picture, String link, String actionName,
			BooleanCallback callback)
	/*-{

		$wnd.FB.ui(
			{
				method : 'feed',
				name : title,
				link : link,
				caption : caption,
				picture : picture,
				actions : [ {
					name : actionName,
					link : link
				} ]
			},
			function(response) {
				if (callback && response && response.post_id) {
					callback.@com.anonymbook.client.event.BooleanCallback::onEvent(Z)(true);
				} else if(callback) {
					callback.@com.anonymbook.client.event.BooleanCallback::onEvent(Z)(false);
				}
			});
	}-*/;

}
