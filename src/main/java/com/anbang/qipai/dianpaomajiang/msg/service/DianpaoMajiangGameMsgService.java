package com.anbang.qipai.dianpaomajiang.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.dianpaomajiang.msg.channel.DianpaoMajiangGameSource;
import com.anbang.qipai.dianpaomajiang.msg.msjobj.CommonMO;
import com.dml.majiang.pan.frame.PanValueObject;

@EnableBinding(DianpaoMajiangGameSource.class)
public class DianpaoMajiangGameMsgService {

	@Autowired
	private DianpaoMajiangGameSource dianpaoMajiangGameSource;

	public void gamePlayerLeave(MajiangGameValueObject majiangGameValueObject, String playerId) {
		boolean playerIsQuit = true;
		for (String pid : majiangGameValueObject.allPlayerIds()) {
			if (pid.equals(playerId)) {
				playerIsQuit = false;
				break;
			}
		}
		if (playerIsQuit) {
			CommonMO mo = new CommonMO();
			mo.setMsg("playerQuit");
			Map data = new HashMap();
			data.put("gameId", majiangGameValueObject.getId());
			data.put("playerId", playerId);
			mo.setData(data);
			dianpaoMajiangGameSource.dianpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
		}
	}

	public void gameFinished(String gameId) {
		CommonMO mo = new CommonMO();
		mo.setMsg("ju finished");
		Map data = new HashMap();
		data.put("gameId", gameId);
		mo.setData(data);
		dianpaoMajiangGameSource.dianpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}

	public void panFinished(MajiangGameValueObject majiangGameValueObject, PanValueObject panAfterAction) {
		CommonMO mo = new CommonMO();
		mo.setMsg("pan finished");
		Map data = new HashMap();
		data.put("gameId", majiangGameValueObject.getId());
		data.put("no", panAfterAction.getNo());
		data.put("playerIds", majiangGameValueObject.allPlayerIds());
		mo.setData(data);
		dianpaoMajiangGameSource.dianpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}
}
