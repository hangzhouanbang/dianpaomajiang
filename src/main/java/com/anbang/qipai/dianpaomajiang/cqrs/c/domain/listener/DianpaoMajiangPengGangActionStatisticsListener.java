package com.anbang.qipai.dianpaomajiang.cqrs.c.domain.listener;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.listener.gang.MajiangPlayerGangActionStatisticsListener;
import com.dml.majiang.player.action.listener.peng.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.position.MajiangPosition;

/**
 * 放炮麻将统计器，包括玩家放杠统计，总杠数
 * 
 * @author lsc
 *
 */
public class DianpaoMajiangPengGangActionStatisticsListener
		implements MajiangPlayerPengActionStatisticsListener, MajiangPlayerGangActionStatisticsListener {

	private Map<String, MajiangPlayerAction> playerActionMap = new HashMap<>();

	private Map<String, Integer> playerIdFangGangShuMap = new HashMap<>();

	private int tongpeiCount = 0;

	@Override
	public void updateForNextPan() {
		playerActionMap = new HashMap<>();
		playerIdFangGangShuMap = new HashMap<>();
		tongpeiCount = 0;
	}

	// 清空当前轮动作
	public void updateForNextLun() {
		playerActionMap.clear();
	}

	@Override
	public void update(MajiangGangAction gangAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());
		if (gangAction.isDisabledByHigherPriorityAction()) {// 如果被阻塞
			playerActionMap.put(player.getId(), gangAction);// 记录下被阻塞的动作
		} else {
			if (gangAction.getGangType().equals(GangType.gangdachu)) {
				String dachupaiPlayerId = gangAction.getDachupaiPlayerId();
				if (playerIdFangGangShuMap.containsKey(dachupaiPlayerId)) {
					Integer count = playerIdFangGangShuMap.get(dachupaiPlayerId) + 1;
					playerIdFangGangShuMap.put(dachupaiPlayerId, count);
				} else {
					playerIdFangGangShuMap.put(dachupaiPlayerId, 1);
				}
				MajiangPlayer dachupaiPlayer = currentPan.findPlayerById(gangAction.getDachupaiPlayerId());
				MajiangPlayer zhuangPlayer = currentPan.findPlayerByMenFeng(MajiangPosition.dong);
				MajiangPlayer shangjia = currentPan.findShangjia(zhuangPlayer);
				MajiangPlayer xiajia = currentPan.findXiajia(zhuangPlayer);
				// 如果上家杠庄家或者下家,通赔计数加2
				if (shangjia.getId().equals(player.getId()) && (zhuangPlayer.getId().equals(dachupaiPlayer.getId())
						|| xiajia.getId().equals(dachupaiPlayer.getId()))) {
					tongpeiCount += 2;
				}
			} else if (gangAction.getGangType().equals(GangType.kezigangshoupai)) {
				String dachupaiPlayerId = gangAction.getDachupaiPlayerId();
				if (dachupaiPlayerId != null) {
					if (playerIdFangGangShuMap.containsKey(dachupaiPlayerId)) {
						Integer count = playerIdFangGangShuMap.get(dachupaiPlayerId) + 1;
						playerIdFangGangShuMap.put(dachupaiPlayerId, count);
					} else {
						playerIdFangGangShuMap.put(dachupaiPlayerId, 1);
					}
					MajiangPlayer dachupaiPlayer = currentPan.findPlayerById(gangAction.getDachupaiPlayerId());
					MajiangPlayer zhuangPlayer = currentPan.findPlayerByMenFeng(MajiangPosition.dong);
					MajiangPlayer shangjia = currentPan.findShangjia(zhuangPlayer);
					MajiangPlayer xiajia = currentPan.findXiajia(zhuangPlayer);
					// 如果上家杠庄家或者下家,通赔计数加2
					if (shangjia.getId().equals(player.getId()) && (zhuangPlayer.getId().equals(dachupaiPlayer.getId())
							|| xiajia.getId().equals(dachupaiPlayer.getId()))) {
						tongpeiCount += 2;
					}
				}
			}
		}
	}

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		if (pengAction.isDisabledByHigherPriorityAction()) {// 如果被阻塞
			playerActionMap.put(player.getId(), pengAction);// 记录下被阻塞的动作
		} else {
			MajiangPlayer dachupaiPlayer = currentPan.findPlayerById(pengAction.getDachupaiPlayerId());
			MajiangPlayer zhuangPlayer = currentPan.findPlayerByMenFeng(MajiangPosition.dong);
			MajiangPlayer shangjia = currentPan.findShangjia(zhuangPlayer);
			MajiangPlayer xiajia = currentPan.findXiajia(zhuangPlayer);
			// 如果上家碰庄家或者下家,通赔计数加1
			if (shangjia.getId().equals(player.getId()) && (zhuangPlayer.getId().equals(dachupaiPlayer.getId())
					|| xiajia.getId().equals(dachupaiPlayer.getId()))) {
				tongpeiCount += 1;
			}
		}
	}

	public MajiangPlayerAction findPlayerFinallyDoneAction() {
		if (playerActionMap.isEmpty()) {
			return null;
		}
		for (MajiangPlayerAction action : playerActionMap.values()) {
			if (action.getType().equals(MajiangPlayerActionType.gang)) {
				return action;
			}
		}
		for (MajiangPlayerAction action : playerActionMap.values()) {
			if (action.getType().equals(MajiangPlayerActionType.peng)) {
				return action;
			}
		}
		return null;
	}

	public Map<String, MajiangPlayerAction> getPlayerActionMap() {
		return playerActionMap;
	}

	public void setPlayerActionMap(Map<String, MajiangPlayerAction> playerActionMap) {
		this.playerActionMap = playerActionMap;
	}

	public Map<String, Integer> getPlayerIdFangGangShuMap() {
		return playerIdFangGangShuMap;
	}

	public void setPlayerIdFangGangShuMap(Map<String, Integer> playerIdFangGangShuMap) {
		this.playerIdFangGangShuMap = playerIdFangGangShuMap;
	}

	public int getTongpeiCount() {
		return tongpeiCount;
	}

	public void setTongpeiCount(int tongpeiCount) {
		this.tongpeiCount = tongpeiCount;
	}

}
