package com.genesis.microservice.integration.authserver.security;

import com.genesis.microservice.integration.authserver.domain.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import java.util.List;

/**
 * Created by Aizhanglin on 2017/10/23.
 */
public class UserDetailService extends JdbcDaoImpl {
    private final String usersByUsernameQuery="select username,password,tel,email,enabled,account_non_expired,credentials_non_expired,account_non_locked from users where username = ?";
    private final String authoritiesByUsernameQuery = "select username,authority from authorities where username = ?";
    private final String groupAuthoritiesByUsernameQuery = "select g.id, g.name, ga.authority from groups g, group_members gm, group_authorities ga where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
    private final String registUserSql="insert users(id,username,password,tel,email,enable,account_non_expired,credentials_non_expired,account_non_locked) VALUES (?,?,?,?,?,?,?,?,?)";

    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {
        return this.getJdbcTemplate().query(this.usersByUsernameQuery, new String[]{username}, (RowMapper) (rs, rowNum) -> {
            String username1 = rs.getString(1);
            String password = rs.getString(2);
            String tel = rs.getString(3);
            String email = rs.getString(4);
            boolean enabled = rs.getBoolean(5);
            boolean accountNonExpired = rs.getBoolean(6);
            boolean credentialsNonExpired = rs.getBoolean(7);
            boolean accountNonLocked = rs.getBoolean(8);
            return new User(username1,tel,email, password,enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, AuthorityUtils.NO_AUTHORITIES);
        });
    }

    @Override
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        User user= (User) userFromUserQuery;
        String returnUsername = userFromUserQuery.getUsername();
        if(!isUsernameBasedPrimaryKey()) {
            returnUsername = username;
        }
        return new User(returnUsername,user.getTel(),user.getEmail(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), combinedAuthorities);
    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        return this.getJdbcTemplate().query(this.authoritiesByUsernameQuery, new String[]{username}, (RowMapper) (rs, rowNum) -> {
            String roleName = getRolePrefix() + rs.getString(2);
            return new SimpleGrantedAuthority(roleName);
        });
    }

    @Override
    protected List<GrantedAuthority> loadGroupAuthorities(String username) {
        return this.getJdbcTemplate().query(this.groupAuthoritiesByUsernameQuery, new String[]{username}, (RowMapper) (rs, rowNum) -> {
            String roleName = getRolePrefix() + rs.getString(3);
            return new SimpleGrantedAuthority(roleName);
        });
    }

    public void registUser(User user){
        Object[] args=new Object[]{user.getId(),user.getUsername(),user.getPassword(),user.getTel(),user.getEmail(),true,true,true,true};
        this.getJdbcTemplate().update(this.registUserSql,args);
    }
}
