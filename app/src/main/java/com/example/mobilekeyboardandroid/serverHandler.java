package com.example.mobilekeyboardandroid;

import android.util.Log;

import com.mobilekeyboard.auth.MsgPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class serverHandler implements Runnable {

    private String serverIP;
    private Socket serverConnection;
    private final int connectionPort = 8484;
    private PrintWriter cBufferOut;
    private AtomicBoolean sendNew;
    private AtomicReference<String> outMsg;


    public serverHandler(String ip, AtomicBoolean newMsg, AtomicReference<String> newOutMsg){
        this.serverIP = ip;
        this.sendNew = newMsg;
        this.outMsg = newOutMsg;
    }


    public void commandOut(String command){
        try {
            /*cBufferOut = new PrintWriter(new OutputStreamWriter(serverConnection.getOutputStream()), true);
            if(cBufferOut != null){
                Log.v("CSend", "Sending command...");
                cBufferOut.println(command);
                cBufferOut.close();
                sendNew.set(false);
                }*/
            MsgPacket out = new MsgPacket();
            out.set_msg("DBG", command.length(), command);
            ObjectOutputStream oos = new ObjectOutputStream(serverConnection.getOutputStream());

            oos.writeObject(out);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void test_connection(){
        MsgPacket out = new MsgPacket();
        out.set_msg("Client", 15, "Connection Successful");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(serverConnection.getOutputStream());

                oos.writeObject(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public void run(){
        try {
            serverConnection = new Socket(InetAddress.getByName(serverIP), connectionPort);
            this.test_connection();

            while(true){
                if(this.sendNew.get()) {
                    Log.v("DBG", "NEW MESSAGE");
                    String outMessage = this.outMsg.get().toString();
                    this.commandOut(outMessage);
                    this.sendNew.set(false);
                } else if(this.outMsg.get().toString().equals("OUT")){
                    break;
                }
            }

        } catch (Exception e) {
            Log.v("ERR", "******* ERROR *******");
            e.printStackTrace();
        }
    }


}
