package com.anbang.qipai.dianpaomajiang.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboRepository extends MongoRepository<JuResultDbo, String> {

	JuResultDbo findOneByGameId(String gameId);

}
