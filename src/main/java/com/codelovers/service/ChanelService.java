package com.codelovers.service;

import com.codelovers.model.Chanel;

public interface ChanelService {
	void save(Chanel chanel);
	Chanel findByChanelName(String name);
	void delete(Chanel chanel);
}
