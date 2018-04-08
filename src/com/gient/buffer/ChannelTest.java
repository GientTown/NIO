package com.gient.buffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class ChannelTest {

	// 字符集：编码与解码
	@Test
	public void test5() throws IOException {
		Charset charset = Charset.forName("utf-8");
		// 获取编码器
		CharsetEncoder encoder = charset.newEncoder();
		// 获取解码器
		CharsetDecoder decoder = charset.newDecoder();
		// 获取缓冲区
		CharBuffer cBuf = CharBuffer.allocate(1024);
		cBuf.put("字符集：编码与解码！");
		System.out.println(cBuf.position());
		System.out.println(cBuf.limit());
		cBuf.flip();
		System.out.println(cBuf.position());
		System.out.println(cBuf.limit());

		// 编码
		ByteBuffer bBuf = encoder.encode(cBuf);
		for (int i = 0; i < bBuf.limit(); i++) {
			System.out.println(bBuf.get());
		}
		bBuf.flip();
		
		//解码
		CharBuffer charBuffer = decoder.decode(bBuf);
		System.out.println(charBuffer.toString());
	}

	// 分散读取，聚集写入
	@Test
	public void test4() throws IOException {
		RandomAccessFile accessFile = new RandomAccessFile("./sql.txt", "rw");
		FileChannel inChannel = accessFile.getChannel();
		// 分散读取
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(200);
		ByteBuffer[] bufs = { buf1, buf2 };
		inChannel.read(bufs);
		// 切换为写入数据模式
		for (ByteBuffer buf : bufs) {
			buf.flip();
		}
		System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
		System.out.println("----------");
		System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
		// 聚集写入
		RandomAccessFile accessFile2 = new RandomAccessFile("./sqll.txt", "rw");
		FileChannel outChannel = accessFile2.getChannel();
		outChannel.write(bufs);

		outChannel.close();
		inChannel.close();

	}

	// 通道之间的数据传输（直接缓冲区）
	@Test
	public void test3() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("./1.jpg"), StandardOpenOption.READ);

		FileChannel outChannel = FileChannel.open(Paths.get("./2.jpg"), StandardOpenOption.WRITE,
				StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

		// inChannel.transferTo(0, inChannel.size(), outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());

		outChannel.close();
		inChannel.close();
	}

	// 使用直接缓冲区实现文件复制（内存映射文件）
	@Test
	public void test2() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("./1.jpg"), StandardOpenOption.READ);

		FileChannel outChannel = FileChannel.open(Paths.get("./2.jpg"), StandardOpenOption.WRITE,
				StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);
		// 内存映射文件
		MappedByteBuffer inMapBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMapBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());

		byte[] dst = new byte[1024];

		// 直接对缓冲区数据进行读写操作
		inMapBuf.get(dst);
		outMapBuf.put(dst);

		outChannel.close();
		inChannel.close();
	}

	// 使用通道实现文件复制（非直接缓冲区）
	@Test
	public void test() throws Exception {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		FileChannel outChannel = null;
		FileChannel inChannel = null;

		// 通道
		fis = new FileInputStream("./1.jpg");
		inChannel = fis.getChannel();

		fos = new FileOutputStream("5.jpg");
		outChannel = fos.getChannel();

		// 缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		// 将通道中的数据写入到缓冲区中
		while (inChannel.read(buffer) != -1) {
			// 切换为读数据模式
			buffer.flip();
			// 将缓冲区中的数据写入通道中
			outChannel.write(buffer);
			// 清空缓冲区
			buffer.clear();
		}

		outChannel.close();
		inChannel.close();
		fos.close();
		fis.close();

	}

}
