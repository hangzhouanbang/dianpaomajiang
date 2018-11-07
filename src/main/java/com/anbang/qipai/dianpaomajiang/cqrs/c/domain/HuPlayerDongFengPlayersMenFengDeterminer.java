package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.Hu;
import com.dml.majiang.player.menfeng.PlayersMenFengDeterminer;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;

/**
 * 胡牌者做庄家，一炮多响时，点炮的人继续做庄家先出牌，流局庄家不变
 * 
 * @author lsc
 *
 */
public class HuPlayerDongFengPlayersMenFengDeterminer implements PlayersMenFengDeterminer {

	@Override
	public void determinePlayersMenFeng(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();
		List<MajiangPlayerValueObject> playerList = latestFinishedPanResult.getPan().getPlayerList();
		List<Hu> huList = new ArrayList<>();
		String huPlayerId = null;
		for (MajiangPlayerValueObject player : playerList) {
			if (player.getHu() != null) {
				huList.add(player.getHu());
				huPlayerId = player.getId();
			}
		}
		if (huList.size() == 1) {// 一人胡
			// 再计算要顺时针移几步到东
			MajiangPosition p = latestFinishedPanResult.playerMenFeng(huPlayerId);
			int n = 0;
			while (true) {
				MajiangPosition np = MajiangPositionUtil.nextPositionClockwise(p);
				n++;
				if (np.equals(MajiangPosition.dong)) {
					break;
				} else {
					p = np;
				}
			}
			// 最后给所有玩家设置门风
			List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
			for (String playerId : allPlayerIds) {
				MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
				MajiangPosition newPlayerMenFeng = playerMenFeng;
				for (int i = 0; i < n; i++) {
					newPlayerMenFeng = MajiangPositionUtil.nextPositionClockwise(newPlayerMenFeng);
				}
				currentPan.updatePlayerMenFeng(playerId, newPlayerMenFeng);
			}
		} else if (huList.size() > 1) {// 一炮多响
			// 再计算要顺时针移几步到东
			MajiangPosition p = latestFinishedPanResult.playerMenFeng(huList.get(0).getDianpaoPlayerId());
			int n = 0;
			while (true) {
				MajiangPosition np = MajiangPositionUtil.nextPositionClockwise(p);
				n++;
				if (np.equals(MajiangPosition.dong)) {
					break;
				} else {
					p = np;
				}
			}
			// 最后给所有玩家设置门风
			List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
			for (String playerId : allPlayerIds) {
				MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
				MajiangPosition newPlayerMenFeng = playerMenFeng;
				for (int i = 0; i < n; i++) {
					newPlayerMenFeng = MajiangPositionUtil.nextPositionClockwise(newPlayerMenFeng);
				}
				currentPan.updatePlayerMenFeng(playerId, newPlayerMenFeng);
			}
		} else {// 流局
			for (MajiangPlayerValueObject player : playerList) {
				MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(player.getId());
				currentPan.updatePlayerMenFeng(player.getId(), playerMenFeng);
			}
		}
	}

}
