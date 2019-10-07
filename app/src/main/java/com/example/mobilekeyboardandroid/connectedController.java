package com.example.mobilekeyboardandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;


import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Process;
import java.nio.Buffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class connectedController extends Activity {

	private final int connectionPort = 8484;
    private String serverIP;
	private Socket connectionSocket;
	private PrintWriter cBufferOut;
	private Thread connectToServer;
	private AtomicBoolean newMsg = new AtomicBoolean(false);
	private AtomicReference<String> msg = new AtomicReference<>("TEST");


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);
        serverIP = getIntent().getStringExtra("Server");
		newMsg = new AtomicBoolean(false);
		connectToServer = new Thread(new serverHandler(serverIP, newMsg, msg));
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Log.v("SIP", "IP: " + serverIP);

        connectToServer.start();

		Button send = findViewById(R.id.button);

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sendText();
			}
		});


	}


	public void sendText(){

		EditText et = findViewById(R.id.cInput);
		String input = et.getText().toString();
		et.getText().clear();
		msg.set(input);
		newMsg.set(true);
		TextView mTextV = findViewById(R.id.textView2);
		String rest = mTextV.getText().toString();
		mTextV.clearComposingText();
		mTextV.setText("\n");
		mTextV.append(input + "\n" + rest);

	}
}
