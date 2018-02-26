package com.sam;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

@EnableBinding(Source.class)
public class TimerSource {

	@InboundChannelAdapter(value=Source.OUTPUT, poller=@Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
	public ChatMessage timerMessageSource() {
		return new ChatMessage("Hello World!", System.currentTimeMillis());
	}

}
