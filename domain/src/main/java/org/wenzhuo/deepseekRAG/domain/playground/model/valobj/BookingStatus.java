package org.wenzhuo.deepseekRAG.domain.playground.model.valobj;

public enum BookingStatus {

	CONFIRMED("预定"), COMPLETED("完成"), CANCELLED("取消");
	private String tag;
	BookingStatus(String tag) {
		this.tag = tag;

	}

}