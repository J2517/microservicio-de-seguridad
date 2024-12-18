// package com.mc.security.Controllers;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
// import org.springframework.security.oauth2.core.OAuth2AccessToken;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.mc.security.Models.Session;
// import com.mc.security.Models.User;
// import com.mc.security.Repositories.SessionRepository;
// import com.mc.security.Repositories.UserRepository;
// import com.mc.security.Services.EmailService;
// import com.mc.security.Services.EncryptionService;
// import com.mc.security.Services.JwtService;

// import io.jsonwebtoken.io.IOException;
// import jakarta.servlet.http.HttpServletResponse;

// @RestController
// public class OAuth2LoginController {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private EncryptionService encryptionService;

//     @Autowired
//     private JwtService jwtService;

//     @Autowired
//     private OAuth2AuthorizedClientService authorizedClientService; // Para obtener el token

//     @Autowired
//     private SessionRepository sessionRepository; // Para manejar las sesiones
//     @Autowired
//     private EmailService emailService; // Para enviar correos

//     @GetMapping("/loginSuccess")
//     public Map<String, Object> getLoginInfo(@AuthenticationPrincipal OAuth2User principal,
//                                             HttpServletResponse response) throws IOException {
//         Map<String, Object> resultResponse = new HashMap<>();

//         // Obtener los atributos del usuario de OAuth2
//         String email = principal.getAttribute("email");
//         String name = principal.getAttribute("name");

//         // Obtener el cliente autorizado
//         OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("github", principal.getName());
//         OAuth2AccessToken accessToken = client.getAccessToken(); // Obtener el token de acceso

//         // Verificar si el usuario ya existe en la base de datos
//         User existingUser = userRepository.getUserByEmail(email);

//         if (existingUser == null) {
//             // Crear un nuevo usuario si no existe
//             User newUser = new User();
//             newUser.setName(name);
//             newUser.setEmail(email);
//             newUser.setPassword(encryptionService.convertSHA256("github_login")); // Establecer una contraseña por defecto
//             userRepository.save(newUser);
//             resultResponse.put("message", "Nuevo usuario creado");
//             resultResponse.put("user", newUser);
//             existingUser = newUser; // Asignar el nuevo usuario a existingUser
//         } else {
//             resultResponse.put("message", "Usuario existente");
//             resultResponse.put("user", existingUser);
//         }

//         // Generar un token JWT para el usuario
//         String token = jwtService.generateToken(existingUser);
//         resultResponse.put("token", token); // Devolver el token en la respuesta

//         // Crear una sesión
//         Session userSession = new Session();
//         userSession.setUser(existingUser); 
//         sessionRepository.save(userSession); // Guardar la sesión en la base de datos

//         // Enviar un correo si es necesario
//         emailService.sendEmail(existingUser.getEmail(), "Inicio de sesión exitoso",
//                 "Se ha creado una nueva sesión para tu cuenta.");

//         return resultResponse;
//     }

//     @GetMapping("/loginFailure")
//     public String loginFailure() {
//         return "Error de autenticación con GitHub.";
//     }

//     //Login con Google
//     @GetMapping("/loginSuccessGoogle")
//     public Map<String, Object> getLoginInfoGoogle(@AuthenticationPrincipal OAuth2User principal,
//                                             HttpServletResponse response) throws IOException {
//         Map<String, Object> resultResponse = new HashMap<>();

//         // Obtener los atributos del usuario de OAuth2
//         String email = principal.getAttribute("email");
//         String name = principal.getAttribute("name");

//         // Obtener el cliente autorizado
//         OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("google", principal.getName());
//         OAuth2AccessToken accessToken = client.getAccessToken(); // Obtener el token de acceso

//         // Verificar si el usuario ya existe en la base de datos
//         User existingUser = userRepository.getUserByEmail(email);

//         if (existingUser == null) {
//             // Crear un nuevo usuario si no existe
//             User newUser = new User();
//             newUser.setName(name);
//             newUser.setEmail(email);
//             newUser.setPassword(encryptionService.convertSHA256("google_login")); // Establecer una contraseña por defecto
//             userRepository.save(newUser);
//             resultResponse.put("message", "Nuevo usuario creado");
//             resultResponse.put("user", newUser);
//             existingUser = newUser; // Asignar el nuevo usuario a existingUser
//         } else {
//             resultResponse.put("message", "Usuario existente");
//             resultResponse.put("user", existingUser);
//         }

//         // Generar un token JWT para el usuario
//         String token = jwtService.generateToken(existingUser);
//         resultResponse.put("token", token); // Devolver el token en la respuesta

//         // Crear una sesión
//         Session userSession = new Session();
//         userSession.setUser(existingUser); // Establecer el usuario para la sesión
//         // Puedes agregar más detalles a la sesión aquí si lo deseas
//         sessionRepository.save(userSession); // Guardar la sesión en la base de datos

//         // Enviar un correo si es necesario
//         emailService.sendEmail(existingUser.getEmail(), "Inicio de sesión exitoso",
//                 "Se ha creado una nueva sesión para tu cuenta.");

//         return resultResponse;
//     }
    
// }




