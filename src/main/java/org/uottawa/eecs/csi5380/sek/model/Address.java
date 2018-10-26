package org.uottawa.eecs.csi5380.sek.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "address")
public class Address {

	//////////////////// attributes of Address model class //////////////

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	int id;
	@NotNull
	@Size(min = 2, message = "at least 2 characters long")
	@Column(name = "street", columnDefinition = "VARCHAR")
	String street;
	@NotNull
	@Size(min = 2, message = "at least 2 characters long")
	@Column(name = "province", columnDefinition = "VARCHAR")
	String province;
	@NotNull
	@Size(min = 2, message = "at least 2 characters long")
	@Column(name = "country", columnDefinition = "VARCHAR")
	String country;
	@NotNull
	@Size(min = 6, message = "at least 6 characters long")
	@Column(name = "zip", columnDefinition = "VARCHAR")
	String zip;
	@NotNull
	@Size(min = 10, message = "at least 10 characters long")
	@Column(name = "phone", columnDefinition = "VARCHAR")
	String phone;
	
	@Column(name = "type", columnDefinition="ENUM('billing','shipping')")
	private String type;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/////////////////////// getters and setters/////////////////////
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
