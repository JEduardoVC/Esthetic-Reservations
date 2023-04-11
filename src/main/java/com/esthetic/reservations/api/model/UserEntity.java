package com.esthetic.reservations.api.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email", "username" }) })
public class UserEntity extends BaseModel<UserEntity> implements UserDetails {

	@Column(name = "username", length = 30, nullable = false)
	private String username;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	@Column(name = "last_name", length = 30, nullable = false)
	private String lastName;

	@Column(name = "phone_number", length = 10, nullable = false)
	private String phoneNumber;

	@Column(name = "address", length = 50, nullable = false)
	private String address;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "password", length = 255, nullable = false)
	private String password;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class, cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
	private Set<Role> userRoles = new HashSet<>();

	public UserEntity() {
		super();
	}

	public UserEntity(Long id, String username, String name, String lastName, String phoneNumber, String address,
			String email, String password, Set<Role> userRoles) {
		super(id);
		this.username = username;
		this.name = name;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
		this.password = password;
		this.userRoles = userRoles;
	}

	public UserEntity(Long id, String username, String name, String lastName, String phoneNumber, String address,
			String email, String password) {
		super(id);
		this.username = username;
		this.name = name;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<Role> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public String toString() {
		return "{" +
				" userId='" + getId() + "'" +
				", username='" + getUsername() + "'" +
				", name='" + getName() + "'" +
				", lastName='" + getLastName() + "'" +
				", phoneNumber='" + getPhoneNumber() + "'" +
				", address='" + getAddress() + "'" +
				", email='" + getEmail() + "'" +
				", password='" + getPassword() + "'" +
				", userRoles='" + getUserRoles() + "'" +
				"}";
	}

	public void copy(UserEntity user) {
		this.username = user.username;
		this.name = user.name;
		this.lastName = user.lastName;
		this.phoneNumber = user.phoneNumber;
		this.address = user.address;
		this.email = user.email;
		this.password = user.password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getUserRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
