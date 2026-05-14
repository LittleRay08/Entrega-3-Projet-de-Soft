package entidade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de Entidade - Representa uma Cesta Semanal de Produtos.
 * Diagrama de Classes: Atributos privados (-) e metodos publicos (+)
 * ESPECIALISTA: Valida suas proprias regras de negocio.
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
     * Adiciona um produto a cesta.
     * ESPECIALISTA: Valida suas proprias regras.
     */
    public boolean adicionarProduto(Produto p, int qtd) {
        if (p == null || !p.verificarEstoque(qtd)) {
            return false;
        }

        // Valida regras do plano
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
     * Valida se o produto está de acordo com o plano.
     * ESPECIALISTA: Valida suas proprias regras de negocio.
     */
    public boolean validarRegrasPlano(Produto p, int qtd) {
        if (plano == null) {
            return true;
        }

        // Verifica limite de itens do plano
        if (produtosEscolhidos.size() >= plano.getLimiteItens() && 
            !produtosEscolhidos.containsKey(p)) {
            return false;
        }

        // Pode ser expandido com outras regras de negocio
        return true;
    }

    /**
     * Calcula o total da cesta.
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
     * Retorna lista dos produtos na cesta.
     */
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtosEscolhidos.keySet());
    }

    /**
     * Verifica se a cesta esta vazia.
     */
    public boolean estaVazia() {
        return produtosEscolhidos.isEmpty();
    }

    /**
     * Retorna quantidade de itens unicos na cesta.
     */
    public int quantidadeItens() {
        return produtosEscolhidos.size();
    }

    @Override
    public String toString() {
        return "CestaSemanal [ID=" + idCesta + ", Data=" + dataReferencia +
               ", Status=" + status + ", Total=R$" + String.format("%.2f", totalCesta) + "]";
    }
}
