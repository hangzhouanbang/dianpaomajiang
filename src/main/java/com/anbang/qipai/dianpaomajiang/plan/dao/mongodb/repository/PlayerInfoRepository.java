package com.anbang.qipai.dianpaomajiang.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.dianpaomajiang.plan.bean.PlayerInfo;

public interface PlayerInfoRepository extends MongoRepository<PlayerInfo, String> {

}
