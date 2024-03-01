package com.warehousekeeper.root.controllers;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.models.Storage;
import com.warehousekeeper.root.services.CustomersService;
import com.warehousekeeper.root.services.StoragesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
/**
 * @author Dmytro
 * @version 1.0
 * Controller responsible for CRUD operations with Storage entity
 */
@Controller
@RequestMapping("/storages")
public class StorageController {

    private final StoragesService storagesService;
    private final CustomersService customersService;
    @Autowired
    public StorageController(StoragesService storagesService, CustomersService customersService) {
        this.storagesService = storagesService;
        this.customersService = customersService;
    }


    @GetMapping("")
    public String showAllStoragesTest(Model model){
        return listByPage(model, 1);
    }
    /**
     * Returns all existing storage
     *
     * @return html page of list of storage and customer using pagenation
     */
    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable ("pageNumber") int currentPage){

        Page<Storage> pageOfStorages = storagesService.findAllWithPages(currentPage);
        long totalStorages = pageOfStorages.getTotalElements();
        int totalPages = pageOfStorages.getTotalPages();

        List<Storage> listOfStorages = pageOfStorages.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalStorages", totalStorages);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("storages", listOfStorages);

        return "storages/all-storages";
    }
    /**
     * Returns storage by id
     *
     * @return html page of storage by id
     */
    @GetMapping("/{id}")
    public String findStorageById(@PathVariable("id") int id, Model model, @ModelAttribute("customer") Customer customer){
        model.addAttribute("storage", storagesService.findStorage(id));

        Customer owner = storagesService.getStorageOwner(id);

        if(owner != null){
            model.addAttribute("owner", owner);
        }else {
            model.addAttribute("customers", customersService.findAll());
        }

        return "storages/show";
    }

    @GetMapping("/new")
    public String addNewStorage(@ModelAttribute("storage") Storage storage){
        return "storages/new";
    }
    /**
     * Create new storage
     *
     * @return html page with all storages
     */
    @PostMapping()
    public String create(@ModelAttribute ("storage") @Valid Storage storage, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "storages/new";
        }
        storagesService.createNewStorage(storage);
        return "redirect:/storages";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("storage", storagesService.findStorage(id));
        return "storages/edit";
    }
    /**
     * Edit storage by id
     *
     * @return html page with all storages
     */
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("storage") @Valid Storage storage,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "storages/edit";
        storagesService.update(id, storage);
        return "redirect:/storages";
    }
    /**
     * Delete storage by id
     *
     * @return html page with all storages
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        storagesService.delete(id);
        return "redirect:/storages";
    }
    /**
     * Assign customer for storage place
     *
     * @return html page with all storages
     */
    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable ("id") int id, @ModelAttribute("customer") Customer selectedCustomer){
        storagesService.assign(id, selectedCustomer);
        return "redirect:/storages/" + id;
    }
    /**
     * Release customer for storage place
     *
     * @return html page with all storages
     */

    @PatchMapping("/{id}/release")
    public String release(@PathVariable ("id") int id){
        storagesService.release(id);
        return "redirect:/storages/" + id;

    }

    @GetMapping("/about")
    public String about(){
        return "storages/about";
    }
}
