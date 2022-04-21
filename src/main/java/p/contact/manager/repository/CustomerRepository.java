package p.contact.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import p.contact.manager.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	@Query("select u from Customer u where u.email = :email")
	public Customer getCustomerByEmail(@Param("email") String email);
}
