package com.mc.security.Controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mc.security.Models.Permission;
import com.mc.security.Models.Session;
import com.mc.security.Models.User;
import com.mc.security.Repositories.SessionRepository;
import com.mc.security.Repositories.UserRepository;
import com.mc.security.Services.EmailService;
import com.mc.security.Services.EncryptionService;
import com.mc.security.Services.JwtService;
import com.mc.security.Services.ValidatorsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @CrossOrigin
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private EncryptionService theEncryptionService;
    @Autowired
    private JwtService theJwtService;
    @Autowired
    private SessionRepository theSessionRepository;
    @Autowired
    private EmailService theEmailService;
    @Autowired
    private ValidatorsService theValidatorsService;
    @Autowired
    private com.mc.security.Services.RecaptchaService RecaptchaService;

    // @PostMapping("/login")
    // public HashMap<String, Object> login(@RequestBody User theNewUser,
    //         final HttpServletResponse response) throws IOException {
    //     HashMap<String, Object> theResponse = new HashMap<>();
    //     User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
    //     if (theActualUser != null
    //             && theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
    //         String token2FA = PasswordService.generateToken();
    //         theActualUser.setPassword("");
    //         theResponse.put("user", theActualUser);
    //         theResponse.put("token2FA", token2FA);
    //         //crear sesion
    //         Session theUserSession = new Session();
    //         theUserSession.setUser(theActualUser); // Establece el usuario para la sesión
    //         theUserSession.setToken2FA(token2FA); // Guarda el token 2FA
    //         theUserSession.setExpiration("holi");
    //         theSessionRepository.save(theUserSession); // Guarda la sesión en la base de datos
    //         // Enviar token 2FA por correo
    //         this.theEmailService.sendEmail(theActualUser.getEmail(), "Código de verificación",
    //                 "Sus datos de incio de sesión son: Id: " + theUserSession.get_id() + " Token: " + token2FA);
    //         return theResponse;
    //     } else {
    //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    //         return theResponse;
    //     }
    // }
    // // Verificar token y generar jwt
    // @PostMapping("/verify2fa")
    // public String verify2FA(@RequestBody Session theSession, final HttpServletResponse response) throws IOException {
    //     Session theUserSession = this.theSessionRepository.findById(theSession.get_id()).orElse(null);
    //     if (theUserSession != null && theUserSession.getToken2FA().equals(theSession.getToken2FA())) {
    //         // Generar token JWT
    //         String token = this.theJwtService.generateToken(theUserSession.getUser());
    //         // Actualizar la sesión
    //         theUserSession.setToken(token);
    //         theSessionRepository.save(theUserSession);
    //         return token;
    //     } else {}
    //     else {
    //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Código de verificación incorrecto");
    //         return null;
    //     }
    // }
    @PostMapping("/verify2fa")
    public String verify2FA(@RequestBody Session theSession, final HttpServletResponse response) throws IOException {
        Session theUserSession = this.theSessionRepository.findById(theSession.get_id()).orElse(null);
        if (theUserSession != null) {
            if (theUserSession.getToken2FA().equals(theSession.getToken2FA())) {
                // Generar token JWT
                String token = this.theJwtService.generateToken(theUserSession.getUser());

                // Actualizar la sesión
                theUserSession.setToken(token);
                theSessionRepository.save(theUserSession);
                return token;
            } else {
                //agregar el contador intentosFallidos de session 1 
                theUserSession.setIntentosFallidos(theUserSession.getIntentosFallidos() + 1);
                theSessionRepository.save(theUserSession);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Código de verificación incorrecto");
                return null;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión no encontrada");
            return null;
        }
    }

    @GetMapping("/failed-attempts/{userId}")
    public int getFailedAttempts(@PathVariable String userId) {
        // Obtener todas las sesiones del usuario
        List<Session> sessions = this.theSessionRepository.getSessionsByUser(userId);
        //sumar los intentos fallidos
        int failedAttempts = 0;
        for (Session session : sessions) {
            failedAttempts += session.getIntentosFallidos();
        }
        return failedAttempts;

    }

    @PostMapping("permissions-validation")
    public boolean permissionsValidation(final HttpServletRequest request,
            @RequestBody Permission thePermission) {
        boolean success = this.theValidatorsService.validationRolePermission(request, thePermission.getUrl(), thePermission.getMethod());
        return success;
    }

    // @PostMapping("/login")
    // public HashMap<String, Object> login(@RequestBody User theNewUser,
    //         final HttpServletResponse response) throws IOException {
    //     HashMap<String, Object> theResponse = new HashMap<>();
    //     String token = "";
    //     User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
    //     if (theActualUser != null
    //             && theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
    //         token = theJwtService.generateToken(theActualUser);
    //         theActualUser.setPassword("");
    //         theResponse.put("token", token);
    //         theResponse.put("user", theActualUser);
    //         return theResponse;
    //     } else {
    //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    //         return theResponse;
    //     }
    // // }
    // @PostMapping("/login")
    // public HashMap<String, Object> login(@RequestBody Session theSession, final HttpServletResponse response) throws IOException {
    //     // Validar el token de reCAPTCHA
    //     boolean isRecaptchaValid = RecaptchaService.verifyRecaptcha(theSession.getRecaptchaToken(), "verify2fa");
    //     if (!isRecaptchaValid) {
    //         response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid reCAPTCHA token");
    //         return new HashMap<>();
    //     }
    //     return new HashMap<>();
    // }
// @PostMapping("/login")
// public HashMap<String, Object> login(@RequestBody User theNewUser,
//                                      @RequestParam String recaptchaToken, 
//                                      final HttpServletResponse response,
//                                      HttpSession session) throws IOException {
//     HashMap<String, Object> theResponse = new HashMap<>();
//     // Verificar las credenciales del usuario
//     User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
//     if (theActualUser != null && 
//         theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
//                 // Obtener el token de reCAPTCHA de la sesión
//     String sessionRecaptchaToken = (String) session.getAttribute("recaptchaToken");
//     if (sessionRecaptchaToken == null || !sessionRecaptchaToken.equals(recaptchaToken)) {
//         response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid reCAPTCHA token");
//         return theResponse;  // Retorna respuesta vacía si el reCAPTCHA no es válido
//     }
//         // Si el usuario es válido, generar el JWT
//         String token = theJwtService.generateToken(theActualUser);
//         theActualUser.setPassword("");  // Limpiar la contraseña antes de enviarla en la respuesta
//         // Responder con el token JWT y los detalles del usuario
//         theResponse.put("token", token);
//         theResponse.put("user", theActualUser);
//         return theResponse;
//     } else {
//         // Si las credenciales son incorrectas
//         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
//         return theResponse;
//     }
// }
    @PostMapping("/login")
    public HashMap<String, Object> login(@RequestBody User theNewUser,
            final HttpServletResponse response) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();
        String token = "";
        User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());
        if (theActualUser != null
                && theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
            token = theJwtService.generateToken(theActualUser);
            theActualUser.setPassword("");
            theResponse.put("token", token);
            theResponse.put("user", theActualUser);
            return theResponse;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return theResponse;
        }

    }

}
