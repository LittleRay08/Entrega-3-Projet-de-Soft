package entidade;

import java.time.LocalDate;

/**
 * Classe de Entidade (Dominio) - Representa a Assinatura do Servico de Feira.
 */
public class Assinatura {
    private int id;
    private String cpfCliente;
    private String plano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;
    private double valorMensal;

    public Assinatura() {}

    public Assinatura(int id, String cpfCliente, String plano, LocalDate dataInicio, 
                      LocalDate dataFim, String status, double valorMensal) {
        this.id = id;
        this.cpfCliente = cpfCliente;
        this.plano = plano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.valorMensal = valorMensal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }

    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorMensal() { return valorMensal; }
    public void setValorMensal(double valorMensal) { this.valorMensal = valorMensal; }

    @Override
    public String toString() {
        return "Assinatura [ID=" + id + ", CPF=" + cpfCliente + ", Plano=" + plano +
               ", Inicio=" + dataInicio + ", Fim=" + dataFim + 
               ", Status=" + status + ", Valor=R$" + valorMensal + "]";
    }
}
