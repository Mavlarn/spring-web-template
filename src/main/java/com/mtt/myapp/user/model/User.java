package com.mtt.myapp.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.mtt.myapp.common.model.BaseEntity;
import org.hibernate.annotations.Index;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "t_users")
public class User extends BaseEntity<User> implements UserDetails {

    /**
     * serial version id.
     */
    private static final long serialVersionUID = 1L;

	@Column(name = "userid", unique = true, nullable = false)
	@Index(name = "userid_index")
	private String userId;

    @Column(name = "username", nullable = false)
    private String userName;


	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", nullable = false)
	private Role role;

    @Column(unique = true, nullable = false, length = 64)
    private String email;

    @Column(nullable = true)
    private Date birth;

    @Column(nullable = false, length = 64)
    private String password;

	@Column(name = "use_language", nullable = true)
	private String userLanguage;

	@Column(nullable = true)
	private String timezone;

	private boolean enabled = true;  //default true


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    public String getUsername() {
        return userName;
    }

	public void setUserName(String name) {
        this.userName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>(1);
		//roles.add(new SimpleGrantedAuthority("ROLE_" + getRole().getShortName()));
		roles.add(new SimpleGrantedAuthority(getRole().getShortName()));
		return roles;
	}

}
