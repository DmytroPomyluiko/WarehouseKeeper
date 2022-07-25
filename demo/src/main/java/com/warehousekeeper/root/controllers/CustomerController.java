package com.warehousekeeper.root.controllers;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.models.Storage;
import com.warehousekeeper.root.services.CustomersService;
import com.warehousekeeper.root.services.StoragesService;
import com.warehousekeeper.root.util.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomersService customersService;
    private final CustomerValidator customerValidator;
    private final StoragesService storagesService;
    @Autowired
    public CustomerController(CustomersService customersService, CustomerValidator customerValidator,
                              StoragesService storagesService) {
        this.customersService = customersService;
        this.customerValidator = customerValidator;
        this.storagesService = storagesService;
    }

    @GetMapping("")
    public String showAllCustomer(Model model){
        return listByPage(model, 1);
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable ("pageNumber") int currentPage){

        Page<Customer> pageOfCustomers = customersService.findAllWithPages(currentPage);
        long totalCustomers = pageOfCustomers.getTotalElements();
        int totalPages = pageOfCustomers.getTotalPages();

        List<Customer> listOfCustomers = pageOfCustomers.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("customers", listOfCustomers);

        return "customers/all-customers";
    }



/*    @GetMapping()
    public String allCustomers(Model model){
        model.addAttribute("customers", customersService.findAll());
        return "customers/all-customers";
    }*/

    @GetMapping("/{id}")
    public String findCustomerById(@PathVariable("id") int id, Model model){
        Customer customer = customersService.findById(id);
        model.addAttribute("customer", customer);
        model.addAttribute("storages", customersService.getStoragesByPersonId(id));

        return "customers/show";
    }

    @GetMapping("/new")
    public String addNewCustomer(@ModelAttribute("customer") Customer customer){
        return "customers/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("customer") @Valid Customer customer, BindingResult bindingResult){
        customerValidator.validate(customer,bindingResult);
        if(bindingResult.hasErrors())
            return "customers/new";

        customersService.createNewCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("customer", customersService.findById(id));
        return "customers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("customer") @Valid Customer customer, BindingResult bindingResult,
                         @PathVariable("id") int id){
        customerValidator.validate(customer,bindingResult);
        if(bindingResult.hasErrors())
            return "customers/edit";

        customersService.update(id,customer);
        return "redirect:/customers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        customersService.delete(id);
        return "redirect:/customers";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "customers/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("customers", customersService.searchByName(query));
        return "customers/search";
    }

}
