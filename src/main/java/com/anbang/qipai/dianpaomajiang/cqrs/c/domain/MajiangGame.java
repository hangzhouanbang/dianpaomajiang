package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.listener.DianpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.finish.FixedPanNumbersJuFinishiDeterminer;
import com.dml.majiang.ju.firstpan.ClassicStartFirstPanProcess;
import com.dml.majiang.ju.nextpan.ClassicStartNextPanProcess;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.guipai.RandomGuipaiDeterminer;
import com.dml.majiang.pan.publicwaitingplayer.WaitDaPlayerPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.player.action.chi.PengganghuFirstChiActionProcessor;
import com.dml.majiang.player.action.da.DachushoupaiDaActionProcessor;
import com.dml.majiang.player.action.gang.HuFirstBuGangActionProcessor;
import com.dml.majiang.player.action.guo.DoNothingGuoActionProcessor;
import com.dml.majiang.player.action.hu.PlayerHuAndClearAllActionHuActionUpdater;
import com.dml.majiang.player.action.initial.ZhuangMoPaiInitialActionUpdater;
import com.dml.majiang.player.action.listener.comprehensive.GuoHuBuHuStatisticsListener;
import com.dml.majiang.player.action.listener.comprehensive.GuoPengBuPengStatisticsListener;
import com.dml.majiang.player.action.listener.comprehensive.TianHuAndDihuOpportunityDetector;
import com.dml.majiang.player.action.listener.gang.GuoGangBuGangStatisticsListener;
import com.dml.majiang.player.action.peng.HuFirstBuPengActionProcessor;
import com.dml.majiang.player.menfeng.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.player.shoupai.gouxing.NoDanpaiOneDuiziGouXingPanHu;
import com.dml.majiang.player.zhuang.MenFengDongZhuangDeterminer;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;

public class MajiangGame extends FixedPlayersMultipanAndVotetofinishGame {
	private boolean dianpao;
	private boolean dapao;
	private boolean quzhongfabai;
	private boolean zhuaniao;
	private boolean qingyise;
	private int niaoshu;
	private int panshu;
	private int renshu;
	private Ju ju;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();

	public PanActionFrame createJuAndStartFirstPan(long currentTime) throws Exception {
		ju = new Ju();
		ju.setStartFirstPanProcess(new ClassicStartFirstPanProcess());
		ju.setStartNextPanProcess(new ClassicStartNextPanProcess());
		ju.setPlayersMenFengDeterminerForFirstPan(new RandomMustHasDongPlayersMenFengDeterminer(currentTime));
		ju.setPlayersMenFengDeterminerForNextPan(new HuPlayerDongFengPlayersMenFengDeterminer());
		ju.setZhuangDeterminerForFirstPan(new MenFengDongZhuangDeterminer());
		ju.setZhuangDeterminerForNextPan(new MenFengDongZhuangDeterminer());

		ju.setAvaliablePaiFiller(new QuZhongFaRandomAvaliablePaiFiller(currentTime + 2, quzhongfabai));
		ju.setGuipaiDeterminer(new RandomGuipaiDeterminer(currentTime + 3));
		ju.setFaPaiStrategy(new DianpaoMajiangFaPaiStrategy(16));

		ju.setCurrentPanFinishiDeterminer(new DianpaoMajiangPanFinishiDeterminer());
		ju.setGouXingPanHu(new NoDanpaiOneDuiziGouXingPanHu());
		ju.setCurrentPanPublicWaitingPlayerDeterminer(new WaitDaPlayerPanPublicWaitingPlayerDeterminer());

		DianpaoMajiangPanResultBuilder dianpaoMajiangPanResultBuilder = new DianpaoMajiangPanResultBuilder();
		dianpaoMajiangPanResultBuilder.setDapao(dapao);
		dianpaoMajiangPanResultBuilder.setDianpao(dianpao);
		dianpaoMajiangPanResultBuilder.setQuzhongfabai(quzhongfabai);
		dianpaoMajiangPanResultBuilder.setZhuaniao(zhuaniao);
		dianpaoMajiangPanResultBuilder.setNiaoshu(niaoshu);
		dianpaoMajiangPanResultBuilder.setQingyise(qingyise);
		ju.setCurrentPanResultBuilder(dianpaoMajiangPanResultBuilder);

		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer(panshu));
		ju.setJuResultBuilder(new DianpaoMajiangJuResultBuilder());
		ju.setInitialActionUpdater(new ZhuangMoPaiInitialActionUpdater());
		ju.setMoActionProcessor(new DianpaoMajiangMoActionProcessor());
		ju.setMoActionUpdater(new DianpaoMajiangMoActionUpdater());
		ju.setDaActionProcessor(new DachushoupaiDaActionProcessor());
		DianpaoMajiangDaActionUpdater daUpdater = new DianpaoMajiangDaActionUpdater();
		daUpdater.setDianpao(dianpao);
		ju.setDaActionUpdater(daUpdater);
		ju.setChiActionProcessor(new PengganghuFirstChiActionProcessor());
		ju.setChiActionUpdater(new DianpaoMajiangChiActionUpdater());
		ju.setPengActionProcessor(new HuFirstBuPengActionProcessor());
		ju.setPengActionUpdater(new DianpaoMajiangPengActionUpdater());
		ju.setGangActionProcessor(new HuFirstBuGangActionProcessor());
		ju.setGangActionUpdater(new DianpaoMajiangGangActionUpdater());
		ju.setGuoActionProcessor(new DoNothingGuoActionProcessor());
		ju.setGuoActionUpdater(new DianpaoMajiangGuoActionUpdater());
		ju.setHuActionProcessor(new DianpaoMajiangHuActionProcessor());
		ju.setHuActionUpdater(new PlayerHuAndClearAllActionHuActionUpdater());

		ju.addActionStatisticsListener(new DianpaoMajiangPengGangActionStatisticsListener());
		ju.addActionStatisticsListener(new DianpaoMajiangLastMoActionPlayerRecorder());
		ju.addActionStatisticsListener(new TianHuAndDihuOpportunityDetector());
		ju.addActionStatisticsListener(new GuoHuBuHuStatisticsListener());
		ju.addActionStatisticsListener(new GuoPengBuPengStatisticsListener());
		ju.addActionStatisticsListener(new GuoGangBuGangStatisticsListener());

		// 开始第一盘
		ju.startFirstPan(allPlayerIds());

		// 必然庄家已经先摸了一张牌了
		return ju.getCurrentPan().findLatestActionFrame();
	}

	public MajiangActionResult action(String playerId, int actionId, int actionNo, long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.action(playerId, actionId, actionNo, actionTime);
		MajiangActionResult result = new MajiangActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		checkAndFinishPan();

		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			DianpaoMajiangPanResult panResult = (DianpaoMajiangPanResult) ju.findLatestFinishedPanResult();
			for (DianpaoMajiangPanPlayerResult dianpaoMajiangPanPlayerResult : panResult.getPanPlayerResultList()) {
				playeTotalScoreMap.put(dianpaoMajiangPanPlayerResult.getPlayerId(),
						dianpaoMajiangPanPlayerResult.getTotalScore());
			}
			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((DianpaoMajiangJuResult) ju.getJuResult());
			}
		}
		result.setMajiangGame(new MajiangGameValueObject(this));
		return result;
	}

	@Override
	public void finish() throws Exception {
		if (ju != null) {
			ju.finish();
		}
	}

	@Override
	protected boolean checkToFinishGame() throws Exception {
		return ju.getJuResult() != null;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		return ju.getCurrentPan() == null;
	}

	@Override
	protected void startNextPan() throws Exception {
		ju.startNextPan();
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToExtendedVotingState() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToVoteNotPassStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(long currentTime) throws Exception {
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	public MajiangGameValueObject toValueObject() {
		return new MajiangGameValueObject(this);
	}

	public int getNiaoshu() {
		return niaoshu;
	}

	public boolean isQingyise() {
		return qingyise;
	}

	public void setQingyise(boolean qingyise) {
		this.qingyise = qingyise;
	}

	public void setNiaoshu(int niaoshu) {
		this.niaoshu = niaoshu;
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public void setDianpao(boolean dianpao) {
		this.dianpao = dianpao;
	}

	public boolean isQuzhongfabai() {
		return quzhongfabai;
	}

	public void setQuzhongfabai(boolean quzhongfabai) {
		this.quzhongfabai = quzhongfabai;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public boolean isZhuaniao() {
		return zhuaniao;
	}

	public void setZhuaniao(boolean zhuaniao) {
		this.zhuaniao = zhuaniao;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public Ju getJu() {
		return ju;
	}

	public void setJu(Ju ju) {
		this.ju = ju;
	}

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

}
