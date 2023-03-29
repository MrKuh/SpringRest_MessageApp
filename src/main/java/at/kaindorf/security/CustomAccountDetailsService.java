package at.kaindorf.security;

import at.kaindorf.database.AccountRepository;
import at.kaindorf.pojos.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
public class CustomAccountDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;

    public CustomAccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Dieser Benutzer wurde nicht gefunden!"));

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                Collections.emptyList()
        );
    }
}
