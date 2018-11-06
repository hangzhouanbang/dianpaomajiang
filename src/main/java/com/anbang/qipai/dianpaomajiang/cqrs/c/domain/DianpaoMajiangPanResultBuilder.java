package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;

public class DianpaoMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private boolean zhuaniao;
	private boolean dianpao;
	private boolean dapao;
	private boolean quzhongfa;

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {

		return null;
	}

	public boolean isZhuaniao() {
		return zhuaniao;
	}

	public void setZhuaniao(boolean zhuaniao) {
		this.zhuaniao = zhuaniao;
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public void setDianpao(boolean dianpao) {
		this.dianpao = dianpao;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public boolean isQuzhongfa() {
		return quzhongfa;
	}

	public void setQuzhongfa(boolean quzhongfa) {
		this.quzhongfa = quzhongfa;
	}

}
