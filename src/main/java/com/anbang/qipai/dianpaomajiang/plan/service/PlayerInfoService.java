package com.anbang.qipai.dianpaomajiang.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.dianpaomajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.dianpaomajiang.plan.dao.PlayerInfoDao;

@Service
public class PlayerInfoService {

	@Autowired
	private PlayerInfoDao playerInfoDao;

	public PlayerInfo findPlayerInfoById(String playerId) {
		return playerInfoDao.findById(playerId);
	}

	public void save(PlayerInfo playerInfo) {
		playerInfoDao.save(playerInfo);
	}

	public void updateVip(String playerId, boolean vip) {
		playerInfoDao.updateVip(playerId, vip);
	}
}
