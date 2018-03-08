package com.test.data.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UnitQo extends PageQo {
	private Long id;
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date create;

	public UnitQo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

}
