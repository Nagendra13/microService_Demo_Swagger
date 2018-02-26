package com.sam;

public class ChatMessage {

	private final String contents;
	private final long time;

	public ChatMessage(String contents, long time) {
		super();
		this.contents = contents;
		this.time = time;
	}

	public String getContents() {
		return contents;
	}

	public long getTime() {
		return time;
	}

}
