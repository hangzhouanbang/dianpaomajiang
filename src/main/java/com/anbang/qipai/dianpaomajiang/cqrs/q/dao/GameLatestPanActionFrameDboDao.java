package com.anbang.qipai.dianpaomajiang.cqrs.q.dao;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.GameLatestPanActionFrameDbo;

public interface GameLatestPanActionFrameDboDao {

	GameLatestPanActionFrameDbo findById(String id);

	void save(String id, byte[] data);

}
