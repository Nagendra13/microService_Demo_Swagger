package com.sam;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(Processor.class)
@Configuration
public class MessageFilter {

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public ChatMessage transform(ChatMessage chatMessage) {
		final String contents = chatMessage.getContents().toUpperCase() + "!";
		return new ChatMessage(contents, chatMessage.getTime());
	}

}
