package org.wenzhuo.deepseekRAG.domain.playground.model.valobj;

public enum BookingClass {

	ECONOMY("经济舱"),
	PREMIUM_ECONOMY("头等舱"),
	BUSINESS("商务舱");
	private String tag;

	BookingClass(String tag) {
		this.tag = tag;
	}



}
