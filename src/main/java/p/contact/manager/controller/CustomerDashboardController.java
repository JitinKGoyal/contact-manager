package p.contact.manager.controller;

import java.lang.ProcessBuilder.Redirect;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import p.contact.manager.entity.Contact;
import p.contact.manager.entity.Customer;
import p.contact.manager.repository.CustomerRepository;

@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

	@Autowired
	CustomerRepository customerRepository;


//	@Autowired
//	contact
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@ModelAttribute
	public void commonData(Principal principal, Model model) {
		String userName = principal.getName();
		Customer customer = customerRepository.getCustomerByEmail(userName);
		model.addAttribute("customer", customer);

	}

	@GetMapping("/index")
	public String customerDash() {
		return "normal/index";
	}

	@GetMapping("/")
	public String home() {
		return "normal/home";
	}

	@GetMapping("/addContact")
	public String addContact(Model m)  {
		m.addAttribute("contact", new Contact());
		return "normal/addContact";
	}
	
	
	@PostMapping("/addContactProcess")
	public RedirectView addContactProcess(@ModelAttribute Contact contact,Principal principal) {
		
		String userName = principal.getName();
		Customer customer = customerRepository.getCustomerByEmail(userName);
		
		contact.setCustomer(customer);
		customer.getContacts().add(contact);
		customerRepository.save(customer);
		
		return new RedirectView("/customer/addContact") ;
				
	}
}
