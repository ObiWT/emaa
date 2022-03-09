package sk.emaa.rest.service.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("user")
public class User {

	@Id
	private String id;
	private String username;
	private String password;
	private String schoolId;
	private Role role;
	private String token;
	private String city;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "username: " + username + ", schoolId: " + schoolId + ", token: " + token;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
