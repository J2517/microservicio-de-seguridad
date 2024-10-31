package com.mc.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mc.security.Models.Role;
import com.mc.security.Models.User;
import com.mc.security.Models.UserRole;
import com.mc.security.Repositories.RoleRepository;
import com.mc.security.Repositories.UserRepository;
import com.mc.security.Repositories.UserRoleRepository;

@CrossOrigin
@RestController
@RequestMapping("/user_role")
public class UserRoleController {

    @Autowired
    private UserRoleRepository theUserRoleRepository;

    @Autowired
    private RoleRepository theRoleRepository;

    @Autowired
    private UserRepository theUserRepository;

    @PostMapping("user/{userId}/role/{roleId}")
    public UserRole create(@PathVariable String userId,
                           @PathVariable String roleId){
        User theUser=this.theUserRepository.findById(userId).orElse(null);
        Role theRole=this.theRoleRepository.findById(roleId).orElse(null);
        if(theUser!=null && theRole!=null){
            UserRole newUserRole=new UserRole();
            newUserRole.setUser(theUser); 
            newUserRole.setRole(theRole);
            return this.theUserRoleRepository.save(newUserRole);
        }else{
            return null;
        }

    }

    @GetMapping("user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable String userId){
        return this.theUserRoleRepository.getRolesByUser(userId);
    }

    @GetMapping("role/{roleId}")
    public List<UserRole> getUsersByRole(@PathVariable String roleId){
        return this.theUserRoleRepository.getUsersByRole(roleId);
    }


    @PutMapping("{id}/user/{userId}/role/{roleId}")
    public UserRole update(@PathVariable String id,
                           @PathVariable String userId,
                           @PathVariable String roleId) {
        UserRole existingUserRole = this.theUserRoleRepository.findById(id).orElse(null);
        User theUser = this.theUserRepository.findById(userId).orElse(null);
        Role theRole = this.theRoleRepository.findById(roleId).orElse(null);

        if (existingUserRole != null && theUser != null && theRole != null) {
            existingUserRole.setUser(theUser);
            existingUserRole.setRole(theRole);
            return this.theUserRoleRepository.save(existingUserRole);
        } else {
            return null;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        UserRole theUserRole = this.theUserRoleRepository.findById(id).orElse(null);
        if (theUserRole != null) {
            this.theUserRoleRepository.delete(theUserRole);
        }
    }

}
