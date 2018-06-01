package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import java.io.IOException;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多客户端列表、线程、活跃度管理类 这个程序可设置限制客户端的个数
 * 使用setLimit(boolean isLimit)和setLimitNum(int limitNum)设置，默认五个客户端
 * 使用map来存储，使用客户端ip作为唯一的key
 * 
 * @author thinkpad
 * 
 */
public class ClientSocketManager {
	private static ClientSocketManager mSocketManager;
	private Map<String, Socket> socketList = new HashMap<String, Socket>();// 保存客户端列表
	private Map<String, Thread> threadList = new HashMap<String, Thread>();// 保存线程列表
	private Map<String, Integer> actionFrequency = new HashMap<String, Integer>();// 标记每个socket的使用情况，但客户端过多时，优先关闭活动量小的客户端
	private boolean isLimit = false;
	private int limitNum = 5;

	private ClientSocketManager() {
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static ClientSocketManager getInstence() {
		if (mSocketManager == null) {
			mSocketManager = new ClientSocketManager();
		}
		return mSocketManager;
	}

	/**
	 * 获取当前限制状态
	 * 
	 * @return
	 */
	public boolean isLimit() {
		return isLimit;
	}

	/**
	 * 设置是否增加限制
	 * 
	 * @param isLimit
	 */
	public void setLimit(boolean isLimit) {
		this.isLimit = isLimit;
	}

	public int getLimitNum() {
		return limitNum;
	}

	/**
	 * 设置客户端限制的个数
	 * 
	 * @return
	 */
	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	/**
	 * 增加客户端socket
	 * 
	 * @param key使用socket客户端的ip保证不重复
	 *            ,限制客户端limitNum个以内,当多于limitNum个时会根据
	 * @param mSocket
	 * @return
	 */
	public boolean putSocket(String key, Socket mSocket, Thread thread) {
		ShowLogManager.outputDebug("tag", "ClientSocketManager add client:"
				+ key);
		if (socketList != null && threadList != null) {
			if (socketList.get(key) != null) {// 是否是重复的数据
				removeItem(key);
			}
			if (socketList.size() >= limitNum && threadList.size() >= limitNum
					&& isLimit) {
				String minKey = getMinSocket();// 大于
				removeItem(minKey);
			}
			addItem(key, mSocket, thread);//增加一个客户端
			return true;
		}
		return false;
	}

	/**
	 * 增加一个客户端
	 * 
	 * @param key
	 * @param mSocket
	 * @param thread
	 */
	private void addItem(String key, Socket mSocket, Thread thread) {
		socketList.put(key, mSocket);
		threadList.put(key, thread);
		actionFrequency.put(key, 0);
	}

	/**
	 * 删除一个客户端
	 * 
	 * @param key
	 */
	private void removeItem(String key) {
		socketList.remove(key);
		threadList.get(key).interrupt();// 线程断掉
		threadList.remove(key);// 在列表中去掉
		actionFrequency.remove(key);
	}

	/**
	 * 删除客户端连接
	 * 
	 * @param key
	 * @return
	 */
	public boolean removeSocket(String key) {
		ShowLogManager.outputDebug("tag", "ClientSocketManager remove client:"
				+ key);
		if (socketList != null && threadList != null) {
			try {
				socketList.get(key).close();
				threadList.get(key).interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			removeItem(key);
			return true;
		}
		return false;
	}

	/**
	 * 清除数据
	 */
	public void closeSocket() {
		// 断开所有的socket
		for (Map.Entry<String, Socket> map : socketList.entrySet()) {
			try {
				if (map.getValue() != null) {
					map.getValue().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 停止所有的线程
		for (Map.Entry<String, Thread> map : threadList.entrySet()) {
			if (map.getValue() != null && map.getValue().isAlive()) {
				map.getValue().interrupt();
			}

		}
		socketList = null;
		threadList = null;
		actionFrequency = null;
		mSocketManager = null;
	}

	/**
	 * 用ip获得某个socket客户端
	 * 
	 * @param key
	 * @return
	 */
	public Socket getSocket(String key) {
		if (mSocketManager != null && socketList != null) {
			return socketList.get(key);
		}
		return null;
	}

	/**
	 * 返回key对应的线程
	 * 
	 * @param key
	 * @return
	 */
	public Thread getThread(String key) {
		if (threadList != null && mSocketManager != null) {
			return threadList.get(key);
		}
		return null;
	}

	/**
	 * 获得当前客户端个数，出错时返回-1
	 * 
	 * @return
	 */
	public int getClientNum() {
		if (socketList != null) {
			return socketList.size();
		}
		return -1;
	}

	/**
	 * 返回当前列表的所有socket的ip
	 * 
	 * @return
	 */
	public List<String> getSocketIPList() {
		List<String> list = new ArrayList<String>();
		if (socketList != null) {
			for (Map.Entry<String, Socket> map : socketList.entrySet()) {
				list.add(map.getKey());
			}
			return list;
		}
		return null;
	}

	/**
	 * 当客户端有有效的数据更新的时候把活动量增加
	 * 
	 * @param key
	 */
	public void setFrequency(String key) {
		if (actionFrequency != null) {
			Integer ins = actionFrequency.get(key);
			ins++;
			actionFrequency.put(key, ins);
		}
	}

	/**
	 * 获得数据传输量最小的客户端的IP
	 * 
	 * @return
	 */
	public String getMinSocket() {
		int cont = 10000;
		String minSocketIP = "";
		for (Map.Entry<String, Integer> map : actionFrequency.entrySet()) {
			if (map.getValue() < cont) {
				cont = map.getValue();
				minSocketIP = map.getKey();
			}
		}
		return minSocketIP;
	}

}
