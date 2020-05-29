package com.huangpeng.messages.service.impl;

import com.huangpeng.messages.dao.EwsSaAreariskindexRtMapper;
import com.huangpeng.messages.entities.EwsSaAreariskindexRt;
import com.huangpeng.messages.service.IAreariskindexRtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请求数据
 */
@Slf4j
@Service
public class AreariskindexRtService implements IAreariskindexRtService {

	@Autowired
	private EwsSaAreariskindexRtMapper ewsSaAreariskindexRtMapper;

	@Override
	public EwsSaAreariskindexRt getAreariskindexRt(String areaId) {
		return this.ewsSaAreariskindexRtMapper.selectNotifyList(areaId);
	}

	@Override
	public EwsSaAreariskindexRt selectLatestTimeModel() {
		return this.ewsSaAreariskindexRtMapper.selectLatestTimeModel();
	}

	@Override
	public int selectCount() {
		return this.ewsSaAreariskindexRtMapper.selectCount();
	}
}
