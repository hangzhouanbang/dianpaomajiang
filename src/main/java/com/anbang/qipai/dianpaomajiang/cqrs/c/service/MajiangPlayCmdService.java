package com.anbang.qipai.dianpaomajiang.cqrs.c.service;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.ReadyToNextPanResult;

public interface MajiangPlayCmdService {

	MajiangActionResult action(String playerId, Integer actionId, Integer actionNo, Long actionTime) throws Exception;

	ReadyToNextPanResult readyToNextPan(String playerId) throws Exception;

}
