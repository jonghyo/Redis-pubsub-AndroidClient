package com.example.redispubsub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class PubFragment extends Fragment {
	
	private EditText channel;
	private EditText emessage;
	private EditText ipbox;
	private TextView text;
	private Button sendbutton;
	
	
	/*UDP通信する際に使用*/
	private DatagramSocket sendSocket;
	private DatagramPacket pkt;
	private byte[] buf;
	
	/*直接Redis Serverにアクセスする際に使用*/
	//private JedisPool pool;
	//private Jedis publisherJedis;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//レイアウトファイルからViewを作って
		View view = inflater.inflate(R.layout.pub_fragment, container, false);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		
		
		
		/*UDP接続用のSocketを生成*/
		try{
			sendSocket = new DatagramSocket();
			}catch(Exception e){
				e.printStackTrace();
			}
		
		/*Redis Serverに直接アクセスする際にServerとの接続を確立*/
		//pool = new JedisPool(new JedisPoolConfig(), "192.168.3.7");
		//subscriberJedis = pool.getResource();
		//publisherJedis = pool.getResource();
		
		text = (TextView)view.findViewById(R.id.data);
		channel = (EditText)view.findViewById(R.id.channel);
		emessage = (EditText)view.findViewById(R.id.message);
	    ipbox = (EditText)view.findViewById(R.id.pubip);
		sendbutton = (Button)view.findViewById(R.id.sendbutton);
		
		//ボタンのクリックリスナー
		sendbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String CHANNEL_NAME = channel.getText().toString();
				String message = emessage.getText().toString();
				String ip = ipbox.getText().toString();
				String unit = CHANNEL_NAME + "," + message;
				buf = unit.getBytes();
				
				
				/*送信パケットを生成し送信*/
				try{
					pkt = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip),5000);
					sendSocket.send(pkt);
					}catch(Exception e){
						e.printStackTrace();
					}
				
				
				//new Publisher(publisherJedis, CHANNEL_NAME, message).start();
				
				//pool.returnResource(publisherJedis);
				
			}
		});
		
		return view;
	}
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		//pool.destroy();
	}

}
