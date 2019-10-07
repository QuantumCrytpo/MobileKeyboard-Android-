package com.example.mobilekeyboardandroid;

import android.util.Log;

import com.mobilekeyboard.auth.MsgPacket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class BroadcastDiscovery {

    private ByteArrayOutputStream bStream;
    private ObjectOutputStream oos;
    private ObjectInputStream broadcastReply;
    private String msg = "MSG";
    private int discoverPort = 8888;
    private MsgPacket discoveryPacket;
    private DatagramSocket csSocket;


    public BroadcastDiscovery() throws SocketException {
        discoveryPacket = new MsgPacket();
        discoveryPacket.set_msg("Client", 10, "Looking for IP");
   }


    public String discoverServer(){
        bStream = new ByteArrayOutputStream();
        boolean connected = false;
        String serverIP = "0.0.0";

        try{
            oos = new ObjectOutputStream(bStream);
            oos.writeObject(discoveryPacket);
            oos.close();

            byte[] pk = bStream.toByteArray();
            csSocket = new DatagramSocket();
            csSocket.setBroadcast(true);

            try{
                DatagramPacket sendP = new DatagramPacket(pk, pk.length, InetAddress.getByName("255.255.255.255"), 8888);
                csSocket.send(sendP);
                connected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Iterate network interfaces to send broadcast.
            Enumeration<NetworkInterface> net_i = NetworkInterface.getNetworkInterfaces();

            while(net_i.hasMoreElements() && !connected){
                Log.v("NI", "Trying network interface.");
                NetworkInterface inter = net_i.nextElement();

                if(inter.isLoopback() || !inter.isUp()){
                    continue;
                }

                for(InterfaceAddress i_a: inter.getInterfaceAddresses()){
                    InetAddress broadcast = i_a.getBroadcast();
                    if(broadcast == null){
                        continue;
                    }

                    try{
                        DatagramPacket sendP = new DatagramPacket(pk, pk.length, broadcast, 8888);
                        csSocket.send(sendP);
                        connected = true;
                    } catch (Exception e){

                    }
                }

            }

            // Move to better place
            byte[] rcvBuf = new byte[15000];
            DatagramPacket packet = new DatagramPacket(rcvBuf, rcvBuf.length);
            csSocket.receive(packet);
            broadcastReply =  new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            MsgPacket msg = (MsgPacket) broadcastReply.readObject();
            broadcastReply.close();
            Log.v("RPL","Received: " + msg.get_msg() + ":" + msg.get_user());
            serverIP = msg.get_msg();




            Log.v("CLOSING", "Socket has been closed");
            csSocket.close();

        } catch (Exception e){

        }
        return serverIP;
    }

    private void receiveServer(){

    }

}
