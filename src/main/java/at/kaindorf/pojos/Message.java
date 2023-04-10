package at.kaindorf.pojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;
    @Column(length = 256)
    private String content;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender;

    public Message(String content, Account sender) {
        this.content = content;
        this.sender = sender;
    }
}
