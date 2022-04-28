package p.contact.manager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import p.contact.manager.entity.Contact;
import p.contact.manager.entity.Customer;
import p.contact.manager.helper.Message;
import p.contact.manager.repository.ContactRepository;
import p.contact.manager.repository.CustomerRepository;

@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	private String alert = "a";

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
	public String addContact(Model m, HttpSession session) {
		m.addAttribute("contact", new Contact());

		if (alert.equals("success")) {
			session.setAttribute("message", new Message("contact added successfully", "success"));
		}

		if (alert.equals("fail")) {
			session.setAttribute("message", new Message("contact is not added", "danger"));
		}

		alert = "b";

		return "normal/addContact";
	}

	@PostMapping("/addContactProcess")
	public RedirectView addContactProcess(@ModelAttribute Contact contact, Principal principal,
			@RequestParam("profileImage") MultipartFile file) {

		try {
			File file2 = new ClassPathResource("static/img/").getFile();

			if (file.isEmpty()) {

				contact.setImageUrl("contact.png");
			} else {

				Path location = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);

				contact.setImageUrl(file.getOriginalFilename());
			}

			String userName = principal.getName();
			Customer customer = customerRepository.getCustomerByEmail(userName);

			contact.setCustomer(customer);
			customer.getContacts().add(contact);
			customerRepository.save(customer);
			alert = "success";
			return new RedirectView("/customer/addContact");

		} catch (IOException e) {
			e.printStackTrace();
			alert = "fail";
			return new RedirectView("/customer/addContact");
		}

	}

	@GetMapping("/showContacts/{pageNo}")
	public String showContacts(@PathVariable("pageNo") int pageNo, Model m, Principal principal) {

		String userName = principal.getName();
		Customer customer = customerRepository.getCustomerByEmail(userName);

		Pageable pageable = PageRequest.of(pageNo, 10);

		Page<Contact> contacts = contactRepository.getContactByUser(customer.getId(), pageable);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", pageNo);
		m.addAttribute("totalPage", contacts.getTotalPages());
		m.addAttribute("totalContacts", customer.getContacts().size());

		return "normal/showContacts";
	}

	@GetMapping("/update/{id}")
	public String updateContact(@PathVariable("id") Integer id, Model m) {

		Contact contact = contactRepository.findById(id).get();

		m.addAttribute("contact", contact);
		return "normal/updateContact";

	}

	@PostMapping("/processUpdate")
	public RedirectView processUpdate(@ModelAttribute Contact contact, Principal principal,
			@RequestParam("profileImage") MultipartFile file) {

		try {
			File file2 = new ClassPathResource("static/img/").getFile();

			if (file.isEmpty()) {
				String prevImg = contactRepository.findById(contact.getId()).get().getImageUrl();
				contact.setImageUrl(prevImg);
			} else {

				Path location = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);

				contact.setImageUrl(file.getOriginalFilename());
			}

			String userName = principal.getName();
			Customer customer = customerRepository.getCustomerByEmail(userName);

			contact.setCustomer(customer);
			customer.getContacts().add(contact);
			customerRepository.save(customer);
			alert = "success";

			return new RedirectView("/customer/showContacts/0");

		} catch (IOException e) {
			e.printStackTrace();
			alert = "fail";

			return new RedirectView("/customer/showContacts/0");
		}

	}
	
	
	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id") int id) {
		contactRepository.deleteById(id);
		return "redirect:/customer/showContacts/0";
	}

}
