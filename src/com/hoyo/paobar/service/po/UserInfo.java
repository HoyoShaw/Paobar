package com.hoyo.paobar.service.po;

import java.io.Serializable;

/**
 * @author lenovo
 *
 */
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String USERIMG = "userimg";
	public static final String NICK = "nick";
	public static final String HPIC = "hpic";
	
	private String nick;
	private String hpic;
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getHpic() {
		return hpic;
	}
	public void setHpic(String hpic) {
		this.hpic = hpic;
	}
	@Override
	public String toString() {
		return "UserInfo [nick=" + nick + ", hpic=" + hpic + "]";
	}
}
