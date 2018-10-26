package org.uottawa.eecs.csi5380.sek.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

/**
 * It is User model class
 * @author Kuntal Ghosh
 *
 */

@Entity
@Table(name = "user")
public class User {
	
	//////////////////attributes of User model class////////////////////
	@Id
	@Column(name = "id", unique = true, columnDefinition = "INT" )
	private int id;
	@NotNull
	@Size(min = 4, message ="at least 6 characters long")
	@Column(name = "user_name", unique = true, columnDefinition = "VARCHAR")
    private String userName;
	@NotNull
	@Email(message ="not a valid email format")
	@Size(min = 6, message ="at least 6 character long")
	@Column(name = "email", unique = true, columnDefinition = "VARCHAR")
    private String email;
	@NotNull
	@Size(min = 4, message ="at least 4 characters long")
	@Column(name = "password", columnDefinition = "VARCHAR")
    private String password;
    private String salt;
    @NotNull
    @Column(name = "first_name", columnDefinition = "VARCHAR" )
    private String firstName;
    @NotNull
    @Column(name = "last_name", columnDefinition = "VARCHAR" )
    private String lastName;
    
    //////////////////getters and setters////////////////////
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	} 
}
