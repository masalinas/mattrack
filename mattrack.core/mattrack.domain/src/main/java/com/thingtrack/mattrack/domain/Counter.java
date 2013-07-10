package com.thingtrack.mattrack.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name="COUNTER")
public class Counter implements Serializable {
	@Id
	@Column(name="COUNTER_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer counterId;
	
	@Column(name="DEVICE_NAME", nullable=false, length=255)
	private String deviceName;
		
	@Column(name="WAY", nullable=false)
	private int way;
	
	@Column(name="COUNT_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date countDate;

	/**
	 * @return the counterId
	 */
	public Integer getCounterId() {
		return counterId;
	}

	/**
	 * @param counterId the counterId to set
	 */
	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the way
	 */
	public int getWay() {
		return way;
	}

	/**
	 * @param way the way to set
	 */
	public void setWay(int way) {
		this.way = way;
	}

	/**
	 * @return the countDate
	 */
	public Date getCountDate() {
		return countDate;
	}

	/**
	 * @param countDate the countDate to set
	 */
	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Counter [counterId=" + counterId + ", deviceName=" + deviceName
				+ ", way=" + way + ", countDate=" + countDate + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((counterId == null) ? 0 : counterId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Counter other = (Counter) obj;
		if (counterId == null) {
			if (other.counterId != null)
				return false;
		} else if (!counterId.equals(other.counterId))
			return false;
		return true;
	}
}
