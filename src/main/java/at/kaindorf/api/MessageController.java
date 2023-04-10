package at.kaindorf.api;


import at.kaindorf.database.AccountRepository;
import at.kaindorf.database.MessageRepository;
import at.kaindorf.pojos.Account;
import at.kaindorf.pojos.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value ="/api/messages",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageController(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.of(Optional.of(messageRepository.findAll()));
    }

    @GetMapping("/add")
    public ResponseEntity<Message> addMessage(HttpServletRequest request, @RequestParam(name = "message") String messagetxt) {
        String email = (String) request.getAttribute("mail");

        Account account = accountRepository.findByEmail(email).orElse(null);

        Message message = new Message(messagetxt, account);
        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }

}
