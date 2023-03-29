package at.kaindorf.api;

import at.kaindorf.database.AccountRepository;
import at.kaindorf.pojos.Account;
import at.kaindorf.request.AuthRequest;
import at.kaindorf.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    public AuthController(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Account> register(@RequestBody AuthRequest authRequest) {
        Optional<Account> userOptional = accountRepository.findByEmail(authRequest.getEmail());

        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Account account = new Account();
        account.setEmail(authRequest.getEmail());
        account.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        Account created = accountRepository.save(account);

        return ResponseEntity.ok(created);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        return ResponseEntity.ok(jwtTokenProvider.generateToken(authentication));
    }

}
