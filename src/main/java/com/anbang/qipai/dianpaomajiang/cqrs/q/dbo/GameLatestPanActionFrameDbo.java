package com.anbang.qipai.dianpaomajiang.cqrs.q.dbo;

public class GameLatestPanActionFrameDbo {
	private String id;// 就是gameid
	private byte[] data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
