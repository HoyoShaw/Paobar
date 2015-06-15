package com.hoyo.paobar.service.po;

import java.io.Serializable;

/**
 * @author Hoyo
 *
 */
public class BarInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TPC = "tpc";
	public static final String PICURL = "picurl";
	public static final String AVGCOST = "avgcost";
	public static final String OPENTIME = "opentime";
	public static final String TELNUM = "telnum";
	public static final String ADDRESS = "address";
	public static final String STAR = "star";
	public static final String ICNT = "icnt";
	public static final String TAG = "tag";
	public static final String CITY = "city";
	public static final String PCNT = "pcnt";
	public static final String PICNUM = "picnum";
	public static final String LNG = "lng";
	public static final String LAT = "lat";
	public static final String DIS = "dis";
	
	
	
	private int id;
	private String name;
	private String tpc;
	private String picurl;
	private String avgcost;
	private String opentime;
	private String telnum;
	private String address;
	private int star;
	private int icnt;
	private String tag;
	private String city;
	private int pcnt;
	private int picnum;
	private String lng;
	private String lat;
	private long dis;
	private UserInfo user;
	
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getTpc() {
		return tpc;
	}



	public void setTpc(String tpc) {
		this.tpc = tpc;
	}



	public String getPicurl() {
		return picurl;
	}



	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}



	public String getAvgcost() {
		return avgcost;
	}



	public void setAvgcost(String avgcost) {
		this.avgcost = avgcost;
	}



	public String getOpentime() {
		return opentime;
	}



	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}



	public String getTelnum() {
		return telnum;
	}



	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public int getStar() {
		return star;
	}



	public void setStar(int star) {
		this.star = star;
	}



	public int getIcnt() {
		return icnt;
	}



	public void setIcnt(int icnt) {
		this.icnt = icnt;
	}



	public String getTag() {
		return tag;
	}



	public void setTag(String tag) {
		this.tag = tag;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public int getPcnt() {
		return pcnt;
	}



	public void setPcnt(int pcnt) {
		this.pcnt = pcnt;
	}



	public int getPicnum() {
		return picnum;
	}



	public void setPicnum(int picnum) {
		this.picnum = picnum;
	}



	public String getLng() {
		return lng;
	}



	public void setLng(String lng) {
		this.lng = lng;
	}



	public String getLat() {
		return lat;
	}



	public void setLat(String lat) {
		this.lat = lat;
	}



	public long getDis() {
		return dis;
	}



	public void setDis(long dis) {
		this.dis = dis;
	}



	public UserInfo getUser() {
		return user;
	}



	public void setUser(UserInfo user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "BarInfo [id=" + id + ", name=" + name + ", tpc=" + tpc
				+ ", picurl=" + picurl + ", avgcost=" + avgcost + ", opentime="
				+ opentime + ", telnum=" + telnum + ", address=" + address
				+ ", star=" + star + ", icnt=" + icnt + ", tag=" + tag
				+ ", city=" + city + ", pcnt=" + pcnt + ", picnum=" + picnum
				+ ", lng=" + lng + ", lat=" + lat + ", dis=" + dis + ", user="
				+ user + "]";
	}
	
}
