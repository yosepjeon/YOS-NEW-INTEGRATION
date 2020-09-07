package com.yosep.msa.yosfrontserver.entity;

public class OrderState {
	public static final int WAIT = 0;
	public static final int BILLING_SUCCESS_BUT_BUY_FAIL = -1;
	public static final int SUCCESS = 1;
	public static final int SEND_MMS_ABOUT_FAIL = 2;
}
