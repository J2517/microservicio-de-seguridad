package com.mc.security.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret-key}")
    private String secretKey; // Clave secreta para verificar el reCAPTCHA

    @Value("${recaptcha.verify-url}")
    private String verifyUrl; // URL de verificación de Google

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Verifica la validez del token de reCAPTCHA enviado desde el frontend.
     * 
     * @param token El token generado en el cliente (frontend) por reCAPTCHA.
     * @param expectedAction Acción esperada (p. ej., "login") para validar el contexto.
     * @return `true` si el reCAPTCHA es válido y cumple con las condiciones.
     */
    public boolean verifyRecaptcha(String token, String expectedAction) {
        if (token == null || token.isEmpty()) {
            return false; // Token vacío o nulo
        }

        String url = verifyUrl + "?secret=" + secretKey + "&response=" + token;

        // Crear encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            // Realizar la solicitud POST a la API de Google
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !Boolean.TRUE.equals(responseBody.get("success"))) {
                return false; // Token inválido
            }

            // Opcional: Validar el puntaje y la acción esperada
            Double score = (Double) responseBody.get("score");
            String action = (String) responseBody.get("action");

            return score != null && score >= 0.5 && expectedAction.equals(action);
        } catch (Exception e) {
            // Manejar errores de conexión o excepciones
            return false;
        }
    }
}
