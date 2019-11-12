package com.codelovers.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.sql.Timestamp;

@Entity
@Table(name = "chanel")
public class Chanel {
	@Id
	@Column(name = "chanel_name")
	private String chanelName;

	@Column(name = "count_users")
	private int countUsers;


	public int getCountUsers() {
		return countUsers;
	}

	public void setCountUsers(int countUsers) {
		this.countUsers = countUsers;
	}

	public String getChanelName() {
		return chanelName;
	}

	public void setChanelName(String chanelName) {
		this.chanelName = chanelName;
	}

}
