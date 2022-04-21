package p.contact.manager.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import p.contact.manager.entity.Customer;
import p.contact.manager.helper.Message;
import p.contact.manager.repository.CustomerRepository;

@Controller
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("admin/welcome")
	@ResponseBody
	public String welcome() {

		return "welcome";
	}

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
	m.addAttribute("customer", new Customer());
		return "signup";
	}

	@PostMapping("/processSignupForm")
	public String processSignupForm( @Validated({Customer.class}) @ModelAttribute("customer")  Customer customer,final BindingResult r, HttpSession session, Model m) {
	
			customer.setRole("ROLE_USER");
			customer.setEnabled(true);
			customer.setImageUrl("Demo_Url");

			m.addAttribute("customer",customer);
			
			customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
			
			customerRepository.save(customer);

			session.setAttribute("message", new Message("Registration successfully done", "alert-success"));
			return "signup";
			
	

	}
	
	
	@GetMapping("/login")
	public String customLogin(Model model) {
		return "login";
	}
}



















