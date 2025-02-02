package fr.fanto.premierstudiosapi.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.fanto.premierstudiosapi.entities.User;
import fr.fanto.premierstudiosapi.exceptions.InternalServerErrorException;
import fr.fanto.premierstudiosapi.exceptions.UserAlreadyExistsException;
import fr.fanto.premierstudiosapi.models.ApiResponse;
import fr.fanto.premierstudiosapi.models.LoginRequest;
import fr.fanto.premierstudiosapi.repositories.UserRepo;
import fr.fanto.premierstudiosapi.utils.JwtUtil;
import fr.fanto.premierstudiosapi.validator.UserValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<String> doRegister(UserValidator userValidator) {
        User user = User.builder()
                        .email(userValidator.getEmail())
                        .password(passwordEncoder.encode(userValidator.getPassword()))
                        .username(userValidator.getUsername())
                        .build();
        if (userRepo.existsById(user.getEmail())) throw new UserAlreadyExistsException("User already exists with this email");
        try {
            userRepo.save(user);
            return new ApiResponse<>(true, 200, "User created successfully", null);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create the user due to a database error");
        }
    }

    public ApiResponse<String> dologin(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails); // Utilisation de UserDetails pour inclure le rôle

            return new ApiResponse<>(true, 200, "Login successful", token);

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return new ApiResponse<>(false, 401, "Invalid email or password", null);
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong during login");
        }
    }

    @Override
    public User loadUserByUsername(String email) {
        return userRepo.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
