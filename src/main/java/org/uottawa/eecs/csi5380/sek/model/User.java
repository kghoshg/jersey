package org.uottawa.eecs.csi5380.sek.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

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
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id", unique = true, columnDefinition = "INT" )
	private int id;
	@NotNull
	@Size(min = 4, message ="at least 6 characters long")
	@Column(name = "user_name", unique = true, columnDefinition = "VARCHAR")
    private String userName;
	@NotNull
	@Size(min = 4, message ="at least 4 characters long")
	@Column(name = "password", columnDefinition = "VARCHAR")
    private String password;
    @NotNull
    @Column(name = "first_name", columnDefinition = "VARCHAR" )
    private String firstName;
    @NotNull
    @Column(name = "last_name", columnDefinition = "VARCHAR" )
    private String lastName;
    
    @OneToMany(
    		mappedBy="user",
            cascade = CascadeType.ALL, 
            orphanRemoval = true)
    @JsonManagedReference
    private List<Address> address = new ArrayList<>();
    
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
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	} 
	
}
