package com.anbang.qipai.dianpaomajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GameRoomSink {

	String DIANPAOGAMEROOM = "dianpaoGameRoom";

	@Input
	SubscribableChannel dianpaoGameRoom();
}
