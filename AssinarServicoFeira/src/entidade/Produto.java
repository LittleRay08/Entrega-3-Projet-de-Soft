package entidade;

/**
 * Classe de Entidade - Representa um Produto de Feira (Fruta, Legume, Verdura).
 * Diagrama de Classes: Atributos privados (-) e metodos publicos (+)
 */
public class Produto {
    private int idProduto;
    private String nome;
    private String categoria; // FRUTA, LEGUME, VERDURA
    private double precoUnid;
    private int estoqueDisponivel;
    private String unidade; // KG, UNIDADE, ETC

    public Produto() {}

    public Produto(int idProduto, String nome, String categoria, double precoUnid, 
                   int estoqueDisponivel, String unidade) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.categoria = categoria;
        this.precoUnid = precoUnid;
        this.estoqueDisponivel = estoqueDisponivel;
        this.unidade = unidade;
    }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecoUnid() { return precoUnid; }
    public void setPrecoUnid(double precoUnid) { this.precoUnid = precoUnid; }

    public int getEstoqueDisponivel() { return estoqueDisponivel; }
    public void setEstoqueDisponivel(int estoqueDisponivel) { this.estoqueDisponivel = estoqueDisponivel; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    /**
     * Verifica se ha estoque suficiente.
     * ESPECIALISTA: Valida suas proprias regras.
     */
    public boolean verificarEstoque(int qtdDesejada) {
        return qtdDesejada > 0 && qtdDesejada <= estoqueDisponivel;
    }

    /**
     * Atualiza o preco do produto.
     */
    public void atualizarPreco(double novoPreco) {
        if (novoPreco > 0) {
            this.precoUnid = novoPreco;
        }
    }

    /**
     * Retorna dados detalhados do produto.
     */
    public String getDadosDetalhados() {
        return "Produto [ID=" + idProduto + ", Nome=" + nome + 
               ", Categoria=" + categoria + ", Preco=R$" + String.format("%.2f", precoUnid) +
               "/" + unidade + ", Estoque=" + estoqueDisponivel + "]";
    }

    @Override
    public String toString() {
        return String.format("%d - %s | R$ %.2f/%s | Estoque: %d", 
                           idProduto, nome, precoUnid, unidade, estoqueDisponivel);
    }
}
