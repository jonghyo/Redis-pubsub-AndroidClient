package com.example.redispubsub;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SubFragment extends Fragment implements OnCheckedChangeListener {

	private Handler mHandler = new Handler();

	private ToggleButton subbutton;
	private EditText edit;
	private EditText ipedit;
	private TextView text;

	/* UDP通信する際に使用 */
	private DatagramSocket udpSocket;
	private DatagramPacket pkt;
	private byte[] buf;
	private byte[] resbuf = new byte[256];

	private String msg;
	private DatagramPacket pack;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());

		// レイアウトファイルからViewを作って
		View view = inflater.inflate(R.layout.sub_fragment, container, false);

		subbutton = (ToggleButton) view.findViewById(R.id.sub_button);
		
		
		
		edit = (EditText) view.findViewById(R.id.channel);
		ipedit = (EditText)view.findViewById(R.id.subip);
		text = (TextView) view.findViewById(R.id.recievedata);
		


		subbutton.setOnCheckedChangeListener(this);

		return view;

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//subbuttonが押された場合
		if(isChecked == true){
			subscribe();
		}else{
			unsubscribe();
		}
	}

	public void subscribe() {
		String CHANNEL_NAME = edit.getText().toString();
		String ip = ipedit.getText().toString();
		buf = CHANNEL_NAME.getBytes();
		pack = new DatagramPacket(resbuf, resbuf.length);

		/* 送信パケットを生成し送信 */
		try {
			udpSocket = new DatagramSocket();
			pkt = new DatagramPacket(buf, buf.length,
					InetAddress.getByName(ip), 5002);
			udpSocket.send(pkt);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// スレッド起動
		(new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					int i = 0;
					
					while (i != 1){
					udpSocket.receive(pack);
					int len = pack.getLength();
					msg = new String(resbuf, 0, len);
					
					/**
					 * Handlerのpostメソッドを使ってUIスレッドに処理をDispatchする
					 */
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							CharSequence s = text.getText();
							text.setText(s + "\n" + msg);
						}
					});
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				
				
			}

		})).start();
	}

	public void unsubscribe() {
		buf = "unsub".getBytes();
		try{
			pkt = new DatagramPacket(buf, buf.length,
					InetAddress.getByName("192.168.0.105"), 5002);
			udpSocket.send(pkt);
		}catch(IOException e){
			e.printStackTrace();
		}
		udpSocket.close();
	}
}
