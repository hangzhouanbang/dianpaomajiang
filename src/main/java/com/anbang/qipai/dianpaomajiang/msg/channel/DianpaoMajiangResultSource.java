package com.anbang.qipai.dianpaomajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface DianpaoMajiangResultSource {
	@Output
	MessageChannel dianpaoMajiangResult();
}
