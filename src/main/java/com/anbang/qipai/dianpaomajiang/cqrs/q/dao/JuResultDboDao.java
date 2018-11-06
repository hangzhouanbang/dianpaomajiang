package com.anbang.qipai.dianpaomajiang.cqrs.q.dao;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboDao {

	void save(JuResultDbo juResultDbo);

	JuResultDbo findByGameId(String gameId);

}
