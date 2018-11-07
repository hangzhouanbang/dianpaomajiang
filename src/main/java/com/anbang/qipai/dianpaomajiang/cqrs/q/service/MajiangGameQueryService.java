package com.anbang.qipai.dianpaomajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.DianpaoMajiangJuResult;
import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.dianpaomajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.dianpaomajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

@Component
public class MajiangGameQueryService {

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GameFinishVoteDboDao gameFinishVoteDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	public MajiangGameDbo findMajiangGameDboById(String gameId) {
		return majiangGameDboDao.findById(gameId);
	}

	public void newMajiangGame(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);
	}

	public void backToGame(String playerId, MajiangGameValueObject majiangGameValueObject) {
		majiangGameDboDao.updatePlayerOnlineState(majiangGameValueObject.getId(), playerId,
				majiangGameValueObject.findPlayerOnlineState(playerId));
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		gameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
	}

	public void joinGame(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);
	}

	public void leaveGame(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(majiangGame.getId());
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGame.getVote();
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
		gameFinishVoteDbo.setGameId(majiangGame.getId());
		gameFinishVoteDboDao.save(gameFinishVoteDbo);

		DianpaoMajiangJuResult dianpaoMajiangJuResult = (DianpaoMajiangJuResult) majiangGame.getJuResult();
		if (dianpaoMajiangJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(majiangGame.getId(), null, dianpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}

	}

	public void finish(MajiangGameValueObject majiangGameValueObject) {
		gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(majiangGameValueObject.getId());
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
		gameFinishVoteDbo.setGameId(majiangGameValueObject.getId());
		gameFinishVoteDboDao.save(gameFinishVoteDbo);

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		DianpaoMajiangJuResult dianpaoMajiangJuResult = (DianpaoMajiangJuResult) majiangGameValueObject.getJuResult();
		if (dianpaoMajiangJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, dianpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public void voteToFinish(MajiangGameValueObject majiangGameValueObject) {
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		gameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		DianpaoMajiangJuResult dianpaoMajiangJuResult = (DianpaoMajiangJuResult) majiangGameValueObject.getJuResult();
		if (dianpaoMajiangJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, dianpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public GameFinishVoteDbo findGameFinishVoteDbo(String gameId) {
		return gameFinishVoteDboDao.findByGameId(gameId);
	}
}
