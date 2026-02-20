package unicam.account;
import jakarta.persistence.*;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "dim")
    private int dim;

    // Costruttore vuoto richiesto da JPA
    protected Team() {}

    public Team(String nome){
        this.nome = nome;
        this.dim = 1;
    }

    public void setDim(int dim){
        this.dim = dim;
    }

    public String getNome() {
        return nome;
    }

    public int getDim() {
        return dim;
    }
}
