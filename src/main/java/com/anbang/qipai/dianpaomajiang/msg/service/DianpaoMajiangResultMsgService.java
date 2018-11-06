package com.anbang.qipai.dianpaomajiang.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.dianpaomajiang.msg.channel.DianpaoMajiangResultSource;
import com.anbang.qipai.dianpaomajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.dianpaomajiang.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.dianpaomajiang.msg.msjobj.MajiangHistoricalPanResult;

@EnableBinding(DianpaoMajiangResultSource.class)
public class DianpaoMajiangResultMsgService {

	@Autowired
	private DianpaoMajiangResultSource dianpaoMajiangResultSource;

	public void recordJuResult(MajiangHistoricalJuResult juResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("dianpaomajiang ju result");
		mo.setData(juResult);
		dianpaoMajiangResultSource.dianpaoMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}

	public void recordPanResult(MajiangHistoricalPanResult panResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("dianpaomajiang pan result");
		mo.setData(panResult);
		dianpaoMajiangResultSource.dianpaoMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}
}
