package entidade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe de Entidade (Dominio) - Representa a Assinatura do Servico de Feira.
 * Conforme Diagrama de Classes: 
 * - CRIADOR: Assinatura cria CestaSemanal
 * - Relacionamento 1:* com CestaSemanal
 */
public class Assinatura {
    private int id;
    private String idProtocolo; // Gerado automaticamente para cada assinatura
    private String cpfCliente;
    private PlanoFeira plano;
    private Endereco endereco;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status; // ATIVA, PENDENTE_APROVACAO, CANCELADA, ENCERRADA
    private double valorMensal;
    private List<CestaSemanal> cestas; // CRIADOR: Assinatura cria cestas

    public Assinatura() {
        this.cestas = new ArrayList<>();
    }

    public Assinatura(int id, String cpfCliente, PlanoFeira plano, LocalDate dataInicio, 
                      LocalDate dataFim, String status, double valorMensal) {
        this.id = id;
        this.cpfCliente = cpfCliente;
        this.plano = plano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.valorMensal = valorMensal;
        this.cestas = new ArrayList<>();
        this.idProtocolo = gerarProtocolo();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdProtocolo() { return idProtocolo; }
    public void setIdProtocolo(String idProtocolo) { this.idProtocolo = idProtocolo; }

    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }

    public PlanoFeira getPlano() { return plano; }
    public void setPlano(PlanoFeira plano) { this.plano = plano; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorMensal() { return valorMensal; }
    public void setValorMensal(double valorMensal) { this.valorMensal = valorMensal; }

    public List<CestaSemanal> getCestas() { return cestas; }

    /**
     * Gera um protocolo unico para a assinatura.
     * Conforme diagrama de sequencia: gerarProtocolo() eh responsabilidade de Assinatura.
     */
    public String gerarProtocolo() {
        if (this.idProtocolo != null && !this.idProtocolo.isEmpty()) {
            return this.idProtocolo;
        }
        
        // Formato: ASSIN-AAAAMMDD-NNNNN (Assinatura - Data - Random)
        String data = LocalDate.now().toString().replace("-", "");
        String random = String.format("%05d", new Random().nextInt(100000));
        return "ASSIN-" + data + "-" + random;
    }

    /**
     * Registra o endereco de entrega.
     * Conforme diagrama de sequencia: registrarEndereco() eh acionado pelo controller.
     */
    public void registrarEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    /**
     * Cria uma nova cesta semanal para a assinatura.
     * CRIADOR (padrão): Assinatura cria a CestaSemanal (conforme diagrama de classes).
     */
    public CestaSemanal gerarCesta(int idCesta) {
        CestaSemanal novaCesta = new CestaSemanal(idCesta, LocalDate.now(), this.plano);
        cestas.add(novaCesta);
        return novaCesta;
    }

    /**
     * Finaliza o mes da assinatura.
     * Atualiza a data de fim para proximo mes.
     */
    public void finalizarMes() {
        this.dataFim = this.dataFim.plusMonths(1);
        // Pode ser expandido com outras logicas
    }

    /**
     * Muda o status da assinatura.
     * Conforme diagrama de sequencia: mudarStatus() eh acionado pelo controller.
     */
    public void mudarStatus(String novoStatus) {
        this.status = novoStatus;
    }

    @Override
    public String toString() {
        return "Assinatura [ID=" + id + ", Protocolo=" + idProtocolo + ", CPF=" + cpfCliente + 
               ", Plano=" + (plano != null ? plano.getNome() : "N/A") +
               ", Inicio=" + dataInicio + ", Fim=" + dataFim + 
               ", Status=" + status + ", Valor=R$" + valorMensal + "]";
    }
}
