package com.codelovers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codelovers.model.Chanel;
@Repository
public interface ChanelRepository   extends JpaRepository<Chanel, Long>{
	Chanel findByChanelName(String name);

}
