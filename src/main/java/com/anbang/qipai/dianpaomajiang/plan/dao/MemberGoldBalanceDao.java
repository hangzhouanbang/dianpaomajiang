package com.anbang.qipai.dianpaomajiang.plan.dao;

import com.anbang.qipai.dianpaomajiang.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceDao {

	void save(MemberGoldBalance memberGoldBalance);

	MemberGoldBalance findByMemberId(String memberId);
}
