package com.gient.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class BlockingNio {

	@Test
	//客户端
	public void client() throws IOException{
		//建立socket通道
		SocketChannel sChannel = SocketChannel.open(
				new InetSocketAddress("127.0.0.1", 8989));
		//创建本地文件通道
		FileChannel fChannel = FileChannel.open(
				Paths.get("1.jpg"), StandardOpenOption.READ);
		//分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//本地文件通道向缓冲区写入数据
		while (fChannel.read(buf) != -1) {
			//切换读数据模式
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		//关闭通道
		sChannel.close();
		fChannel.close();
	}
	
	@Test
	//服务端
	public void server() throws IOException{
		//1、获取通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		
		//绑定连接
		ssChannel.bind(new InetSocketAddress(8989));
		
		//2、获取本地文件通道
		FileChannel fChannel = FileChannel.open(Paths.get("2.jpg"), 
				StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		//3、获取客户端连接的通道
		SocketChannel sChannel = ssChannel.accept();
		
		//4、分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		//5、接收客户端的数据，并保存到本地
		while (sChannel.read(buf) != -1) {
			//切换模式
			buf.flip();
			fChannel.write(buf);
			buf.clear();
		}
		
		//6、关闭通道
		fChannel.close();
		sChannel.close();
		ssChannel.close();
	}
}
