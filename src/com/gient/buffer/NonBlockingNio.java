package com.gient.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 测试非阻塞式nio，使用selector
 * @author gient
 *
 */
public class NonBlockingNio {

	@Test
	//客户端
	public void client() throws IOException{
		//1、获取通道
		SocketChannel sChannel = SocketChannel.open(
				new InetSocketAddress("127.0.0.1", 8989));
		
		//2、切换为非阻塞模式
		sChannel.configureBlocking(false);
		
		//3、分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String str = scanner.nextLine();
			//4、向缓冲区中写入数据
			buf.put((LocalDateTime.now().toString()+str).getBytes());
			buf.flip();
			
			//5、通道从缓冲区中读取数据
			sChannel.write(buf);
			buf.clear();
		}
		
		
		sChannel.close();
	}
	
	@Test
	//服务端
	public void server() throws IOException{
		//1、获取通道并绑定连接
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.bind(new InetSocketAddress(8989));
		
		//2、切换为非阻塞模式
		ssChannel.configureBlocking(false);
		
		//3、获取选择器
		Selector selector = Selector.open();
		
		//4、将通道注册到选择器,并指定监听事件
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		//5、访问选择器中已经准备就绪的通道任务（选择一组其相应通道准备好进行I/O操作的键）
		while (selector.select() > 0) {
			//6、获取当前选择器中注册的选择键
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			//7、判断选择键的类型
			while (it.hasNext()) {
				SelectionKey sk = it.next();
				if(sk.isAcceptable()){
					//选择键事件是 连接就绪
					//8、获取客户端通道
					SocketChannel sChannel = ssChannel.accept();
					
					//9、将客户端通道注册到选择器中
					sChannel.configureBlocking(false);
					sChannel.register(selector, SelectionKey.OP_READ);
				}else if (sk.isReadable()) {
					//选择键事件是 读就绪
					//10、获取当前选择器上“读就绪”的通道
					SocketChannel sChannel = (SocketChannel) sk.channel();
					
					//11、读数据
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int len = 0;
					while ((len = sChannel.read(buf)) > 0) {
						buf.flip();
						System.out.println(new String(buf.array(),0,len));
						buf.clear();
					}
				}
				//12、取消选择键
				it.remove();
			}
		}
	}
	
	
}





