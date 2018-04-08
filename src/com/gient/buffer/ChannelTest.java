package com.gient.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class ChannelTest {
	
	//通道之间的数据传输（直接缓冲区）
	@Test
	public void test3() throws IOException{
		FileChannel inChannel = FileChannel.open(
				Paths.get("./1.jpg"), StandardOpenOption.READ);
		
		FileChannel outChannel = FileChannel.open(
				Paths.get("./2.jpg"), StandardOpenOption.WRITE,
				StandardOpenOption.READ,StandardOpenOption.CREATE_NEW);
		
		// inChannel.transferTo(0, inChannel.size(), outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		
		outChannel.close();
		inChannel.close();
	}
	
	//使用直接缓冲区实现文件复制（内存映射文件）
	@Test
	public void test2() throws IOException{
		FileChannel inChannel = FileChannel.open(
				Paths.get("./1.jpg"), StandardOpenOption.READ);
		
		FileChannel outChannel = FileChannel.open(
				Paths.get("./2.jpg"), StandardOpenOption.WRITE,
				StandardOpenOption.READ,StandardOpenOption.CREATE_NEW);
		//内存映射文件
		MappedByteBuffer inMapBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMapBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		byte[] dst = new byte[1024];
		
		//直接对缓冲区数据进行读写操作
		inMapBuf.get(dst);
		outMapBuf.put(dst);
		
		outChannel.close();
		inChannel.close();
	}
	
	//使用通道实现文件复制（非直接缓冲区）
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
