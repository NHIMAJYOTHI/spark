package com.codethen.bankapi.user.api;

import com.codethen.bankapi.common.api.dto.MessageDTO;
import com.codethen.bankapi.common.security.SecurityUtil;
import com.codethen.bankapi.user.domain.model.User;
import com.codethen.bankapi.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Objects;

import static spark.Spark.halt;
import static spark.Spark.post;

public class UserApi {

    public static void setupEndpoints(
        ObjectMapper mapper,
        UserService userService) {

        post("/login", (req, res) -> {

            final LoginDTO loginDTO = mapper.readValue(req.body(), LoginDTO.class);

            final User user  = userService.findByUsername(loginDTO.username);

            if (user == null || !SecurityUtil.checkPassword(loginDTO.password, user.getPassword())) {
                return halt(HttpStatus.UNAUTHORIZED_401);
            }

            final String jwt = generateJWT(user);
            return mapper.writeValueAsString(new TokenDTO(jwt));
        });

        post("/register", (req, res) -> {

            final RegisterDTO registerDTO = mapper.readValue(req.body(), RegisterDTO.class);

            final User user = new User(registerDTO.username, registerDTO.password);

            userService.create(user);

            return mapper.writeValueAsString(new MessageDTO("ok"));
        });
    }

    private static boolean isPasswordCorrect(String storedPassword, String givenPassword) {
        return Objects.equals(storedPassword, givenPassword);  // TODO: stored password should be encrypted
    }

    private static String generateJWT(User user) {
        return user.getPassword(); // TODO: generate JWT
    }
}