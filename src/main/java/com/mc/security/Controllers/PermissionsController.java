package com.mc.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mc.security.Models.Permission;
import com.mc.security.Repositories.PermissionRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;
    @GetMapping("")
    public List<Permission> findAll(){
        return this.thePermissionRepository.findAll();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission create(@RequestBody Permission theNewPermission){
        return this.thePermissionRepository.save(theNewPermission);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        if (thePermission != null) {
            this.thePermissionRepository.delete(thePermission);
        }
    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission updatedPermission) {
        Permission existingPermission = this.thePermissionRepository.findById(id).orElse(null);

        if (existingPermission != null) {
            existingPermission.setUrl(updatedPermission.getUrl());
            existingPermission.setMethod(updatedPermission.getMethod());
            return this.thePermissionRepository.save(existingPermission);
        } else {
            return null; // Manejar error en caso de no encontrar el recurso
        }
    }
}
