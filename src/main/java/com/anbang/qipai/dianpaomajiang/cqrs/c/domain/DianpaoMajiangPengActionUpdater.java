package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.listener.DianpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;

/**
 * 碰的那个人要打牌
 * 
 * @author lsc
 *
 */
public class DianpaoMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		DianpaoMajiangPengGangActionStatisticsListener pengGangRecordListener = ju.getActionStatisticsListenerManager()
				.findListener(DianpaoMajiangPengGangActionStatisticsListener.class);
		if (pengAction.isDisabledByHigherPriorityAction()) {// 如果动作被阻塞
			player.clearActionCandidates();// 玩家已经做了决定，要删除动作
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 所有玩家行牌结束，并且没人胡
				MajiangPlayerAction finallyDoneAction = pengGangRecordListener.findPlayerFinallyDoneAction();// 找出最终应该执行的动作
				MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
				if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰，也只能是碰
					MajiangPengAction action = (MajiangPengAction) finallyDoneAction;
					actionPlayer.addActionCandidate(new MajiangPengAction(action.getActionPlayerId(),
							action.getDachupaiPlayerId(), action.getPai()));
				}
				pengGangRecordListener.updateForNextLun();// 清空动作缓存
			}
		} else {
			currentPan.clearAllPlayersActionCandidates();
			pengGangRecordListener.updateForNextLun();// 清空动作缓存

			// 刻子杠手牌
			player.tryKezigangshoupaiAndGenerateCandidateAction();

			// 需要有“过”
			player.checkAndGenerateGuoCandidateAction();

			if (player.getActionCandidates().isEmpty()) {
				// 啥也不能干，那只能打出牌
				/*
				 * 头风：抓牌后，手牌中单独一张的风牌字牌需要优先打出
				 */
				List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
				Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();

				for (MajiangPai pai : fangruShoupaiList) {
					if (!guipaiTypeSet.contains(pai) && MajiangPai.isZipai(pai)) {
						if (player.getShoupaiCalculator().count(pai) == 1) {
							player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
						}
					}
				}
			}
			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
