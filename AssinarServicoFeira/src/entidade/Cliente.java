package entidade;

/**
 * Classe de Entidade (Dominio) - Representa o Cliente que assina o servico de feira.
 * Correspondencia com o Diagrama de Classes: atributos privados (-) e metodos publicos (+).
 */
public class Cliente {
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;

    public Cliente() {}

    public Cliente(String cpf, String nome, String email, String telefone, String endereco) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return "Cliente [CPF=" + cpf + ", Nome=" + nome + ", Email=" + email +
               ", Telefone=" + telefone + ", Endereco=" + endereco + "]";
    }
}
