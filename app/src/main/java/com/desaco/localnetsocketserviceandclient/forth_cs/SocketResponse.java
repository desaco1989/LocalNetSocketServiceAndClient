package com.desaco.localnetsocketserviceandclient.forth_cs;

public abstract class SocketResponse<T> {
    /**
     * 返回数据请求的结果值
     *
     * @param response
     */
    public abstract void onResponse(T response);
}
