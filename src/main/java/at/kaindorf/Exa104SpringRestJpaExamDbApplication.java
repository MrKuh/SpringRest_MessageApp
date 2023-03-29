package at.kaindorf;

import at.kaindorf.database.AccountRepository;
import at.kaindorf.pojos.Account;
import at.kaindorf.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Exa104SpringRestJpaExamDbApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AccountRepository userRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public static void main(String[] args) {
		SpringApplication.run(Exa104SpringRestJpaExamDbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Account user = new Account();
			user.setEmail("renbea18@htl-kaindorf.at");
			user.setPassword(passwordEncoder.encode("1234"));

			if(!userRepository.findByEmail(user.getEmail()).isPresent()){
				Account saved = userRepository.save(user);
				System.out.println(jwtTokenProvider.generateToken(saved.getEmail()));
			}

		} catch (Exception e) {
			//
		}
	}
}
