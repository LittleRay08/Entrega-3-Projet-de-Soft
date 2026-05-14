package entidade;

/**
 * Classe de Entidade - Representa um Endereco de Entrega.
 * Diagrama de Classes: Atributos privados (-) e metodos publicos (+)
 */
public class Endereco {
    private String logradouro;
    private String complemento;
    private String cep;
    private String cidade;
    private String estado;

    public Endereco() {}

    public Endereco(String logradouro, String complemento, String cep, String cidade, String estado) {
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public void vincularEndereco(Endereco endereco) {
        this.logradouro = endereco.getLogradouro();
        this.complemento = endereco.getComplemento();
        this.cep = endereco.getCep();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
    }

    @Override
    public String toString() {
        return logradouro + " " + complemento + " | " + cep + " - " + cidade + "/" + estado;
    }
}
