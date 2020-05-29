package com.huangpeng.messages.dao;

import com.huangpeng.messages.entities.EwsSaAreariskindexRt;
import org.springframework.stereotype.Repository;

@Repository
public interface EwsSaAreariskindexRtMapper {
    public EwsSaAreariskindexRt selectNotifyList(String areaId);
    public EwsSaAreariskindexRt selectLatestTimeModel();
    public int selectCount();
}