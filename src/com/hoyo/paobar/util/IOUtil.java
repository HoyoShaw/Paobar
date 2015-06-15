package com.hoyo.paobar.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

import android.util.Log;

/**
 * 数据流的工具类
 * @author hg
 *
 */
public final class IOUtil {
	public static final String DFT_CHARSET = "utf-8";
	private IOUtil(){}

	private static final int BUFFER_SIZE = 2048;

	/**
	 * 关闭所有closeables子类的流,close异常不会抛出
	 * @param closeables
	 */
	public static void close(java.io.Closeable... closeables){
		if( closeables != null ){
			for (java.io.Closeable c : closeables) {
				if (c != null) {
					try{
						c.close();
					}catch (Exception e) {
						Log.d("IOUTil","Close failed",e);
					}
				}
			}
		}
	}

	/**
	 * 网络条件不好的情况下关闭网络流会等很久，这里增加了一个异步关闭的方法
	 * @param is
	 */
	public static void asynchronousClose( final java.io.Closeable... closeables ){
		new Thread(new Runnable() {
			@Override
			public void run() {
				IOUtil.close( closeables );
			}
		}).start();
	}

	/**
	 * 把输入流写到输出流中.不关闭该流
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public static void fromTo( InputStream is, OutputStream os ) throws IOException{
		fromTo( is, os, false );
	}

	/**
	 * 把输入流写到输出流中.
	 * @param is
	 * @param os
	 * @param closeIO 指定是否关闭该流
	 * @throws IOException
	 */
	public static void fromTo( InputStream is, OutputStream os, boolean closeIO ) throws IOException{
		if( is == null || os == null ) {
			return;
		}

		try{
			byte [] buffer = new byte[BUFFER_SIZE];//1k bytes buffer
			int size = is.read( buffer, 0, BUFFER_SIZE);
			while( size != -1 ){
				os.write( buffer, 0 , size );
				size = is.read( buffer, 0, BUFFER_SIZE);
			}
			os.flush();
		}finally{
			if( closeIO ){
				IOUtil.close( is, os );
			}
		}
	}

	/**
	 * 把reader读成字符串,不关闭该流
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String read( Reader reader ) throws IOException{
		return read( reader, false );
	}

	/**
	 * 把reader读成字符串
	 * @param reader
	 * @param closeReader 是否关闭该流
	 * @return
	 * @throws IOException
	 */
	public static String read( Reader reader, boolean closeReader ) throws IOException{
		if( reader == null ) {
			return null;
		}

		char [] buffer = new char[BUFFER_SIZE];
		StringBuilder sb = new StringBuilder();

		try {
			int count = reader.read(buffer);
			while( count != -1 ){
				sb.append( buffer, 0, count );
				count = reader.read(buffer);
			}
		} finally{
			if( closeReader ){
				IOUtil.close( reader );
			}
		}

		return sb.toString();
	}

	public static String read(InputStream is, String charSet){
		if( is == null ) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		char [] chBuf = new char[200];
		try {
			InputStreamReader reader = new InputStreamReader(is,charSet);
			int len = reader.read(chBuf);
			while(len != -1){
				sb.append(chBuf, 0, len);
				len = reader.read(chBuf);
			}
		} catch (IOException e) {
			LogUtil.e(e);
		} finally{
			IOUtil.close(is);
		}

		return sb.toString();
	}

	public static String read(InputStream is){
		return read(is, "utf-8");
	}

	/**
	 * 根据输入流解压成gzip,并根据编码生产字符串
	 * @param inputStream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readGzip(InputStream inputStream, String charset) throws IOException{
        GZIPInputStream gis = null;
        try {
            byte[] realBytes = IOUtil.readBytes(inputStream);
            byte[] unzipBytes = GzipUtil.ungzip(realBytes);

            return new String(unzipBytes, charset);
        } finally {
        	IOUtil.close(gis);
        }
	}

	/**
	 * close InputStream after read.
	 * @param inputStream
	 * @param charSet
	 * @return
	 * @throws IOException
	 */
	public static String readText( InputStream inputStream, String charSet ) throws IOException{
		if( inputStream == null ) {
			return null;
		}

		try {
			InputStreamReader reader = new InputStreamReader( inputStream, charSet );
			return read( reader, true );
		} finally {
			IOUtil.close(inputStream);
		}
	}

	/**
	 *
	 * close InputStream after read.
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readText( InputStream inputStream ) throws IOException{
		return readText(inputStream, DFT_CHARSET);
	}

	/**
	 *
	 * 读取完所有inputStream内容之后close掉inputStream
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte [] readBytes( InputStream inputStream ) throws IOException{
		ByteArrayOutputStream bos= new ByteArrayOutputStream();
		fromTo(inputStream, bos, true);
		return bos.toByteArray();
	}

	/**
	 * 读取固定长度的数据()
	 * @param inStream
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream inStream, int length,boolean closeIO) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		fromTo(inStream, bos, length,closeIO);
		return bos.toByteArray();
	}

	/**
	 * 从输入流读取固定长度到输出流中(不关闭输入流)
	 * @param is
	 * @param os
	 * @param length
	 * @throws IOException
	 */
	public static void fromTo(InputStream is,OutputStream os,int length,boolean closeIO) throws IOException {
		if( is == null || os == null ) {
			return;
		}

		try{
			byte[] bf = new byte[BUFFER_SIZE];
			int len = -1;
			int readCount = 0;
			int readLength = length > BUFFER_SIZE ? BUFFER_SIZE : length;
			while((len = is.read(bf, 0, readLength)) != -1 && readCount < length){
				os.write(bf, 0, len);

				readCount += len;
				readLength = (length - readCount) > BUFFER_SIZE ? BUFFER_SIZE : (length - readCount);
			}
			os.flush();
		}finally{
			IOUtil.close(os);
		}
	}

	/**
     * 流的拷贝，带流量检查
     *
     * @param in
     * @param out
     * @param buffSize
     * @param checkedSize
     * @param close
     * @return 拷贝的文件长度
     * @throws java.io.IOException
     */
    public static long copyBytes(InputStream in, OutputStream out, int buffSize, long checkedSize, boolean close)
            throws IOException {
        byte buf[] = new byte[buffSize];
        try {
            long readCount = 0;
            int bytesRead = 0;
            while ((bytesRead = in.read(buf)) >= 0) {
                readCount += bytesRead;
                if (readCount > checkedSize && checkedSize != 0) {
                    throw new IOException("Inputstream is more than " + checkedSize + " bytes");
                }
                out.write(buf, 0, bytesRead);
            }
            return readCount;
        } finally {
            if (close) {
                out.close();
                in.close();
            }
        }
    }

    /**
     * 流的拷贝
     *
     * @param in
     * @param out
     * @param buffSize
     * @param close
     * @return 返回拷贝流长度
     * @throws java.io.IOException
     */
    public static long copyBytes(InputStream in, OutputStream out, int buffSize, boolean close)
            throws IOException {
        byte buf[] = new byte[buffSize];
        try {
            long writeCount = 0;
            int bytesRead = 0;
            while ((bytesRead = in.read(buf)) >= 0) {
                out.write(buf, 0, bytesRead);
                writeCount += bytesRead;
            }
            return writeCount;
        } finally {
            if (close) {
                out.close();
                in.close();
            }
        }
    }

    /**
     * 彻底读取流到字节数组
     *
     * @param in
     * @param buf
     * @param off
     * @param len
     * @throws java.io.IOException
     */
    public static byte[] readFully(InputStream in, long checkedSize, boolean close) throws IOException {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            IOUtil.copyBytes(in, output, BUFFER_SIZE, checkedSize, false);
            return output.toByteArray();
        } finally {
            if (close) {
                output.close();
                in.close();
            }
        }
    }

    /**
     * 写入流到文件
     *
     * @param file
     * @param in
     * @param close
     * @throws java.io.IOException
     */
    public static long write(InputStream in, File toFile, boolean close) throws IOException {
        FileOutputStream out = new FileOutputStream(toFile);
        try {
            return copyBytes(in, out, BUFFER_SIZE, close);
        } finally {
            if (!close && out != null) {
                out.close();
            }
        }
    }
}