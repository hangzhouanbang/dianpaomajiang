package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.pan.frame.PanActionFrame;

public class MajiangActionResult {

	private MajiangGameValueObject majiangGame;
	private PanActionFrame panActionFrame;
	private DianpaoMajiangPanResult panResult;
	private DianpaoMajiangJuResult juResult;

	public MajiangGameValueObject getMajiangGame() {
		return majiangGame;
	}

	public void setMajiangGame(MajiangGameValueObject majiangGame) {
		this.majiangGame = majiangGame;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public DianpaoMajiangPanResult getPanResult() {
		return panResult;
	}

	public void setPanResult(DianpaoMajiangPanResult panResult) {
		this.panResult = panResult;
	}

	public DianpaoMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(DianpaoMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
