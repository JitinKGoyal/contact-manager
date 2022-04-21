package p.contact.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import p.contact.manager.entity.Customer;
import p.contact.manager.repository.CustomerRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Customer customer = customerRepository.getCustomerByEmail(username);
		
		if (customer == null) {
			throw new UsernameNotFoundException("could not found user");	
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(customer);
		return customUserDetails;
	}

}
