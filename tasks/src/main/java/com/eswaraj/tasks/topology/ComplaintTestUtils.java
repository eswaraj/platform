package com.eswaraj.tasks.topology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ComplaintTestUtils {

	public static String sendHealthCheck(String host, int port, String cmd) throws IOException {
		System.out.println("connecting to " + host + " " + port);
		Socket sock = new Socket(host, port);
		BufferedReader reader = null;
		try {
			OutputStream outstream = sock.getOutputStream();
			outstream.write(cmd.getBytes());
			outstream.flush();
			// this replicates NC - close the output stream before reading
			sock.shutdownOutput();

			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} finally {
			sock.close();
			if (reader != null) {
				reader.close();
			}
		}
	}

	public static boolean checkZkServer(String host, int port, long timeout) {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				String result = sendHealthCheck(host, port, "stat");
				System.out.println("result of send: " + result);
				if (result.startsWith("Zookeeper version:")) {
					return true;
				}
			} catch (IOException e) {
				System.out.println("server " + host + ":" + port + " not up " + e);
			}

			if (System.currentTimeMillis() > start + timeout) {
				break;
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// ignore
			}
		}
		return false;
	}

	public static void await(CountDownLatch latch) {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("FATAL ERROR");
			System.exit(-1);
		}
	}


	public static void countDown(CountDownLatch latch) {
		try {
			latch.countDown();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FATAL ERROR");
			System.exit(-1);
		}
	}
}
