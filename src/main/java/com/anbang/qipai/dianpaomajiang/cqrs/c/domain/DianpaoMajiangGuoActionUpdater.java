package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.listener.DianpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;

public class DianpaoMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());

		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findNotGuoLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠
			MajiangPai gangmoShoupai = player.getGangmoShoupai();
			// 那要我打牌
			if (player.getActionCandidates().isEmpty()) {
				// 啥也不能干，那只能打出牌
				/*
				 * 头风：抓牌后，手牌中单独一张的风牌字牌需要优先打出
				 */
				List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
				Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();

				for (MajiangPai pai : fangruShoupaiList) {
					if (!guipaiTypeSet.contains(pai) && MajiangPai.isZipai(pai)) {
						if (!gangmoShoupai.equals(pai) && player.getShoupaiCalculator().count(pai) == 1) {
							player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
						}
					}
				}
				if (!guipaiTypeSet.contains(gangmoShoupai) && MajiangPai.isZipai(gangmoShoupai)
						&& player.getShoupaiCalculator().count(gangmoShoupai) == 0) {
					player.addActionCandidate(new MajiangDaAction(player.getId(), gangmoShoupai));
				}
			}
			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				DianpaoMajiangPengGangActionStatisticsListener pengGangRecordListener = ju
						.getActionStatisticsListenerManager()
						.findListener(DianpaoMajiangPengGangActionStatisticsListener.class);
				MajiangPlayerAction finallyDoneAction = pengGangRecordListener.findPlayerFinallyDoneAction();
				if (finallyDoneAction != null) {// 有其他吃碰杠动作，先执行吃碰杠
					MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
					if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰
						MajiangPengAction doAction = (MajiangPengAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangPengAction(doAction.getActionPlayerId(),
								doAction.getDachupaiPlayerId(), doAction.getPai()));
					} else if (finallyDoneAction instanceof MajiangGangAction) {// 如果是杠
						MajiangGangAction doAction = (MajiangGangAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangGangAction(doAction.getActionPlayerId(),
								doAction.getDachupaiPlayerId(), doAction.getPai(), doAction.getGangType()));
					}
				} else {
					// 打牌那家的下家摸牌
					MajiangPlayer xiajiaPlayer = currentPan
							.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
					xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
				}
				pengGangRecordListener.updateForNextLun();// 清空动作缓存
			}
		} else if (action.getType().equals(MajiangPlayerActionType.gang)) {// 过的是别人杠牌之后我可以胡
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				// 杠牌那家摸牌
				MajiangPlayer gangPlayer = currentPan.findPlayerById(action.getActionPlayerId());
				gangPlayer.addActionCandidate(new MajiangMoAction(gangPlayer.getId(), new LundaoMopai()));
			}
		} else {

		}
	}

}
