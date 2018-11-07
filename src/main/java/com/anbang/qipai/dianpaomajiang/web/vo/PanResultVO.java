package com.anbang.qipai.dianpaomajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.DianpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.PanResultDbo;

public class PanResultVO {

	private List<DianpaoMajiangPanPlayerResultVO> playerResultList;

	private boolean hu;

	private int panNo;

	private long finishTime;

	private List<NiaoPaiVO> niaoPaiList = new ArrayList<>();// 抓到的鸟牌

	private int paiCount;

	public PanResultVO(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		List<DianpaoMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList
					.add(new DianpaoMajiangPanPlayerResultVO(majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()),
							dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		}
		for (DianpaoMajiangPanPlayerResultVO playerResult : playerResultList) {
			if (playerResult.getNiaoPaiList().size() > 0) {
				setNiaoPaiList(playerResult.getNiaoPaiList());
			}
		}
		hu = dbo.isHu();
		panNo = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
		paiCount = dbo.getPanActionFrame().getPanAfterAction().getAvaliablePaiList().getPaiCount();
	}

	public List<DianpaoMajiangPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public boolean isHu() {
		return hu;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public int getPanNo() {
		return panNo;
	}

	public int getPaiCount() {
		return paiCount;
	}

	public void setPaiCount(int paiCount) {
		this.paiCount = paiCount;
	}

	public List<NiaoPaiVO> getNiaoPaiList() {
		return niaoPaiList;
	}

	public void setNiaoPaiList(List<NiaoPaiVO> niaoPaiList) {
		this.niaoPaiList = niaoPaiList;
	}

}
