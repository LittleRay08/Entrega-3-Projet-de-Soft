package entidade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de Entidade - Representa uma Cesta Semanal de Produtos.
 * Diagrama de Classes: Atributos privados (-) e métodos públicos (+)
 * ESPECIALISTA: Valida suas próprias regras de negócio.
 * CRIADOR: Assinatura cria a cesta.
 */
public class CestaSemanal {
    private int idCesta;
    private LocalDate dataReferencia;
    private String status; // ABERTA, FECHADA, ENTREGUE
    private Map<Produto, Integer> produtosEscolhidos; // Produto -> Quantidade
    private PlanoFeira plano;
    private double totalCesta;

    public CestaSemanal() {
        this.produtosEscolhidos = new HashMap<>();
        this.status = "ABERTA";
        this.totalCesta = 0.0;
    }

    public CestaSemanal(int idCesta, LocalDate dataReferencia, PlanoFeira plano) {
        this.idCesta = idCesta;
        this.dataReferencia = dataReferencia;
        this.plano = plano;
        this.produtosEscolhidos = new HashMap<>();
        this.status = "ABERTA";
        this.totalCesta = 0.0;
    }

    // Getters e Setters
    public int getIdCesta() { return idCesta; }
    public void setIdCesta(int idCesta) { this.idCesta = idCesta; }

    public LocalDate getDataReferencia() { return dataReferencia; }
    public void setDataReferencia(LocalDate dataReferencia) { this.dataReferencia = dataReferencia; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Map<Produto, Integer> getProdutosEscolhidos() { return produtosEscolhidos; }

    public PlanoFeira getPlano() { return plano; }
    public void setPlano(PlanoFeira plano) { this.plano = plano; }

    public double getTotalCesta() { return totalCesta; }

    /**
     * Adiciona um produto à cesta.
     * ESPECIALISTA: Valida suas próprias regras.
     */
    public boolean adicionarProduto(Produto p, int qtd) {
        if (p == null || !p.verificarEstoque(qtd)) {
            return false;
        }

        // Valida regras do plano (limite de itens)
        if (!validarRegrasPlano(p, qtd)) {
            return false;
        }

        produtosEscolhidos.put(p, qtd);
        calcularTotal();
        return true;
    }

    /**
     * Remove um produto da cesta.
     */
    public void removerProduto(Produto p) {
        if (produtosEscolhidos.containsKey(p)) {
            produtosEscolhidos.remove(p);
            calcularTotal();
        }
    }

    /**
     * Valida se a quantidade total de itens na cesta respeita o limite do plano.
     * ESPECIALISTA: Soma as quantidades individuais de cada produto.
     */
    public boolean validarRegrasPlano(Produto p, int qtdDesejada) {
        if (plano == null) {
            return true;
        }

        // Soma a quantidade de todos os itens já presentes na cesta
        int totalItensAtuais = 0;
        for (int qtd : produtosEscolhidos.values()) {
            totalItensAtuais += qtd;
        }

        // Se o produto já está na cesta, a lógica do put() substitui a anterior.
        // Verificamos se o novo total (atuais + nova quantidade) excede o limite.
        if (totalItensAtuais + qtdDesejada > plano.getLimiteItens()) {
            System.out.println("[ALERTA] Limite do plano excedido! Máximo: " + plano.getLimiteItens());
            return false;
        }

        return true;
    }

    /**
     * Calcula o total financeiro da cesta (informativo).
     */
    public double calcularTotal() {
        totalCesta = 0.0;
        for (Map.Entry<Produto, Integer> entry : produtosEscolhidos.entrySet()) {
            Produto p = entry.getKey();
            int qtd = entry.getValue();
            totalCesta += p.getPrecoUnid() * qtd;
        }
        return totalCesta;
    }

    /**
     * Retorna a quantidade total de unidades na cesta (Soma das quantidades).
     */
    public int quantidadeItens() {
        int total = 0;
        for (int qtd : produtosEscolhidos.values()) {
            total += qtd;
        }
        return total;
    }

    /**
     * Retorna lista dos produtos na cesta.
     */
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtosEscolhidos.keySet());
    }

    /**
     * Verifica se a cesta está vazia.
     */
    public boolean estaVazia() {
        return produtosEscolhidos.isEmpty();
    }

    @Override
    public String toString() {
        return "CestaSemanal [ID=" + idCesta + ", Data=" + dataReferencia +
               ", Status=" + status + ", Total=R$" + String.format("%.2f", totalCesta) + 
               ", Itens=" + quantidadeItens() + "]";
    }
}
