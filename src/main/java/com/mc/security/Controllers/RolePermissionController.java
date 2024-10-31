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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mc.security.Models.Permission;
import com.mc.security.Models.Role;
import com.mc.security.Models.RolePermission;
import com.mc.security.Repositories.PermissionRepository;
import com.mc.security.Repositories.RolePermissionRepository;
import com.mc.security.Repositories.RoleRepository;

@CrossOrigin
@RestController
@RequestMapping("/role-permission")
public class RolePermissionController {

    @Autowired
    private RolePermissionRepository theRolePermissionRepository;
    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private RoleRepository theRoleRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("role/{roleId}/permission/{permissionId}")
    public RolePermission create(@PathVariable String roleId,
            @PathVariable String permissionId) {
        Role theRole = this.theRoleRepository.findById(roleId)
                .orElse(null);
        Permission thePermission = this.thePermissionRepository.findById((permissionId))
                .orElse(null);
        if (theRole != null && thePermission != null) {
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setRole(theRole);
            newRolePermission.setPermission(thePermission);
            return this.theRolePermissionRepository.save(newRolePermission);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        RolePermission theRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        if (theRolePermission != null) {
            this.theRolePermissionRepository.delete(theRolePermission);
        }
    }

    @GetMapping("role/{roleId}")
    public List<RolePermission> findPermissionsByRole(@PathVariable String roleId) {
        return this.theRolePermissionRepository.getPermissionsByRole(roleId);
    }

    @PutMapping("{id}/role/{roleId}/permission/{permissionId}")
    public RolePermission update(@PathVariable String id,
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        RolePermission existingRolePermission = this.theRolePermissionRepository.findById(id).orElse(null);
        Role theRole = this.theRoleRepository.findById(roleId).orElse(null);
        Permission thePermission = this.thePermissionRepository.findById(permissionId).orElse(null);

        if (existingRolePermission != null && theRole != null && thePermission != null) {
            existingRolePermission.setRole(theRole);
            existingRolePermission.setPermission(thePermission);
            return this.theRolePermissionRepository.save(existingRolePermission);
        } else {
            return null;
        }
    }


}
