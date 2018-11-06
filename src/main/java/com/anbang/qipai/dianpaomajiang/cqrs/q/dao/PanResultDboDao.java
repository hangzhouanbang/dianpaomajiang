package com.anbang.qipai.dianpaomajiang.cqrs.q.dao;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.PanResultDbo;

public interface PanResultDboDao {

	void save(PanResultDbo panResultDbo);

	PanResultDbo findByGameIdAndPanNo(String gameId, int panNo);

}
