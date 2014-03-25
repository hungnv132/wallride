package org.wallride.core.domain;


import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

/*
 * 選手
 */
@Entity
@Table(name="tm05_player")
public class Player implements Serializable {

	@Id
	@Column(name="tm05_id")
	private int id;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="tm05_nationality1")
	private Country country;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || !(other instanceof DomainObject)) return false;
		DomainObject that = (DomainObject) other;
		return (getId() == that.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
}
