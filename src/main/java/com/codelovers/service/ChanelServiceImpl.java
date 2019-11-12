package com.codelovers.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codelovers.model.Chanel;
import com.codelovers.repository.ChanelRepository;

@Service
public class ChanelServiceImpl implements ChanelService {
	@Autowired
	ChanelRepository repository;
	@PersistenceContext
	private EntityManager em;

//	@Modifying
	@Transactional
	@Override
	public void save(Chanel chanel) {
		// TODO Auto-generated method stub
		repository.save(chanel);

	}

	@Override
	public Chanel findByChanelName(String name) {
		// TODO Auto-generated method stub
		return repository.findByChanelName(name);
	}

	@Override
	public void delete(Chanel chanel) {
		// TODO Auto-generated method stub
		repository.delete(chanel);
	}

}