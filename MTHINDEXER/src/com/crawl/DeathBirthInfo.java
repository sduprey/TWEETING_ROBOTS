package com.crawl;

import java.util.Date;

public class DeathBirthInfo {

	private String birthLocation;
	private String deathLocation;
	private Date birthDate;
	private Date deathDate;
	public String getDeathLocation() {
		return deathLocation;
	}
	public void setDeathLocation(String deathLocation) {
		this.deathLocation = deathLocation;
	}
	public String getBirthLocation() {
		return birthLocation;
	}
	public void setBirthLocation(String birthLocation) {
		this.birthLocation = birthLocation;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

}
