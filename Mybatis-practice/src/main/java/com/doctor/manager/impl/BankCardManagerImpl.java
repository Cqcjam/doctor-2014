package com.doctor.manager.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.doctor.entity.BankCard;
import com.doctor.manager.BankCardManager;

@Repository("bankCardManager")
public class BankCardManagerImpl implements BankCardManager {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	SqlSessionTemplate sqlSessionTemplate;

	public boolean insertBankCard(BankCard bankCard) {
		if(sqlSessionTemplate.insert("BankCardDao.insert_bank_card", bankCard) ==1){
			return true;
		}
		
		return false;
	}
}
