package com.example.redispubsub;


import redis.clients.jedis.Jedis;

public class Publisher {
	
	private final Jedis publisherJedis;
	private String channel;
	private String msg;
	
	public Publisher(Jedis publisherJedis, String channel, String message){
		this.publisherJedis = publisherJedis;
		this.channel = channel;
		this.msg = message;
	}
	
	public void start(){
			String message = msg;
			publisherJedis.publish(channel, message);
	}
}
