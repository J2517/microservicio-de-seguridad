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

import com.mc.security.Models.Profile;
import com.mc.security.Models.User;
import com.mc.security.Repositories.ProfileRepository;
import com.mc.security.Repositories.UserRepository;
import com.mc.security.Services.EmailService;
import com.mc.security.Services.EncryptionService;
import com.mc.security.Services.PasswordService;

@CrossOrigin
@RestController
@RequestMapping("/api/public/users")

public class UsersController {

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private ProfileRepository theProfileRepository;

    @Autowired
    private EncryptionService theEncryprionService;

    @Autowired
    private PasswordService thePasswordService;

    @Autowired
    private EmailService theEmailService;

    @GetMapping("")
    public List<User> find() {
        return this.theUserRepository.findAll();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable String id) {
        User theUser = this.theUserRepository.findById(id).orElse(null);
        return theUser;
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        newUser.setPassword(this.theEncryprionService.convertSHA256(newUser.getPassword()));
        return this.theUserRepository.save(newUser);
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User newUser) {
        User actualUser = this.theUserRepository.findById(id).orElse(null);
        if (actualUser != null) {
            actualUser.setName(newUser.getName());
            actualUser.setEmail(newUser.getEmail());
            actualUser.setPassword(this.theEncryprionService.convertSHA256(newUser.getPassword()));
            this.theUserRepository.save(actualUser);
            return actualUser;
        } else {
            return null;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        User theUser = this.theUserRepository.findById(id).orElse(null);
        if (theUser != null) {
            this.theUserRepository.delete(theUser);
        }
    }

    @PostMapping("{userId}/profile/{profileId}")
    public User assignProfileToUser(@PathVariable String userId, @PathVariable String profileId) {
        User theUser = this.theUserRepository.findById(userId).orElse(null);
        Profile theProfile = this.theProfileRepository.findById(profileId).orElse(null);

        if (theUser != null && theProfile != null) {
            theUser.setProfile(theProfile); // Asignar el perfil al usuario
            return this.theUserRepository.save(theUser); // Guardar el usuario con el perfil asignado
        } else {
            return null; // El usuario o el perfil no existen
        }
    }

    @PostMapping("/recover-password")
    public String recoverPassword(@RequestBody User theUser) {
        User theActualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
        if (theActualUser != null) {
            String tokenPassword = thePasswordService.generateToken();
            theActualUser.setPassword(theEncryprionService.convertSHA256(tokenPassword));
            this.theUserRepository.save(theActualUser);
            this.theEmailService.sendEmail(theActualUser.getEmail(), "Recuperación de contraseña",
                    "Su nueva contraseña es: " + tokenPassword);   
            return "Correo enviado";           
        } else {
            return "Usuario no encontrado";
        }
    }

}
