package com.example.mobilekeyboardandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import java.net.SocketException;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
		Button nextAct = (Button) findViewById(R.id.connectPC);
		nextAct.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){

				connect();
			}
		});
	}

	public void connect(){
		try {
			BroadcastDiscovery discover = new BroadcastDiscovery();
			String sIp = discover.discoverServer();
			if(sIp.length() > 8){
                Intent it = new Intent(getApplicationContext(), connectedController.class);
                it.putExtra("Server", sIp);
                startActivity(it);
            }
		} catch (SocketException e){
			return;
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
