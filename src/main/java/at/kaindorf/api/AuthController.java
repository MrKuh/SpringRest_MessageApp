package at.kaindorf.api;

import at.kaindorf.database.AccountRepository;
import at.kaindorf.pojos.Account;
import at.kaindorf.request.AuthRequest;
import at.kaindorf.security.JwtTokenProvider;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private static final Gson gson = new Gson();

    public AuthController(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }
//The code first checks if an account with the given email already exists in the account repository by calling the
    //"findByEmail" method on the "accountRepository" object, which returns an optional containing the account if found.
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
    //The code first performs authentication by calling the "authenticate" method on the "authenticationManager" object.
    //The "authenticationManager" is responsible for authenticating the user's credentials against the configured
    //authentication provide.
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(gson.toJson(token));
    }

}
