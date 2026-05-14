package controle;

import entidade.*;
import persistencia.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Controller - Orquestra o fluxo do caso de uso "Assinar Servico de Feira".
 * INVENÇÃO PURA: Orquestra, mas não faz a lógica pesada.
 * As entidades (especialistas) fazem suas próprias validações.
 */
public class AssinaturaController {
    private ClienteDAO clienteDAO;
    private AssinaturaDAO assinaturaDAO;
    private PlanoFeiraDAO planoFeiraDAO;
    private ProdutoDAO produtoDAO;
    private CestaSemanalDAO cestaDAO;

    // Estado temporário da assinatura em progresso
    private Assinatura assinaturaPendente;
    private CestaSemanal cestaPendente;
    private Map<Produto, Integer> produtosSelecionados;

    public AssinaturaController() {
        this.clienteDAO = new ClienteDAO();
        this.assinaturaDAO = new AssinaturaDAO();
        this.planoFeiraDAO = new PlanoFeiraDAO();
        this.produtoDAO = new ProdutoDAO();
        this.cestaDAO = new CestaSemanalDAO();
        this.produtosSelecionados = new HashMap<>();
    }

    /**
     * Etapa 1: Validar Acesso (SMS)
     */
    public boolean validarAcesso(String celular) {
        if (celular == null || celular.length() < 10) {
            return false;
        }
        System.out.println("[SMS] Codigo enviado para " + celular);
        System.out.println("[DEBUG] Codigo de teste: 123456");
        return true;
    }

    /**
     * Etapa 2: Confirmar Código
     */
    public boolean confirmarCodigo(String codigoRecebido) {
        String codigoEsperado = "123456";
        return codigoRecebido != null && codigoRecebido.equals(codigoEsperado);
    }

    /**
     * Etapa 3: Listar Planos
     */
    public List<PlanoFeira> listarPlanosDisponiveis() {
        return planoFeiraDAO.listarPlanos();
    }

    public PlanoFeira buscarPlano(String codigo) {
        return planoFeiraDAO.buscarPorCodigo(codigo);
    }

    /**
     * Inicia o processo de assinatura.
     */
    public Assinatura iniciarAssinatura(String cpf, String codigoPlano) throws IOException {
        PlanoFeira plano = planoFeiraDAO.buscarPorCodigo(codigoPlano);
        if (plano == null) {
            throw new IllegalArgumentException("Plano invalido: " + codigoPlano);
        }

        Cliente cliente = clienteDAO.buscarPorCpf(cpf);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao encontrado. Cadastre-se primeiro.");
        }

        if (assinaturaDAO.clienteTemAssinaturaAtiva(cpf)) {
            throw new IllegalStateException("Cliente ja possui uma assinatura ativa.");
        }

        int id = assinaturaDAO.getProximoId();
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = dataInicio.plusMonths(1);

        // PADRÃO CRIADOR: Assinatura é instanciada
        assinaturaPendente = new Assinatura(id, cpf, plano, dataInicio, dataFim,
                "PENDENTE_APROVACAO", plano.getValorMensal());

        int idCesta = cestaDAO.getProximoId();
        cestaPendente = new CestaSemanal(idCesta, dataInicio, plano);

        return assinaturaPendente;
    }

    /**
     * Etapa 4 e 5: Gestão de Produtos na Cesta
     */
    public List<Produto> buscarProdutos(String categoria) throws IOException {
        return produtoDAO.listarProdutosPorCategoria(categoria);
    }

    public List<Produto> listarTodosProdutos() throws IOException {
        return produtoDAO.listarTodosProdutos();
    }

    public boolean adicionarProdutoNaCesta(Produto produto, int quantidade) {
        if (cestaPendente == null) {
            throw new IllegalStateException("Nenhuma cesta em progresso.");
        }
        return cestaPendente.adicionarProduto(produto, quantidade);
    }

    public void removerProdutoDaCesta(Produto produto) {
        if (cestaPendente != null) {
            cestaPendente.removerProduto(produto);
        }
    }

    public CestaSemanal getCestaPendente() {
        return cestaPendente;
    }

    /**
     * Gestão de Clientes
     */
    public Cliente buscarCliente(String cpf) throws IOException {
        return clienteDAO.buscarPorCpf(cpf);
    }

    public void cadastrarCliente(Cliente cliente) throws IOException {
        clienteDAO.salvar(cliente);
    }

    /**
     * Etapa 6: Finalizar Assinatura
     * CORREÇÃO: Valor de pagamento fixado pelo Plano.
     */
    public Assinatura finalizarAssinatura(Endereco endereco, String numeroCartao,
                                         String nomeCartao, String validade, String cvv) throws IOException {
        if (assinaturaPendente == null) {
            throw new IllegalStateException("Nenhuma assinatura em progresso.");
        }

        // 1. Registrar endereço
        assinaturaPendente.registrarEndereco(endereco);

        // 2. Mudar status temporário
        assinaturaPendente.mudarStatus("AGUARDANDO_APROVACAO");

        // 3. CORREÇÃO DE REGRA DE NEGÓCIO:
        // O valor enviado para a operadora é o valor mensal do PLANO,
        double valorFixoAssinatura = assinaturaPendente.getPlano().getValorMensal();

        Pagamento pagamento = new Pagamento(1, numeroCartao, nomeCartao, validade, cvv, valorFixoAssinatura);
        boolean transacaoAutorizada = autorizarTransacao(valorFixoAssinatura, pagamento);

        if (!transacaoAutorizada) {
            assinaturaPendente.mudarStatus("RECUSADO");
            throw new RuntimeException("Transacao recusada. Verifique os dados do cartao.");
        }

        // 4. Ativar assinatura e gerar protocolo
        assinaturaPendente.mudarStatus("ATIVA");
        String protocolo = assinaturaPendente.gerarProtocolo();

        // 5. Persistência
        assinaturaDAO.salvar(assinaturaPendente);
        if (cestaPendente != null && !cestaPendente.estaVazia()) {
            cestaDAO.salvar(cestaPendente);
        }

        // 6. Limpeza de estado
        Assinatura concluida = assinaturaPendente;
        assinaturaPendente = null;
        cestaPendente = null;
        produtosSelecionados.clear();

        return concluida;
    }

    private boolean autorizarTransacao(double valor, Pagamento pagamento) {
        // Delega para a entidade Pagamento (Especialista)
        return pagamento.autorizar(valor);
    }

    public boolean clienteTemAssinaturaAtiva(String cpf) throws IOException {
        return assinaturaDAO.clienteTemAssinaturaAtiva(cpf);
    }

    public List<Assinatura> listarAssinaturasCliente(String cpf) throws IOException {
        return assinaturaDAO.buscarPorCpf(cpf);
    }
}
