package entidade;

/**
 * Classe de Entidade (Dominio) - Representa um Plano de Feira disponivel.
 */
public class PlanoFeira {
    private String codigo;
    private String nome;
    private String descricao;
    private double valorMensal;
    private int frequenciaSemanal;

    public PlanoFeira() {}

    public PlanoFeira(String codigo, String nome, String descricao, double valorMensal, int frequenciaSemanal) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.valorMensal = valorMensal;
        this.frequenciaSemanal = frequenciaSemanal;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorMensal() { return valorMensal; }
    public void setValorMensal(double valorMensal) { this.valorMensal = valorMensal; }

    public int getFrequenciaSemanal() { return frequenciaSemanal; }
    public void setFrequenciaSemanal(int frequenciaSemanal) { this.frequenciaSemanal = frequenciaSemanal; }

    @Override
    public String toString() {
        return codigo + " - " + nome + " | " + descricao + 
               " | R$" + valorMensal + "/mes | " + frequenciaSemanal + "x por semana";
    }
}
