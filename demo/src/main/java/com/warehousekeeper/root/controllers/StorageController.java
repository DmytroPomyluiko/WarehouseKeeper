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

/*    @GetMapping()
    public String showAllStorages(Model model, @RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "storages_per_page", required = false) Integer storagePerPage,
                                  @RequestParam(value = "sort_by_size", required = false) boolean sortBySize){

        if (page == null || storagePerPage == null)
            model.addAttribute("storages", storagesService.findAll(sortBySize)); // выдача всех книг
        else
            model.addAttribute("storages", storagesService.findStoragesWithPagination
                    (page, storagePerPage, sortBySize));
        return "storages/all-storages";
    }*/

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

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("storage") @Valid Storage storage,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "storages/edit";
        storagesService.update(id, storage);
        return "redirect:/storages";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        storagesService.delete(id);
        return "redirect:/storages";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable ("id") int id, @ModelAttribute("customer") Customer selectedCustomer){
        storagesService.assign(id, selectedCustomer);
        return "redirect:/storages/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable ("id") int id){
        storagesService.release(id);
        return "redirect:/storages/" + id;

    }
}
