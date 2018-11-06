package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class DianpaoMajiangHu extends Hu {

	private DianpaoMajiangHufen hufen;

	private boolean huxingHu;// 三财神推倒就不是胡形的胡

	public DianpaoMajiangHu() {
	}

	public DianpaoMajiangHu(ShoupaiPaiXing shoupaiPaiXing, DianpaoMajiangHufen hufen) {
		super(shoupaiPaiXing);
		this.hufen = hufen;
		this.huxingHu = true;
	}

	public DianpaoMajiangHu(DianpaoMajiangHufen hufen) {
		this.hufen = hufen;
		this.huxingHu = false;
	}

	public DianpaoMajiangHufen getHufen() {
		return hufen;
	}

	public void setHufen(DianpaoMajiangHufen hufen) {
		this.hufen = hufen;
	}

	public boolean isHuxingHu() {
		return huxingHu;
	}

	public void setHuxingHu(boolean huxingHu) {
		this.huxingHu = huxingHu;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}
}
