package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.listener.DianpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;

public class DianpaoMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private boolean zhuaniao;
	private int niaoshu;
	private boolean dianpao;
	private boolean dapao;
	private boolean quzhongfabai;

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		DianpaoMajiangPanResult latestFinishedPanResult = (DianpaoMajiangPanResult) ju.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (DianpaoMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}

		DianpaoMajiangPengGangActionStatisticsListener fangGangCounter = ju.getActionStatisticsListenerManager()
				.findListener(DianpaoMajiangPengGangActionStatisticsListener.class);
		Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();

		List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();
		DianpaoMajiangPanResult dianpaoMajiangPanResult = new DianpaoMajiangPanResult();
		dianpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
		List<String> playerIdList = currentPan.sortedPlayerIdList();
		List<DianpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
		// 鸟分
		DianpaoMajiangNiao niao = new DianpaoMajiangNiao(quzhongfabai, zhuaniao, niaoshu);
		niao.calculate();
		// 放炮玩家id
		String dianPaoPlayerId = "";
		if (huPlayers.size() > 1) {// 一炮多响
			Set<String> huPlayerSet = new HashSet<>();
			// 放炮玩家输给胡家们的胡分
			int delta = 0;
			// 计算胡家胡分
			for (MajiangPlayer huPlayer : huPlayers) {
				huPlayerSet.add(huPlayer.getId());
				DianpaoMajiangPanPlayerResult huPlayerResult = new DianpaoMajiangPanPlayerResult();
				DianpaoMajiangHu hu = (DianpaoMajiangHu) huPlayer.getHu();
				DianpaoMajiangHufen huPlayerHufen = hu.getHufen();
				dianPaoPlayerId = hu.getDianpaoPlayerId();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				delta += huPlayerHufen.getValue();
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				DianpaoMajiangGang gang = new DianpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				DianpaoMajiangPao pao = new DianpaoMajiangPao(huPlayer);
				pao.calculate(dapao, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				DianpaoMajiangNiao niaofen = new DianpaoMajiangNiao();
				niaofen.jiesuan(-niao.getValue());
				huPlayerResult.setNiao(niaofen);
				playerResultList.add(huPlayerResult);
			}
			// 计算非胡玩家胡分
			for (String playerId : playerIdList) {
				if (!huPlayerSet.contains(playerId)) {
					if (dianPaoPlayerId.equals(playerId)) {// 计算点炮玩家分数
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						DianpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount = playerFangGangMap.get(playerId);
						if (fangGangCount == null) {
							fangGangCount = 0;
						}
						DianpaoMajiangGang gang = new DianpaoMajiangGang(buHuplayer);
						if (huPlayers.get(0).getHu().isQianggang()) {
							List<GangchuPaiZu> gangchupaiZuList = buHuplayer.getGangchupaiZuList();
							GangchuPaiZu gangChuPaiZu = gangchupaiZuList.remove(gangchupaiZuList.size() - 1);
							PengchuPaiZu pengChuPaiZu = new PengchuPaiZu(
									new Kezi(gangChuPaiZu.getGangzi().getPaiType()), null, buHuplayer.getId());
							buHuplayer.getPengchupaiZuList().add(pengChuPaiZu);
							gang.setZimoMingGangShu(gang.getZimoMingGangShu() - 1);// 被抢的杠不算杠分
						}
						gang.calculate(playerIdList.size(), fangGangCount);
						buHuPlayerResult.setGang(gang);
						// 计算炮分
						DianpaoMajiangPao pao = new DianpaoMajiangPao(buHuplayer);
						pao.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao);
						// 计算鸟分
						niao.jiesuan(niao.getValue() * huPlayers.size());
						buHuPlayerResult.setNiao(niao);
						playerResultList.add(buHuPlayerResult);
					} else {// 计算其他玩家分数
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						// 计算杠分
						Integer fangGangCount = playerFangGangMap.get(playerId);
						if (fangGangCount == null) {
							fangGangCount = 0;
						}
						DianpaoMajiangGang gang = new DianpaoMajiangGang(buHuplayer);
						gang.calculate(playerIdList.size(), fangGangCount);
						buHuPlayerResult.setGang(gang);
						// 计算炮分
						DianpaoMajiangPao pao = new DianpaoMajiangPao(buHuplayer);
						pao.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao);
						// 计算鸟分
						DianpaoMajiangNiao niaofen = new DianpaoMajiangNiao();
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					}
				}
			}
			// 两两结算杠、炮
			for (int i = 0; i < playerResultList.size(); i++) {
				DianpaoMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				DianpaoMajiangGang gang1 = playerResult1.getGang();
				DianpaoMajiangPao pao1 = playerResult1.getPao();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					DianpaoMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					DianpaoMajiangGang gang2 = playerResult2.getGang();
					DianpaoMajiangPao pao2 = playerResult2.getPao();
					// 结算杠分
					int zimogang1 = gang1.getZimoMingGangShu();
					int zimogang2 = gang2.getZimoMingGangShu();
					int angang1 = gang1.getAnGangShu();
					int angang2 = gang2.getAnGangShu();
					gang1.jiesuan(-zimogang2 - angang2 * 2);
					gang2.jiesuan(-zimogang1 - angang1 * 2);
					// 结算炮分
					int paovalue1 = pao1.getValue();
					int paovalue2 = pao2.getValue();
					pao1.jiesuan(-paovalue2);
					pao2.jiesuan(-paovalue1);
				}
			}
			String zhunagPlayerId = currentPan.getZhuangPlayerId();
			MajiangPlayer zhuangPlayer = currentPan.findPlayerById(zhunagPlayerId);
			if (playerResultList.size() >= 3 && zhuangPlayer.getHu() != null
					&& fangGangCounter.getTongpeiCount() >= 3) {// 3、 4人游戏,如果庄家胡,北风家通赔
				int detal = 0;// 北风家需要替其他玩家赔的分数
				MajiangPlayer shangjia = currentPan.findShangjia(zhuangPlayer);
				for (DianpaoMajiangPanPlayerResult playerResult : playerResultList) {
					for (MajiangPlayer huPlayer : huPlayers) {
						if (!shangjia.getId().equals(playerResult.getPlayerId())
								&& !huPlayer.getId().equals(playerResult.getPlayerId())) {
							detal += playerResult.getHufen().getValue();
							playerResult.getHufen().setValue(0);
						}
					}
				}
				for (DianpaoMajiangPanPlayerResult playerResult : playerResultList) {
					if (shangjia.getId().equals(playerResult.getPlayerId())) {
						playerResult.getHufen().setValue(playerResult.getHufen().getValue() + detal);
						playerResult.setTongpei(true);
					}
				}
			}
			playerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				int score = playerResult.getHufen().getValue() + playerResult.getNiao().getTotalScore()
						+ playerResult.getGang().getValue() + playerResult.getPao().getTotalscore();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
			});
			dianpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
			dianpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			dianpaoMajiangPanResult.setPanPlayerResultList(playerResultList);
			dianpaoMajiangPanResult.setHu(true);
			dianpaoMajiangPanResult.setZimo(false);
			dianpaoMajiangPanResult.setDianpaoPlayerId(dianPaoPlayerId);
			return dianpaoMajiangPanResult;
		} else if (huPlayers.size() == 1) {// 一人胡
			MajiangPlayer huPlayer = huPlayers.get(0);
			DianpaoMajiangHu hu = (DianpaoMajiangHu) huPlayer.getHu();
			DianpaoMajiangHufen huPlayerHufen = hu.getHufen();
			dianPaoPlayerId = hu.getDianpaoPlayerId();
			if (hu.isDianpao()) {// 点炮胡
				// 结算胡数
				DianpaoMajiangPanPlayerResult huPlayerResult = new DianpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				// 放炮玩家输给胡家的胡数
				int delta = huPlayerHufen.getValue();
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				DianpaoMajiangGang gang = new DianpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				DianpaoMajiangPao pao = new DianpaoMajiangPao(huPlayer);
				pao.calculate(dapao, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				niao.jiesuan(niao.getValue());
				huPlayerResult.setNiao(niao);
				playerResultList.add(huPlayerResult);
				playerIdList.forEach((playerId) -> {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else if (playerId.equals(hu.getDianpaoPlayerId())) {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						DianpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(playerId);
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						DianpaoMajiangGang gang1 = new DianpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						DianpaoMajiangPao pao1 = new DianpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						DianpaoMajiangNiao niaofen = new DianpaoMajiangNiao();
						niaofen.jiesuan(-niao.getValue());
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					} else {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(buHuplayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						DianpaoMajiangGang gang1 = new DianpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						DianpaoMajiangPao pao1 = new DianpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new DianpaoMajiangNiao());
						playerResultList.add(buHuPlayerResult);
					}
				});
			}
			if (hu.isQianggang()) {// 抢杠胡
				// 结算胡数
				DianpaoMajiangPanPlayerResult huPlayerResult = new DianpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				// 放炮玩家输给胡家的胡数
				int delta = huPlayerHufen.getValue();
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				DianpaoMajiangGang gang = new DianpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				DianpaoMajiangPao pao = new DianpaoMajiangPao(huPlayer);
				pao.calculate(dapao, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				niao.jiesuan(niao.getValue());
				huPlayerResult.setNiao(niao);
				playerResultList.add(huPlayerResult);
				playerIdList.forEach((playerId) -> {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else if (playerId.equals(hu.getDianpaoPlayerId())) {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						DianpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(playerId);
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						DianpaoMajiangGang gang1 = new DianpaoMajiangGang(buHuplayer);
						gang1.setZimoMingGangShu(gang1.getZimoMingGangShu() - 1);// 被抢的杠不算杠分
						List<GangchuPaiZu> gangchupaiZuList = buHuplayer.getGangchupaiZuList();
						GangchuPaiZu gangChuPaiZu = gangchupaiZuList.remove(gangchupaiZuList.size() - 1);
						PengchuPaiZu pengChuPaiZu = new PengchuPaiZu(new Kezi(gangChuPaiZu.getGangzi().getPaiType()),
								null, buHuplayer.getId());
						buHuplayer.getPengchupaiZuList().add(pengChuPaiZu);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						DianpaoMajiangPao pao1 = new DianpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						DianpaoMajiangNiao niaofen = new DianpaoMajiangNiao();
						niaofen.jiesuan(-niao.getValue());
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					} else {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(buHuplayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						DianpaoMajiangGang gang1 = new DianpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						DianpaoMajiangPao pao1 = new DianpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new DianpaoMajiangNiao());
						playerResultList.add(buHuPlayerResult);
					}
				});
			}
			if (hu.isZimo()) {// 自摸胡
				// 结算胡数
				DianpaoMajiangPanPlayerResult huPlayerResult = new DianpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());

				// 其他人输给胡家的胡数
				int delta = huPlayerHufen.getValue();
				huPlayerHufen.setValue(huPlayerHufen.getValue() * (playerIdList.size() - 1));
				huPlayerResult.setHufen(huPlayerHufen);
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				DianpaoMajiangGang gang = new DianpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				DianpaoMajiangPao pao = new DianpaoMajiangPao(huPlayer);
				pao.calculate(dapao, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				niao.jiesuan(niao.getValue() * (playerIdList.size() - 1));
				huPlayerResult.setNiao(niao);
				playerResultList.add(huPlayerResult);
				for (String playerId : playerIdList) {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else {
						DianpaoMajiangPanPlayerResult buHuPlayerResult = new DianpaoMajiangPanPlayerResult();
						MajiangPlayer buHuPlayer = currentPan.findPlayerById(playerId);
						buHuPlayerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						buHuPlayerResult
								.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuPlayer));
						DianpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(buHuPlayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						DianpaoMajiangGang gang1 = new DianpaoMajiangGang(buHuPlayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						DianpaoMajiangPao pao1 = new DianpaoMajiangPao(buHuPlayer);
						pao1.calculate(dapao, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						DianpaoMajiangNiao niaofen = new DianpaoMajiangNiao();
						niaofen.jiesuan(-niao.getValue());
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					}
				}
			}
			// 两两结算杠、炮
			for (int i = 0; i < playerResultList.size(); i++) {
				DianpaoMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				DianpaoMajiangGang gang1 = playerResult1.getGang();
				DianpaoMajiangPao pao1 = playerResult1.getPao();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					DianpaoMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					DianpaoMajiangGang gang2 = playerResult2.getGang();
					DianpaoMajiangPao pao2 = playerResult2.getPao();
					// 结算杠分
					int zimogang1 = gang1.getZimoMingGangShu();
					int zimogang2 = gang2.getZimoMingGangShu();
					int angang1 = gang1.getAnGangShu();
					int angang2 = gang2.getAnGangShu();
					gang1.jiesuan(-zimogang2 - angang2 * 2);
					gang2.jiesuan(-zimogang1 - angang1 * 2);
					// 结算炮分
					int paovalue1 = pao1.getValue();
					int paovalue2 = pao2.getValue();
					pao1.jiesuan(-paovalue2);
					pao2.jiesuan(-paovalue1);
				}
			}
			String zhunagPlayerId = currentPan.getZhuangPlayerId();
			MajiangPlayer zhuangPlayer = currentPan.findPlayerById(zhunagPlayerId);
			if (playerResultList.size() >= 3 && zhuangPlayer.getHu() != null
					&& fangGangCounter.getTongpeiCount() >= 3) {// 3、 4人游戏,如果庄家胡,北风家通赔
				int detal = 0;// 北风家需要替其他玩家赔的分数
				MajiangPlayer shangjia = currentPan.findShangjia(zhuangPlayer);
				for (DianpaoMajiangPanPlayerResult playerResult : playerResultList) {
					for (MajiangPlayer huplayer : huPlayers) {
						if (!shangjia.getId().equals(playerResult.getPlayerId())
								&& !huplayer.getId().equals(playerResult.getPlayerId())) {
							detal += playerResult.getHufen().getValue();
							playerResult.getHufen().setValue(0);
						}
					}
				}
				for (DianpaoMajiangPanPlayerResult playerResult : playerResultList) {
					if (shangjia.getId().equals(playerResult.getPlayerId())) {
						playerResult.getHufen().setValue(playerResult.getHufen().getValue() + detal);
						playerResult.setTongpei(true);
					}
				}
			}
			playerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				int score = playerResult.getHufen().getValue() + playerResult.getNiao().getTotalScore()
						+ playerResult.getGang().getValue() + playerResult.getPao().getTotalscore();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
			});
			dianpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
			dianpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			dianpaoMajiangPanResult.setPanPlayerResultList(playerResultList);
			dianpaoMajiangPanResult.setHu(true);
			dianpaoMajiangPanResult.setZimo(hu.isZimo());
			dianpaoMajiangPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
			return dianpaoMajiangPanResult;
		} else {// 流局
			// 结算胡数
			playerIdList.forEach((playerId) -> {
				MajiangPlayer player = currentPan.findPlayerById(playerId);
				DianpaoMajiangPanPlayerResult playerResult = new DianpaoMajiangPanPlayerResult();
				playerResult.setPlayerId(playerId);
				// 计算非胡玩家分数
				playerResult.setHufen(DianpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(player));
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(playerId);
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				DianpaoMajiangGang gang = new DianpaoMajiangGang(player);
				gang.calculate(playerIdList.size(), fangGangCount);
				playerResult.setGang(gang);
				// 计算炮分
				DianpaoMajiangPao pao = new DianpaoMajiangPao(player);
				pao.calculate(dapao, playerIdList.size());
				playerResult.setPao(pao);
				// 计算鸟分
				playerResult.setNiao(new DianpaoMajiangNiao());
				playerResultList.add(playerResult);
			});
			// 两两结算杠、炮
			for (int i = 0; i < playerResultList.size(); i++) {
				DianpaoMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				DianpaoMajiangGang gang1 = playerResult1.getGang();
				DianpaoMajiangPao pao1 = playerResult1.getPao();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					DianpaoMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					DianpaoMajiangGang gang2 = playerResult2.getGang();
					DianpaoMajiangPao pao2 = playerResult2.getPao();
					// 结算杠分
					int zimogang1 = gang1.getZimoMingGangShu();
					int zimogang2 = gang2.getZimoMingGangShu();
					int angang1 = gang1.getAnGangShu();
					int angang2 = gang2.getAnGangShu();
					gang1.jiesuan(-zimogang2 - angang2 * 2);
					gang2.jiesuan(-zimogang1 - angang1 * 2);
					// 结算炮分
					int paovalue1 = pao1.getValue();
					int paovalue2 = pao2.getValue();
					pao1.jiesuan(-paovalue2);
					pao2.jiesuan(-paovalue1);
				}
			}
			playerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				int score = playerResult.getHufen().getValue() + playerResult.getNiao().getTotalScore()
						+ playerResult.getGang().getValue() + playerResult.getPao().getTotalscore();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
			});
			dianpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
			dianpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			dianpaoMajiangPanResult.setPanPlayerResultList(playerResultList);
			dianpaoMajiangPanResult.setHu(false);
			return dianpaoMajiangPanResult;
		}
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

	public boolean isQuzhongfabai() {
		return quzhongfabai;
	}

	public void setQuzhongfabai(boolean quzhongfabai) {
		this.quzhongfabai = quzhongfabai;
	}

	public int getNiaoshu() {
		return niaoshu;
	}

	public void setNiaoshu(int niaoshu) {
		this.niaoshu = niaoshu;
	}

}
