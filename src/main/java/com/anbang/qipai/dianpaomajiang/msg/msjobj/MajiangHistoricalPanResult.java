package com.anbang.qipai.dianpaomajiang.msg.msjobj;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.DianpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.PanResultDbo;

public class MajiangHistoricalPanResult {
	private String gameId;
	private int no;// 盘数
	private long finishTime;// 完成时间
	private List<DianpaoMajiangPanPlayerResultMO> playerResultList;

	public MajiangHistoricalPanResult(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		gameId = majiangGameDbo.getId();
		List<DianpaoMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList.add(new DianpaoMajiangPanPlayerResultMO(
					majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()), panPlayerResult)));
		}
		no = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public List<DianpaoMajiangPanPlayerResultMO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<DianpaoMajiangPanPlayerResultMO> playerResultList) {
		this.playerResultList = playerResultList;
	}

}
