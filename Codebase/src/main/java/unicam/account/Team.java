package unicam.account;

public class Team {
    private String nome;
    private int dim;

    public Team(String nome){
        this.nome = nome;
        this.dim = 1;
    }

    public void setDim(int dim){
        this.dim = dim;
    }
}
