package com.warehousekeeper.root.services;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.models.Storage;
import com.warehousekeeper.root.repositories.CustomersRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomersService {

    private final CustomersRepository customersRepository;
    @Autowired
    public CustomersService(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }


    public Page<Customer> findAllWithPages(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5, Sort.by("fullName"));
        return customersRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public List<Customer> findAll(){
        return customersRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Customer findById(int id){
        Customer customer = customersRepository.findById(id).orElseThrow(null);
        return customer;
    }

    @Transactional
    public void createNewCustomer(Customer customer){
        customersRepository.save(customer);
    }

    @Transactional
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(int id, Customer updateCustomer){
        updateCustomer.setId(id);
        customersRepository.save(updateCustomer);
    }

    @Transactional
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(int id){
        customersRepository.deleteById(id);
    }

    public Optional<Customer> findCustomerByFullName(String name){
        return customersRepository.findCustomerByFullName(name);
    }

    public List<Customer> searchByName(String query){
        return customersRepository.findByFullNameStartingWith(query);
    }
    @Transactional
    public List<Storage> getStoragesByPersonId(int id) {
        Optional<Customer> customer = customersRepository.findById(id);

        if (customer.isPresent()) {
            Hibernate.initialize(customer.get().getStorages());

            customer.get().getStorages().forEach(storage -> {
                long diffInMillies = Math.abs(storage.getTakenAt().getTime() - new Date().getTime());
                // 10 days
                if (diffInMillies > 864000000)
                    storage.setExpired(true);
            });

            return customer.get().getStorages();
        }
        else {
            return Collections.emptyList();
        }
    }


}
