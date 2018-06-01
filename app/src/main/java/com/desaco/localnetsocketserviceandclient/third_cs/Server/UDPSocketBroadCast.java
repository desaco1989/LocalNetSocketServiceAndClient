package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * UDP广播类，定时向局域网内发送广播广播信息为本机的ip和端口号，“IAMZTSERVERSOCKET-IP-PORT”
 * 
 * @author thinkpad
 * 
 */
public class UDPSocketBroadCast {
	/**
	 * .要使用多点广播,需要让一个数据报标有一组目标主机地址,其思想便是设置一组特殊网络地址作为多点广播地址,第一个多点广播地址都被看作是一个组
	 * ,当客户端需要发送
	 * .接收广播信息时,加入该组就可以了.IP协议为多点广播提供这批特殊的IP地址,这些IP地址范围是224.0.0.0---239.255
	 * .255.255
	 * ,其中224.0.0.0为系统自用.下面BROADCAST_IP是自己声明的一个String类型的变量,其范围但是前面所说的IP范围
	 * ,比如BROADCAST_IP="224.224.224.225"
	 */
	private static final String BROADCAST_IP = "224.224.224.225";
	private static final int BROADCAST_PORT = 8681;
	private static byte[] sendData;
	private boolean isStop = false;
	private static UDPSocketBroadCast broadCast = new UDPSocketBroadCast();
	private MulticastSocket mSocket = null;
	private InetAddress address = null;
	private DatagramPacket dataPacket;

	private UDPSocketBroadCast() {

	}

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static UDPSocketBroadCast getInstance() {
		if (broadCast == null) {
			broadCast = new UDPSocketBroadCast();
		}
		return broadCast;
	}

	/**
	 * 开始发送广播
	 * 
	 * @param ip
	 */
	public void startUDP(String ip, int port) {
		sendData = ("IAMZTSERVERSOCKET" + "-" + ip + "-" + port).getBytes();
		ShowLogManager.outputDebug("tag", ip+";"+port);
		new Thread(UDPRunning).start();
	}

	/**
	 * 停止广播
	 */
	public void stopUDP() {
		isStop = true;
		destroy();
	}

	/**
	 * 销毁缓存的数据
	 */
	public void destroy() {
		broadCast = null;
		mSocket = null;
		address = null;
		dataPacket = null;
		sendData = null;
	}

	/**
	 * 创建udp数据
	 */
	private void CreateUDP() {
		try {
			mSocket = new MulticastSocket(BROADCAST_PORT);
			mSocket.setTimeToLive(1);// 广播生存时间0-255
			address = InetAddress.getByName(BROADCAST_IP);
			mSocket.joinGroup(address);
			dataPacket = new DatagramPacket(sendData, sendData.length, address,
					BROADCAST_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 两秒发送一次广播
	 */
	private Runnable UDPRunning = new Runnable() {

		@Override
		public void run() {
			while (!isStop) {
				if (mSocket != null) {
					try {
						mSocket.send(dataPacket);
						Thread.sleep(5 * 1000);// 发送一次停5秒
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					CreateUDP();
				}
			}
		}
	};

}
