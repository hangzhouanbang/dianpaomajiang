package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.ju.result.JuResultBuilder;
import com.dml.majiang.pan.result.PanResult;

public class DianpaoMajiangJuResultBuilder implements JuResultBuilder {

	@Override
	public JuResult buildJuResult(Ju ju) {
		DianpaoMajiangJuResult dianpaoMajiangJuResult = new DianpaoMajiangJuResult();
		dianpaoMajiangJuResult.setFinishedPanCount(ju.countFinishedPan());
		if (ju.countFinishedPan() > 0) {
			Map<String, DianpaoMajiangJuPlayerResult> juPlayerResultMap = new HashMap<>();
			for (PanResult panResult : ju.getFinishedPanResultList()) {
				DianpaoMajiangPanResult dianpaoMajiangPanResult = (DianpaoMajiangPanResult) panResult;
				for (DianpaoMajiangPanPlayerResult panPlayerResult : dianpaoMajiangPanResult.getPanPlayerResultList()) {
					DianpaoMajiangJuPlayerResult juPlayerResult = juPlayerResultMap.get(panPlayerResult.getPlayerId());
					if (juPlayerResult == null) {
						juPlayerResult = new DianpaoMajiangJuPlayerResult();
						juPlayerResult.setPlayerId(panPlayerResult.getPlayerId());
						juPlayerResultMap.put(panPlayerResult.getPlayerId(), juPlayerResult);
					}
					if (dianpaoMajiangPanResult.ifPlayerHu(panPlayerResult.getPlayerId())) {
						juPlayerResult.increaseHuCount();
					}
					juPlayerResult.increaseCaishenCount(
							dianpaoMajiangPanResult.playerGuipaiCount(panPlayerResult.getPlayerId()));
					if (dianpaoMajiangPanResult.ifPlayerHu(panPlayerResult.getPlayerId())
							&& dianpaoMajiangPanResult.isZimo()) {
						juPlayerResult.increaseZiMoCount();
					}
					String dianPaoPlayerId = dianpaoMajiangPanResult.getDianpaoPlayerId();
					if (dianPaoPlayerId != null && dianPaoPlayerId.equals(panPlayerResult.getPlayerId())) {
						juPlayerResult.increaseFangPaoCount();
					}
					juPlayerResult.setTotalScore(panPlayerResult.getTotalScore());
				}
			}

			DianpaoMajiangJuPlayerResult dayingjia = null;
			DianpaoMajiangJuPlayerResult datuhao = null;
			for (DianpaoMajiangJuPlayerResult juPlayerResult : juPlayerResultMap.values()) {
				if (dayingjia == null) {
					dayingjia = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() > dayingjia.getTotalScore()) {
						dayingjia = juPlayerResult;
					}
				}

				if (datuhao == null) {
					datuhao = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() < datuhao.getTotalScore()) {
						datuhao = juPlayerResult;
					}
				}
			}
			dianpaoMajiangJuResult.setDatuhaoId(datuhao.getPlayerId());
			dianpaoMajiangJuResult.setDayingjiaId(dayingjia.getPlayerId());
			dianpaoMajiangJuResult.setPlayerResultList(new ArrayList<>(juPlayerResultMap.values()));
		}
		return dianpaoMajiangJuResult;
	}

}
