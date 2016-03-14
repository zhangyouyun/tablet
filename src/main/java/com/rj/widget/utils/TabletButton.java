package com.rj.widget.utils;

import android.graphics.drawable.Drawable;

public class TabletButton {

	private int callBackType;
	private String btTitle;
	private Drawable beforeImg;

	
	public int getCallBackType() {
		return callBackType;
	}
	public void setCallBackType(int callBackType) {
		this.callBackType = callBackType;
	}
	public String getTitle() {
		return btTitle;
	}
	public void setTitle(String btTitle) {
		this.btTitle = btTitle;
	}
	
	public Drawable getBeforeImg() {
		return beforeImg;
	}
	public void setBeforeImg(Drawable beforeImg) {
		this.beforeImg = beforeImg;
	}



}
