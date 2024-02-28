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
import java.util.Optional;

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
    public List<Customer> showAllCustomer() {
        return customerDao.index();
    }


    @GetMapping("/{id}")
    public Customer findCustomerById(@PathVariable("id") int id) {
        return customerDao.findById(id);
    }


    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CustomerDto customerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = getExceptionMessage(bindingResult);
            throw new CustomerNotCreatedException(stringBuilder.toString());
        }
        customersService.createNewCustomer(convertToPerson(customerDto));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid CustomerDto customerDto, BindingResult bindingResult,
                                             @PathVariable("id") int id) {
        customerValidator.validate(convertToPerson(customerDto), bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = getExceptionMessage(bindingResult);
            throw new CustomerNotCreatedException(stringBuilder.toString());
        }

        customersService.update(id,convertToPerson(customerDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<CustomerErrorResponse> handlerExceptionNotCreatedCustomer(CustomerNotCreatedException e) {
        CustomerErrorResponse personErrorResponse =
                new CustomerErrorResponse(e.getMessage(), new Date());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<CustomerErrorResponse> handlerExceptionNotFoundCustomer(CustomerNotFoundException e) {
        CustomerErrorResponse personErrorResponse =
                new CustomerErrorResponse("Customer with this id/name was not found", new Date());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        customersService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping ("/search")
    public ResponseEntity<List<Customer>> makeSearch(@RequestParam("query") String query) {
        List<Customer> customers = customerDao.findByFirstTwoLettersIgnoreCase(query);
        if (customers.size() == 0)
            throw new CustomerNotFoundException();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    private static StringBuilder getExceptionMessage(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError fr : errors) {
        stringBuilder.append(fr.getField()).append(" - ").append(fr.getDefaultMessage())
                .append(";");
    }
        return stringBuilder;
}
    private Customer convertToPerson(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }
}
