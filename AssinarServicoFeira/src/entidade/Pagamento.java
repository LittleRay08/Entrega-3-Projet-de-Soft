package entidade;

import java.time.LocalDateTime;

/**
 * Classe de Entidade - Representa um Pagamento.
 * Diagrama de Classes: Atributos privados (-) e metodos publicos (+)
 */
public class Pagamento {
    private int idPagamento;
    private String numeroCartao;
    private String nomeCartao;
    private String validade; // MM/AA
    private String cvv;
    private double valor;
    private String status; // PENDENTE, AUTORIZADO, RECUSADO
    private LocalDateTime dataPagamento;

    public Pagamento() {}

    public Pagamento(int idPagamento, String numeroCartao, String nomeCartao, 
                    String validade, String cvv, double valor) {
        this.idPagamento = idPagamento;
        this.numeroCartao = numeroCartao;
        this.nomeCartao = nomeCartao;
        this.validade = validade;
        this.cvv = cvv;
        this.valor = valor;
        this.status = "PENDENTE";
        this.dataPagamento = LocalDateTime.now();
    }

    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public String getNumeroCartao() { return numeroCartao; }
    public void setNumeroCartao(String numeroCartao) { this.numeroCartao = numeroCartao; }

    public String getNomeCartao() { return nomeCartao; }
    public void setNomeCartao(String nomeCartao) { this.nomeCartao = nomeCartao; }

    public String getValidade() { return validade; }
    public void setValidade(String validade) { this.validade = validade; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    /**
     * Autoriza a transacao (valida dados do cartao).
     * ESPECIALISTA: Valida suas proprias regras.
     */
    public boolean autorizar(double valor) {
        // Validacoes basicas
        if (valor <= 0) {
            this.status = "RECUSADO";
            return false;
        }

        if (!validarCartao()) {
            this.status = "RECUSADO";
            return false;
        }

        if (valor > this.valor) {
            this.status = "RECUSADO";
            return false;
        }

        // Simula autorizacao bem-sucedida
        this.status = "AUTORIZADO";
        this.dataPagamento = LocalDateTime.now();
        return true;
    }

    /**
     * Valida dados basicos do cartao.
     */
    private boolean validarCartao() {
        // Valida numero do cartao (16 digitos)
        if (numeroCartao == null || numeroCartao.replaceAll("\\D", "").length() != 16) {
            return false;
        }

        // Valida validade
        if (validade == null || !validade.matches("\\d{2}/\\d{2}")) {
            return false;
        }

        // Valida CVV
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            return false;
        }

        // Valida nome
        if (nomeCartao == null || nomeCartao.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Pagamento [ID=" + idPagamento + ", Valor=R$" + String.format("%.2f", valor) +
               ", Cartao=" + numeroCartao.substring(numeroCartao.length() - 4) +
               ", Status=" + status + "]";
    }
}
