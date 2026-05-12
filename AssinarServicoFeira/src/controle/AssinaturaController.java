package controle;

import entidade.Assinatura;
import entidade.Cliente;
import entidade.PlanoFeira;
import persistencia.AssinaturaDAO;
import persistencia.ClienteDAO;
import persistencia.PlanoFeiraDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe Controller - Orquestra o fluxo do caso de uso "Assinar Servico de Feira".
 * Corresponde ao diagrama de sequencia: recebe requisicoes da Fronteira,
 * interage com as Entidades e Persistencia, e retorna resultados.
 */
public class AssinaturaController {
    private ClienteDAO clienteDAO;
    private AssinaturaDAO assinaturaDAO;
    private PlanoFeiraDAO planoFeiraDAO;

    public AssinaturaController() {
        this.clienteDAO = new ClienteDAO();
        this.assinaturaDAO = new AssinaturaDAO();
        this.planoFeiraDAO = new PlanoFeiraDAO();
    }

    /**
     * Retorna a lista de planos disponiveis para exibicao na tela.
     */
    public List<PlanoFeira> listarPlanosDisponiveis() {
        return planoFeiraDAO.listarPlanos();
    }

    /**
     * Busca um plano pelo codigo.
     */
    public PlanoFeira buscarPlano(String codigo) {
        return planoFeiraDAO.buscarPorCodigo(codigo);
    }

    /**
     * Verifica se o cliente ja esta cadastrado pelo CPF.
     */
    public Cliente buscarCliente(String cpf) throws IOException {
        return clienteDAO.buscarPorCpf(cpf);
    }

    /**
     * Cadastra um novo cliente no sistema.
     */
    public void cadastrarCliente(Cliente cliente) throws IOException {
        clienteDAO.salvar(cliente);
    }

    /**
     * Verifica se o cliente ja possui uma assinatura ativa.
     */
    public boolean clienteTemAssinaturaAtiva(String cpf) throws IOException {
        return assinaturaDAO.clienteTemAssinaturaAtiva(cpf);
    }

    /**
     * Realiza a assinatura do servico de feira.
     * Fluxo principal do diagrama de sequencia:
     * 1. Verifica se cliente existe
     * 2. Verifica se ja tem assinatura ativa
     * 3. Valida o plano escolhido
     * 4. Cria e persiste a assinatura
     */
    public Assinatura realizarAssinatura(String cpf, String codigoPlano) throws IOException {
        // Verifica se cliente existe
        Cliente cliente = clienteDAO.buscarPorCpf(cpf);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao encontrado. Cadastre-se primeiro.");
        }

        // Verifica se ja tem assinatura ativa
        if (assinaturaDAO.clienteTemAssinaturaAtiva(cpf)) {
            throw new IllegalStateException("Cliente ja possui uma assinatura ativa.");
        }

        // Valida plano
        PlanoFeira plano = planoFeiraDAO.buscarPorCodigo(codigoPlano);
        if (plano == null) {
            throw new IllegalArgumentException("Plano invalido: " + codigoPlano);
        }

        // Cria assinatura
        int id = assinaturaDAO.getProximoId();
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = dataInicio.plusMonths(1);

        Assinatura assinatura = new Assinatura(id, cpf, plano.getNome(), 
                                               dataInicio, dataFim, "ATIVA", plano.getValorMensal());

        // Persiste
        assinaturaDAO.salvar(assinatura);

        return assinatura;
    }

    /**
     * Lista assinaturas de um cliente.
     */
    public List<Assinatura> listarAssinaturasCliente(String cpf) throws IOException {
        return assinaturaDAO.buscarPorCpf(cpf);
    }
}
