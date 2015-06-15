package com.hoyo.paobar.service.protocol;

public class PaobarProtocol{
	
	
	/** �����������ַ*/
	public static final String BASE_URL = "http://182.92.156.49:8080/server.aspx";
	
	public static final int RESULT_OK = 0;

	public static final int RESULT_ERROR = -1;
	
	public static final String KEY_RESULT = "result";

	public static final String KEY_DATA = "data";
	
	public static final String KEY_ERRNO = "errno";
	
	public static final String KEY_ERRMSG = "errmsg";
	
	public static final String KEY_UDID = "udid";
	
	public static final String KEY_KEY = "key";

	public static final String KEY_TYP = "typ";

	
	/** ҵ������***/
	
	public static final String SERVICE_TYPE_GETBARLIST = "getbarList";
	
	public static final String SERVICE_TYPE_GETBARINFO = "getbarInfo";

	public static final String SERVICE_TYPE_GETBARPIC = "getbarPic";

	public static final String SERVICE_TYPE_GETBARPL = "getbarPL";
	
	public static final String SERVICE_TYPE_USEREGISTE = "userRegiste";
	
	public static final String SERVICE_TYPE_USERVERI = "userVeri";

	public static final String SERVICE_TYPE_USERLOGIN = "userLogin";
	
	public static final String SERVICE_TYPE_ADDBARPL = "addbarPL";
	
	public static final String SERVICE_TYPE_ADDBARZAN = "addbarZan";
	
	public static final String SERVICE_TYPE_ADDBARPIC = "addbarPic";
	
	public static final String SERVICE_TYPE_USERREGISTERETRY = "userRegisteReTry";
	
	public static final String SERVICE_TYPE_GETVERSION = "getVersion";
	
	public static final String SERVICE_TYPE_GETUSER = "getUser";
	
	public static final String SERVICE_TYPE_EDITUSER = "editUser";
	
	public static final String SERVICE_TYPE_GETBARSALER = "getbarSaler";
	
	
	public interface BarList{
		public static final String KEY_CITY = "city";
		public static final String KEY_GPS = "gps";
		public static final String KEY_DIS = "dis";
		public static final String KEY_PSIZE = "psize";
		public static final String KEY_PINDEX = "pindex";
		public static final String KEY_SORTTYPE = "sortType";
	}
	
	
}
