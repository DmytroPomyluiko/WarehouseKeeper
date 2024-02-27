package com.warehousekeeper.root.controllers.rest;

import com.warehousekeeper.root.dao.CustomerDao;
import com.warehousekeeper.root.dto.CustomerDto;
import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.services.CustomersService;
import com.warehousekeeper.root.services.StoragesService;
import com.warehousekeeper.root.util.CustomerErrorResponse;
import com.warehousekeeper.root.util.CustomerNotCreatedException;
import com.warehousekeeper.root.util.CustomerNotFoundException;
import com.warehousekeeper.root.util.CustomerValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class RestCustomerController {

    private final CustomersService customersService;
    private final CustomerValidator customerValidator;
    private final StoragesService storagesService;
    private final CustomerDao customerDao;
    private final ModelMapper modelMapper;
    @Autowired
    public RestCustomerController(CustomersService customersService, CustomerValidator customerValidator,
                                  StoragesService storagesService, CustomerDao customerDao, ModelMapper modelMapper) {
        this.customersService = customersService;
        this.customerValidator = customerValidator;
        this.storagesService = storagesService;
        this.customerDao = customerDao;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public List<Customer> showAllCustomer(){
        return customerDao.index();
    }


    @GetMapping("/{id}")
    public Customer findCustomerById(@PathVariable("id") int id){
        return customerDao.findById(id);
    }


    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CustomerDto customerDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError fr : errors){
                stringBuilder.append(fr.getField()).append(" - ").append(fr.getDefaultMessage())
                        .append(";");
            }
            throw  new CustomerNotCreatedException(stringBuilder.toString());
        }
        customersService.createNewCustomer(convertToPerson(customerDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    private Customer convertToPerson(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }
    @ExceptionHandler
    private ResponseEntity<CustomerErrorResponse> handlerExceptionNotCreatedCustomer(CustomerNotCreatedException e){
        CustomerErrorResponse personErrorResponse =
                new CustomerErrorResponse(e.getMessage(), new Date());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<CustomerErrorResponse> handlerExceptionNotFoundCustomer(CustomerNotFoundException e){
        CustomerErrorResponse personErrorResponse =
                new CustomerErrorResponse("Customer with this id was not found", new Date());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }
//
//    @GetMapping("/new")
//    public String addNewCustomer(@ModelAttribute("customer") Customer customer){
//        return "customers/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("customer") @Valid Customer customer, BindingResult bindingResult){
//        customerValidator.validate(customer,bindingResult);
//        if(bindingResult.hasErrors())
//            return "customers/new";
//
//        customersService.createNewCustomer(customer);
//        return "redirect:/customers";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(@PathVariable("id") int id, Model model){
//        model.addAttribute("customer", customersService.findById(id));
//        return "customers/edit";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("customer") @Valid Customer customer, BindingResult bindingResult,
//                         @PathVariable("id") int id){
//        customerValidator.validate(customer,bindingResult);
//        if(bindingResult.hasErrors())
//            return "customers/edit";
//
//        customersService.update(id,customer);
//        return "redirect:/customers";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id){
//        customersService.delete(id);
//        return "redirect:/customers";
//    }
//
//    @GetMapping("/search")
//    public String searchPage() {
//        return "customers/search";
//    }
//
//    @PostMapping("/search")
//    public String makeSearch(Model model, @RequestParam("query") String query) {
//        model.addAttribute("customers", customersService.searchByName(query));
//        return "customers/search";
//    }

}
