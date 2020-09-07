package com.yosep.msa.account;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class YoggaebiUserService implements UserDetailsService {

	@Autowired
	YoggaebiUserRepository yoggaebiUserRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public YoggaebiUser saveAccount(YoggaebiUser user) {
		// TODO Auto-generated method stub
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		user.setUserRdate(LocalDateTime.now());
		user.setUserUdate(LocalDateTime.now());
		
		return this.yoggaebiUserRepository.save(user);
	}
	
	public Optional<YoggaebiUser> findUserByUsername(String userName) {
		return this.yoggaebiUserRepository.findByUserName(userName);
	}
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		YoggaebiUser account = yoggaebiUserRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		
		return new User(account.getUserName(), account.getPassword(), authorities(account.getRoles()));
//		return new User("enekelx1", "123123", authorities(account.getRoles()));
	}

	private Collection<? extends GrantedAuthority> authorities(Set<YoggaebiUserRole> roles) {
		// TODO Auto-generated method stub
		return roles.stream().map(r -> {
			return new SimpleGrantedAuthority("ROLE_" + r.name());
		}).collect(Collectors.toSet());
	}

}
