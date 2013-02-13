package com.anonymbook.client.controller;

import java.util.Date;

import com.anonymbook.client.event.StringCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.ui.question.QuestionModel;
import com.anonymbook.client.util.Anonym;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window.Location;

public final class TwitterController {
	
	private static AnonymMessages messages = AnonymMessages.instance;
	
	private TwitterController() {
	}

	public static void share(final Element tweetButton,
			String id, final QuestionModel model) {

		String anonymUrl = Location.getProtocol()
				+ "//apps.facebook.com/anonymbook";
		String questionUrl = anonymUrl + "?anonym_question=" + model.getKey()
				+ "&anonym_user=" + id;

		Anonym.shortUrl(questionUrl, new StringCallback() {
			@Override
			public void onEvent(String bitly) {
				handleMessage(tweetButton, bitly, model);
			}
		});
	}

	private static void handleMessage(Element tweetButton, String bitly, QuestionModel model) {

		String question = model.getQuestion();
		String suffix = " " + bitly + " %23anonymbook";
		String more = " ...";

		int size = 142 - suffix.length();

		String text = null;

		if (question.length() <= size) {
			text = question;
		} else {
			text = question.substring(0, size - more.length()) + more;
		}
		String url = "https://twitter.com/share?_=" + new Date().getTime() + "&count=none" + "&text=" + URL.encode(text) + suffix + "&url=";
		handleTweet(tweetButton, url);
	}

	private static native void handleTweet(Element element, String url)
	/*-{
		$wnd.$(element).click(
					function() {
						var options = "scrollbars=yes,resizable=no,toolbar=no,location=yes";
						var w = 550;
						var h = 420;
						var left = $wnd.Math.round(($wnd.screen.width / 2)
								- (w / 2));
						var top = $wnd.Math.round(($wnd.screen.height / 2)
								- (h / 2));

						$wnd.open(url, "dialog", options + ",width=" + w
								+ ",height=" + h + ",left=" + left
								+ ",top=" + top);
					}
				);
	}-*/;

}
