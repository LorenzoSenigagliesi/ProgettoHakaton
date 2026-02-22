package unicam.hackathon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentorihackathon")
public class MentoriHackathon {
    @Id
    @Column(name = "emailstaff", nullable = false)
    private String email;

    @Column(name = "hackathon")
    private String hackathon;

    public MentoriHackathon(String email, String hackathon) {
        this.email = email;
        this.hackathon = hackathon;
    }

    public String getEmail() {
        return email;
    }
    public String getHackathon() {
        return hackathon;
    }
}
