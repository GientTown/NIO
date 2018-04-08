package com.gient.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

public class ChannelTest {

	//使用非直接缓冲区实现文件复制
	@Test
	public void test() throws Exception {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		FileChannel outChannel = null;
		FileChannel inChannel = null;
		
		//通道
		fis = new FileInputStream("./1.jpg");
		inChannel = fis.getChannel();
		
		fos = new FileOutputStream("5.jpg");
		outChannel = fos.getChannel();
		
		//缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		//将通道中的数据写入到缓冲区中
		while (inChannel.read(buffer) != -1) {
			//切换为读数据模式
			buffer.flip();
			//将缓冲区中的数据写入通道中
			outChannel.write(buffer);
			//清空缓冲区
			buffer.clear();
		}
		
		outChannel.close();
		inChannel.close();
		fos.close();
		fis.close();
		
	}
	
}
