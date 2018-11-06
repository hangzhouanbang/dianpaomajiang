package com.anbang.qipai.dianpaomajiang.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.dianpaomajiang.plan.bean.MemberGoldBalance;
import com.anbang.qipai.dianpaomajiang.plan.dao.MemberGoldBalanceDao;

@Service
public class MemberGoldBalanceService {

	@Autowired
	private MemberGoldBalanceDao memberGoldBalanceDao;

	public void save(MemberGoldBalance memberGoldBalance) {
		memberGoldBalanceDao.save(memberGoldBalance);
	}

	public MemberGoldBalance findByMemberId(String memberId) {
		return memberGoldBalanceDao.findByMemberId(memberId);
	}
}
