package com.mc.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mc.security.Models.Role;
import com.mc.security.Repositories.RoleRepository;

@CrossOrigin
@RestController
@RequestMapping("/roles")

public class RoleController {

    @Autowired
    private RoleRepository theRoleRepository;

    // Obtener todos los roles
    @GetMapping("")
    public List<Role> find() {
        return this.theRoleRepository.findAll();
    }

    // Obtener un rol por su ID
    @GetMapping("{id}")
    public Role findById(@PathVariable String id) {
        return this.theRoleRepository.findById(id).orElse(null);
    }

    // Crear un nuevo rol
    @PostMapping
    public Role create(@RequestBody Role newRole) {
        return this.theRoleRepository.save(newRole);
    }


    // Eliminar un rol por su ID
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Role theRole = this.theRoleRepository.findById(id).orElse(null);
        if (theRole != null) {
            this.theRoleRepository.delete(theRole);
        }
    }

// Actualizar un rol por su ID
    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role updatedRole) {
        Role existingRole = this.theRoleRepository.findById(id).orElse(null);
        if (existingRole != null) {
            existingRole.setName(updatedRole.getName());
            existingRole.setDescription(updatedRole.getDescription());
            return this.theRoleRepository.save(existingRole);
        } else {
            return null;
        }
    }
}

