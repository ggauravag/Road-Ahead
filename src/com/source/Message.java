package com.source;

public class Message {
	private String title;
	private int id;
	private String message;
	
	
	public Message()
	{
		
	}
	@Override
	public String toString()
	{
		return title + " - "+ time;
	}
	
	public Message(int id,String title,  String message, String time) {
		
		this.title = title;
		this.id = id;
		this.message = message;
		this.time = time;
	}
	private String time;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	

}
