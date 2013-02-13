package com.anonymbook.client.home;

import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.ui.AnonymHeader;
import com.anonymbook.client.ui.AnonymLeft;
import com.anonymbook.client.ui.AnonymRight;
import com.anonymbook.client.ui.AnonymTop;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UserHome extends Composite {

	private static UserHomeUiBinder uiBinder = GWT
			.create(UserHomeUiBinder.class);

	interface UserHomeUiBinder extends UiBinder<Widget, UserHome> {
	}

	@UiField
	AnonymTop top;

	@UiField
	AnonymHeader header;

	@UiField
	AnonymLeft left;

	@UiField
	AnonymRight right;
	
	@UiField
	Label adLabel;

	private SimpleCallback loadCallback;

	public UserHome() {
		initWidget(uiBinder.createAndBindUi(this));
		
		AnonymMessages messages = AnonymMessages.instance;
		
		adLabel.setText(messages.adHeader());
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if (loadCallback != null) {
			loadCallback.onEvent();
		}
	}

	public void addLoadCallback(SimpleCallback callback) {
		loadCallback = callback;
	}

	public AnonymTop getTop() {
		return top;
	}

	public AnonymHeader getHeader() {
		return header;
	}

	public AnonymLeft getLeft() {
		return left;
	}

	public AnonymRight getRight() {
		return right;
	}

	public void setTop(AnonymTop top) {
		this.top = top;
	}

	public void setHeader(AnonymHeader header) {
		this.header = header;
	}

	public void setLeft(AnonymLeft left) {
		this.left = left;
	}

	public void setRight(AnonymRight right) {
		this.right = right;
	}

}
