package com.gient.buffer;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * 测试Buffer的4个核心属性
 * @author gient
 *
 */
public class BufferTest {

	@Test
	public void test2(){
		String str = "abcde";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(str.getBytes());
		buffer.flip();
		byte[] dst = new byte[buffer.limit()];
		buffer.get(dst,0,2);
		System.out.println(new String(dst));
		System.out.println(buffer.position());
		
		buffer.mark();
		buffer.get(dst, 2, 2);
		System.out.println(new String(dst));
		
		System.out.println("position:"+buffer.position());
		buffer.reset();
		System.out.println("position:"+buffer.position());
	}
	
	@Test
	public void test1(){
		String str = "abcde";
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		System.out.println("-----allocate-----");
		System.out.println("position:"+buffer.position());
		System.out.println("limit:"+buffer.limit());
		System.out.println("capcity:"+buffer.capacity());
		
		buffer.put(str.getBytes());
		
		System.out.println("-----put-----");
		System.out.println("position:"+buffer.position());
		System.out.println("limit:"+buffer.limit());
		System.out.println("capcity:"+buffer.capacity());
		
		buffer.flip();
		
		System.out.println("-----flip-----");
		System.out.println("position:"+buffer.position());
		System.out.println("limit:"+buffer.limit());
		System.out.println("capcity:"+buffer.capacity());
		
		byte[] dst = new byte[buffer.limit()];
		ByteBuffer byteBuffer = buffer.get(dst);
		System.out.println(new String(dst,0,dst.length));
		System.out.println("-----get-----");
		System.out.println("position:"+buffer.position());
		System.out.println("limit:"+buffer.limit());
		System.out.println("capcity:"+buffer.capacity());
		
		buffer.rewind();
		
//		byte[] dest = new byte[buffer.limit()];
//		buffer.get(dest);
//		System.out.println(new String(dest,0,dest.length));
		
		System.out.println("-----rewind-----");
		System.out.println("position:"+buffer.position());
		System.out.println("limit:"+buffer.limit());
		System.out.println("capcity:"+buffer.capacity());
		
		byte[] dest = new byte[buffer.limit()];
		buffer.get(dest);
		System.out.println(new String(dest,0,dest.length));
	}
}
