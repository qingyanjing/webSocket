package com.huangpeng.messages.service;


import com.huangpeng.messages.entities.EwsSaAreariskindexRt;

public interface IAreariskindexRtService {
    /**
     * 请求数据
     * @return
     */
	public EwsSaAreariskindexRt getAreariskindexRt(String areaId);
    public EwsSaAreariskindexRt selectLatestTimeModel();
    public int selectCount();
}

