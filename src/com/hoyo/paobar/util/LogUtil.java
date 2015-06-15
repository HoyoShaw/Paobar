package com.hoyo.paobar.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * 异常处理器，方便查错使用，提供：
 *        1.初始化后自动保存应用未捕获的异常堆栈信息到SD卡文件
 *        2.手动记录异常堆栈信息到SD卡文件
 *        3.手动记录调试内容到SD卡文件
 *     SD卡文件路径:/SD卡/data/log/lcp.sdk.trace.txt
 */
public final class LogUtil {
	private static final String TAG = "LogUtil";
	private static final int LOG_FILE_SIZE = 1024 * 1024 * 2;//2M
	private static final String ROOT_PATH = "/data/log/";
	private static final String FATAL = "FATAL";//致命的错误，输出到文件
	private static final String ERROR = "ERROR";//错误，输出到文件
	private static final String WARN = "WARN";//潜在的错误，输出到文件
	private static final String INFO = "INFO";//关键信息，输出到文件
	//private static final String DEBUG = "DEBUG";//调试日志，只打印到控制台

	//private static Context context;
	private static String logFilename = "paobar.trace.txt";

	public static final String USER_LOG_KEY = "userlog";

	private LogUtil(){}

	/**
	 * 初始化LCP SDK的异常处理器
	 * 初始化完成后，应用程序运行过程中抛出的未捕获异常的堆栈信息将记录到SD卡文件
	 */
	public static void init(){
		Context ctx = ContextUtil.getContext();
		logFilename = ctx.getApplicationInfo().packageName + ".txt";

		final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Log.e(TAG, FATAL, throwable);
				traceThrowable(FATAL, throwable);
				defaultHandler.uncaughtException(thread, throwable);
			}
		});
	}

	/**
	 * 错误，输出到文件
	 * @param throwable
	 */
	public static void e(Throwable throwable){
		Log.e(TAG, ERROR, throwable);

		traceThrowable(ERROR, throwable);
	}

	/**
	 * 错误，输出到文件
	 * @param error
	 */
	public static void e(String error){
		Log.e(TAG, error);

		traceLog(ERROR, error);
	}

	/**
	 * 潜在的错误异常，输出到文件
	 * @param throwable
	 */
	public static void w(Throwable throwable){
		Log.w(TAG, throwable);

		traceThrowable(WARN, throwable);
	}

	/**
	 * 潜在的错误，输出到文件
	 * @param warn
	 */
	public static void w(String warn){
		Log.w(TAG, warn);

		traceLog(WARN, warn);
	}

	/**
	 * 关键信息，输出到文件
	 * @param info
	 */
	public static void i(String info){
		Log.i(TAG, info);

		traceLog(INFO, info);
	}

	/**
	 * 关键信息，输出到文件
	 * @param tag
	 * @param info
	 */
	public static void i(String tag, String info){
		Log.i(tag, info);

		traceLog(INFO, info);
	}

	/**
	 * debug信息暂时不写文件
	 * @param debug
	 */
	public static void d(String debug){
		Log.i(TAG, debug);

		//traceLog(DEBUG, debug);
	}

	public static void d(String tag, String debug){
		Log.i(tag, debug);

		//traceLog(DEBUG, debug);
	}
	/**
	 * 清理日志文件
	 */
	public static void clearUserLog(){
		ZipUtil.delFolder(getDefaultLogPath().getAbsolutePath());
		File z = new File(getUserLogZipTempUrl());
		if(z.exists()) {
            z.delete();
        }
	}

	/**
	 * 得到日志文件压缩包
	 * @return
	 * @throws Exception 压缩报错
	 */
	public static File getUserLogByZip() throws Exception{
		ZipUtil.createZip(getDefaultLogPath().getAbsolutePath(), getUserLogZipTempUrl());
		return new File(getUserLogZipTempUrl());
	}

	/**
	 * 得到日志文件压缩包路径
	 * @return
	 */
	private static String getUserLogZipTempUrl(){
		return Environment.getExternalStorageDirectory().getAbsolutePath()+"/data/"+"log.zip";
	}

	/**
	 * 记录日志字符串信息到日志文件
	 * @param logContent
	 */
	private static void traceLog(String label, String logContent) {
		if(TextUtils.isEmpty(logContent)){
			return ;
		}

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		PrintWriter writer = null;
		try{
			writer = getTraceFileWriter();
			if(null == writer){
				return ;
			}

			writer.write(label + " " + format.format(new Date()) + "\n");
			writer.write(logContent);
			writer.write("\n");
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			IOUtil.close(writer);
		}
	}

	/**
	 * 记录异常到日志文件
	 * @param label
	 * @param throwable
	 */
	private static void traceThrowable(String label, Throwable throwable) {
		if(null == throwable){
			return ;
		}

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		PrintWriter writer = null;
		try{
			writer = getTraceFileWriter();
			if(null == writer){
				return ;
			}

			//记录Throwable到日志文件
			writer.write(label + " " + format.format(new Date()) + "\n");
			throwable.printStackTrace(writer);
			writer.write("\n");
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtil.close(writer);
		}
	}

	/**
	 * 获取日志文件写入器
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static PrintWriter getTraceFileWriter() throws FileNotFoundException, UnsupportedEncodingException{
		FileOutputStream fos = null;

		File logDir = getDefaultLogPath();
		if(null != logDir && (logDir.exists() || logDir.mkdirs())){
			File logFile = new File(logDir.getAbsolutePath() + File.separator + logFilename);
			//日志文件大于2M后，覆盖重写
			if(logFile.exists() && logFile.length() > LOG_FILE_SIZE){
				fos = new FileOutputStream(logFile, false);
			}else {
				fos = new FileOutputStream(logFile, true);
			}
			return new PrintWriter(new OutputStreamWriter(fos, "UTF-8"), false);
		}
		return null;
	}

	/**
	 * 读取默认的日志文件路径
	 * @return
	 */
	private static File getDefaultLogPath() {
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File logDir = new File(rootPath + ROOT_PATH);
		return logDir;
	}
}
