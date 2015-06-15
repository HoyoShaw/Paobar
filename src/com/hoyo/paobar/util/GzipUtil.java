package com.hoyo.paobar.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip的压缩工具.
 * @author hg
 *
 */
public final class GzipUtil {
	private static final int BUFFER_SIZE = 1024;
	private GzipUtil(){
	}

	/**
	 * 根据输入流压缩成gzip,
	 * @param is
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static byte[] ungzip( InputStream is ) throws IOException{
		GZIPInputStream gis = null;
		try {
			gis = new GZIPInputStream( is );
			return IOUtil.readBytes( gis );
		} finally {
			IOUtil.close( gis, is );
		}
	}

	/**
	 * 根据字节数组压缩成gzip,
	 * @param in
	 * @return 压缩后的字节数组
	 */
    public static byte[] gzip(byte[] in){
        ByteArrayOutputStream baos = new ByteArrayOutputStream( BUFFER_SIZE );
        GZIPOutputStream gos = null;
        try {
        	gos = new GZIPOutputStream( baos );
            gos.write(in);
            gos.finish();
            //gos.flush();
            IOUtil.close(gos);
            return baos.toByteArray();
        } catch (IOException e) {
    		throw new IllegalStateException("This exception should not happen. If happend, there must be some unreasonable thing.");
        }finally{
        	IOUtil.close( baos, gos );
        }
    }

    /**
     * 根据字节数组解压缩gzip
     * @param in
     * @return 解压缩以后的字节数据
     */
    public static byte[] ungzip( byte[] in ){
    	InputStream is = null;
    	try{
    		is = new ByteArrayInputStream( in );
            return ungzip( is );
    	}catch( IOException ioe ){
    		throw new IllegalStateException("This exception should not happen. If happend, there must be some unreasonable thing.");
    	}finally{
    		IOUtil.close( is );
    	}
    }
}