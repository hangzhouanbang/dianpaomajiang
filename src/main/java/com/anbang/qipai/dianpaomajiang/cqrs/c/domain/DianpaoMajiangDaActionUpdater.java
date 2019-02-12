package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.comprehensive.TianHuAndDihuOpportunityDetector;
import com.dml.majiang.player.action.listener.comprehensive.GuoHuBuHuStatisticsListener;
import com.dml.majiang.player.action.listener.comprehensive.GuoPengBuPengStatisticsListener;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

/**
 * 一炮多响的打
 * 
 * @author lsc
 *
 */
public class DianpaoMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

	private boolean dianpao;

	@Override
	public void updateActions(MajiangDaAction daAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer daPlayer = currentPan.findPlayerById(daAction.getActionPlayerId());
		// 是否可以地胡
		TianHuAndDihuOpportunityDetector dianpaoDihuOpportunityDetector = ju.getActionStatisticsListenerManager()
				.findListener(TianHuAndDihuOpportunityDetector.class);
		boolean couldDihu = dianpaoDihuOpportunityDetector.ifDihuOpportunity();
		daPlayer.clearActionCandidates();

		MajiangPlayer xiajiaPlayer = currentPan.findXiajia(daPlayer);
		xiajiaPlayer.clearActionCandidates();
		// 下家可以吃
		// 点炮麻将没有吃
		// xiajiaPlayer.tryChiAndGenerateCandidateActions(daAction.getActionPlayerId(),
		// daAction.getPai());
		GuoHuBuHuStatisticsListener guoHuBuHuStatisticsListener = ju.getActionStatisticsListenerManager()
				.findListener(GuoHuBuHuStatisticsListener.class);
		GuoPengBuPengStatisticsListener guoPengBuPengStatisticsListener = ju.getActionStatisticsListenerManager()
				.findListener(GuoPengBuPengStatisticsListener.class);

		Set<String> canNotHuPlayers = guoHuBuHuStatisticsListener.getCanNotHuPlayers();
		Map<String, List<MajiangPai>> canNotPengPlayersPaiMap = guoPengBuPengStatisticsListener
				.getCanNotPengPlayersPaiMap();

		while (true) {
			if (!xiajiaPlayer.getId().equals(daAction.getActionPlayerId())) {
				// 其他的可以碰杠胡
				List<MajiangPai> fangruShoupaiList = xiajiaPlayer.getFangruShoupaiList();
				if (fangruShoupaiList.size() != 2) {
					boolean canPeng = true;// 可以碰
					if (canNotPengPlayersPaiMap.containsKey(xiajiaPlayer.getId())) {
						List<MajiangPai> canNotPengPaiList = canNotPengPlayersPaiMap.get(xiajiaPlayer.getId());
						if (canNotPengPaiList != null && !canNotPengPaiList.isEmpty()) {
							for (MajiangPai pai : canNotPengPaiList) {
								if (pai.equals(daAction.getPai())) {
									canPeng = false;
									break;
								}
							}
						}
					}
					if (canPeng) {
						xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
					}
				}
				xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				if (!canNotHuPlayers.contains(xiajiaPlayer.getId()) && dianpao) {
					// 点炮胡
					DianpaoMajiangPanResultBuilder dianpaoMajiangPanResultBuilder = (DianpaoMajiangPanResultBuilder) ju
							.getCurrentPanResultBuilder();
					boolean qingyise = dianpaoMajiangPanResultBuilder.isQingyise();
					GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
					// 先把这张牌放入计算器
					xiajiaPlayer.getShoupaiCalculator().addPai(daAction.getPai());
					DianpaoMajiangHu bestHu = DianpaoMajiangJiesuanCalculator.calculateBestDianpaoHu(
							couldDihu && !currentPan.getZhuangPlayerId().equals(xiajiaPlayer.getId()), gouXingPanHu,
							xiajiaPlayer, daAction.getPai(), qingyise);
					// 再把这张牌拿出计算器
					xiajiaPlayer.getShoupaiCalculator().removePai(daAction.getPai());
					if (bestHu != null) {
						bestHu.setDianpao(true);
						bestHu.setDianpaoPlayerId(daPlayer.getId());
						xiajiaPlayer.addActionCandidate(new MajiangHuAction(xiajiaPlayer.getId(), bestHu));
					}
				}

				xiajiaPlayer.checkAndGenerateGuoCandidateAction();
			} else {
				break;
			}
			xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
			xiajiaPlayer.clearActionCandidates();
		}
		currentPan.disablePlayerActionsByHuPengGangChiPriority();// 吃碰杠胡优先级判断

		// 如果所有玩家啥也做不了,那就下家摸牌
		if (currentPan.allPlayerHasNoActionCandidates()) {
			xiajiaPlayer = currentPan.findXiajia(daPlayer);
			xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
		}
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public void setDianpao(boolean dianpao) {
		this.dianpao = dianpao;
	}

}
