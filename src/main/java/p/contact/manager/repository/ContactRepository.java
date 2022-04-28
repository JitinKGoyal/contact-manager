package p.contact.manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import p.contact.manager.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("select u from Contact u where u.customer.id =:userId")
	public Page<Contact> getContactByUser(@Param("userId") Long userId, Pageable pageable);
}
