package com.mobilekeyboard.auth;

import java.io.Serializable;

public class MsgPacket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _user;
	private int _size;
	private String _msg;
	
	public MsgPacket() {
		_user = "";
		_size = -1;
		_msg = "";
	}
	
	public void set_msg(String usr, int size, String msg){
		_user = usr;
		_size = size;
		_msg = msg;
	}
	
	public String get_msg() { return _msg;}
	
	public int get_size() { return _size;}
	
	public String get_user() { return _user;}

}
