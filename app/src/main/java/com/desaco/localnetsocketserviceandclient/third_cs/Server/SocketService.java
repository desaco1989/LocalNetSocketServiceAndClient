package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import java.net.SocketException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SocketService extends Service {
	private UDPSocketBroadCast mBroadCast;
	private ServersSocket mServersSocket;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			String ip = ConnectionManager.getLocalIP();
			if (ip != null && !"".equals(ip)) {
				Info.SERVERSOCKET_IP = ip;
				mBroadCast = UDPSocketBroadCast.getInstance();
				mServersSocket = ServersSocket.getInstance();
				mBroadCast.startUDP(Info.SERVERSOCKET_IP,
						Info.SERVERSOCKET_PORT);
				mServersSocket.startServer(clientData);
			} else {
				Toast.makeText(getApplicationContext(), "请检查网络设置",
						Toast.LENGTH_LONG).show();
				stopSelf();
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 客户端数据在这里处理
	 */
	private ServersSocket.ClientDataCallBack clientData = new ServersSocket.ClientDataCallBack() {

		@Override
		public void getClientData(int connectMode, String str) {
			switch (connectMode) {
			case Info.CONNECT_SUCCESS:// 连接成功
				sendCast(Info.CONNECT_SUCCESS, str);
				break;
			case Info.CONNECT_GETDATA:// 传输数据
				sendCast(Info.CONNECT_SUCCESS, str);
				break;
			case Info.CONNECT_FAIL:
				sendCast(Info.CONNECT_FAIL, str);
				break;
			}
		}

		private void sendCast(int flag, String str) {
			Intent intent = new Intent();
			intent.putExtra("flag", flag);
			intent.putExtra("str", str);
			intent.setAction("updata");
			sendBroadcast(intent);
		}
	};

}
