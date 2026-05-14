package entidade;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Entidade (Dominio) - Representa o Cliente que assina o servico de feira.
 * Conforme Diagrama de Classes:
 * - Atributos privados (-) e metodos publicos (+)
 * - Relacionamento 1:1..* com Endereco
 * - Relacionamento 1:0..* com Assinatura
 */
public class Cliente {
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private List<Endereco> enderecos; // Pode ter multiplos enderecos
    private List<Assinatura> assinaturas; // Historico de assinaturas

    public Cliente() {
        this.enderecos = new ArrayList<>();
        this.assinaturas = new ArrayList<>();
    }

    public Cliente(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.enderecos = new ArrayList<>();
        this.assinaturas = new ArrayList<>();
    }

    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public List<Endereco> getEnderecos() { return enderecos; }
    public List<Assinatura> getAssinaturas() { return assinaturas; }

    /**
     * Vincula um endereco ao cliente.
     * Conforme diagrama de classes: vincularEndereco(Endereco end)
     */
    public void vincularEndereco(Endereco endereco) {
        if (endereco != null && !enderecos.contains(endereco)) {
            enderecos.add(endereco);
        }
    }

    /**
     * Remove um endereco do cliente.
     */
    public void removerEndereco(Endereco endereco) {
        if (endereco != null) {
            enderecos.remove(endereco);
        }
    }

    /**
     * Retorna o primeiro endereco (endereco de entrega principal).
     */
    public Endereco getEnderecoAtual() {
        return enderecos.isEmpty() ? null : enderecos.get(0);
    }

    /**
     * Adiciona uma assinatura ao historico do cliente.
     */
    public void adicionarAssinatura(Assinatura assinatura) {
        if (assinatura != null && !assinaturas.contains(assinatura)) {
            assinaturas.add(assinatura);
        }
    }

    @Override
    public String toString() {
        return "Cliente [CPF=" + cpf + ", Nome=" + nome + ", Email=" + email +
               ", Telefone=" + telefone + ", Enderecos=" + enderecos.size() + "]";
    }
}
