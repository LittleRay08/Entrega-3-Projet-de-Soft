package controle;

import entidade.*;
import persistencia.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Classe Controller - Orquestra o fluxo do caso de uso "Assinar Servico de Feira".
 * INVENÇÃO PURA (conforme diagrama): Orquestra, mas nao faz a logica pesada.
 * As entidades (especialistas) fazem suas proprias validacoes.
 */
public class AssinaturaController {
    private ClienteDAO clienteDAO;
    private AssinaturaDAO assinaturaDAO;
    private PlanoFeiraDAO planoFeiraDAO;
    private ProdutoDAO produtoDAO;
    private CestaSemanalDAO cestaDAO;
    
    // Estado temporario da assinatura em progresso
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
     * Etapa 1 do Diagrama de Sequencia: Validar Acesso
     * Conforme diagrama: validarAcesso(celular) -> Envia SMS
     */
    public boolean validarAcesso(String celular) {
        // Validacao basica
        if (celular == null || celular.length() < 10) {
            return false;
        }
        
        // Simula envio de SMS
        System.out.println("[SMS] Codigo enviado para " + celular);
        System.out.println("[DEBUG] Codigo de teste: 123456");
        
        return true;
    }

    /**
     * Etapa 2 do Diagrama de Sequencia: Confirmar Codigo SMS
     * Conforme diagrama: confirmarCodigo(codigo)
     */
    public boolean confirmarCodigo(String codigoRecebido) {
        // Simula validacao (em producao, seria verificado com SMS provider)
        String codigoEsperado = "123456";
        return codigoRecebido != null && codigoRecebido.equals(codigoEsperado);
    }

    /**
     * Etapa 3 do Diagrama de Sequencia: Buscar Planos
     * Conforme diagrama: buscarPlanos()
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
     * Inicia o processo de assinatura com um plano selecionado.
     * Conforme diagrama: iniciarAssinatura(plano)
     * Cria uma nova Assinatura (PADRAO CRIADOR).
     */
    public Assinatura iniciarAssinatura(String cpf, String codigoPlano) throws IOException {
        // Valida plano
        PlanoFeira plano = planoFeiraDAO.buscarPorCodigo(codigoPlano);
        if (plano == null) {
            throw new IllegalArgumentException("Plano invalido: " + codigoPlano);
        }

        // Verifica se cliente existe
        Cliente cliente = clienteDAO.buscarPorCpf(cpf);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao encontrado. Cadastre-se primeiro.");
        }

        // Verifica se ja tem assinatura ativa
        if (assinaturaDAO.clienteTemAssinaturaAtiva(cpf)) {
            throw new IllegalStateException("Cliente ja possui uma assinatura ativa.");
        }

        // CRIADOR: Assinatura cria a si mesma conforme diagrama
        int id = assinaturaDAO.getProximoId();
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = dataInicio.plusMonths(1);

        assinaturaPendente = new Assinatura(id, cpf, plano, dataInicio, dataFim, 
                                           "PENDENTE_APROVACAO", plano.getValorMensal());

        // Cria a cesta semanal para o cliente montar
        int idCesta = cestaDAO.getProximoId();
        cestaPendente = new CestaSemanal(idCesta, dataInicio, plano);

        return assinaturaPendente;
    }

    /**
     * Etapa 4 do Diagrama de Sequencia: Buscar Produtos por Categoria
     * Conforme diagrama: buscarProdutos(categoria)
     */
    public List<Produto> buscarProdutos(String categoria) throws IOException {
        return produtoDAO.listarProdutosPorCategoria(categoria);
    }

    /**
     * Lista todos os produtos disponiveis.
     */
    public List<Produto> listarTodosProdutos() throws IOException {
        return produtoDAO.listarTodosProdutos();
    }

    /**
     * Etapa 5 do Diagrama de Sequencia: Adicionar Itens a Cesta
     * Conforme diagrama: adicionarItens(lista)
     * Delega para a CestaSemanal (ESPECIALISTA em validar suas regras).
     */
    public boolean adicionarProdutoNaCesta(Produto produto, int quantidade) {
        if (cestaPendente == null) {
            throw new IllegalStateException("Nenhuma cesta em progresso. Inicie uma assinatura primeiro.");
        }

        // CestaSemanal valida suas proprias regras
        return cestaPendente.adicionarProduto(produto, quantidade);
    }

    /**
     * Remove um produto da cesta.
     */
    public void removerProdutoDaCesta(Produto produto) {
        if (cestaPendente != null) {
            cestaPendente.removerProduto(produto);
        }
    }

    /**
     * Retorna a cesta atual em edicao.
     */
    public CestaSemanal getCestaPendente() {
        return cestaPendente;
    }

    /**
     * Busca um cliente pelo CPF.
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
     * Etapa 6 do Diagrama de Sequencia: Finalizar Assinatura
     * Conforme diagrama: finalizarAssinatura(endereco, cartao)
     * Executa as acoes finais: registrar endereco, mudar status, autorizar transacao, gerar protocolo.
     */
    public Assinatura finalizarAssinatura(Endereco endereco, String numeroCartao, 
                                         String nomeCartao, String validade, String cvv) throws IOException {
        if (assinaturaPendente == null) {
            throw new IllegalStateException("Nenhuma assinatura em progresso.");
        }

        // Acao 1 do diagrama: registrarEndereco(endereco)
        assinaturaPendente.registrarEndereco(endereco);

        // Acao 2 do diagrama: mudarStatus("Aguardando Aprovacao")
        assinaturaPendente.mudarStatus("AGUARDANDO_APROVACAO");

        // Acao 3 do diagrama: autorizarTransacao(valor)
        Pagamento pagamento = new Pagamento(1, numeroCartao, nomeCartao, validade, cvv,
                                           assinaturaPendente.getValorMensal());
        boolean transacaoAutorizada = autorizarTransacao(assinaturaPendente.getValorMensal(), pagamento);

        if (!transacaoAutorizada) {
            assinaturaPendente.mudarStatus("RECUSADO");
            throw new RuntimeException("Transacao recusada. Verifique os dados do cartao.");
        }

        // Acao 4 do diagrama: setStatus("Aprovado")
        assinaturaPendente.mudarStatus("ATIVA");

        // Acao 5 do diagrama: gerarProtocolo()
        String protocolo = assinaturaPendente.gerarProtocolo();

        // Persiste a assinatura
        assinaturaDAO.salvar(assinaturaPendente);

        // Persiste a cesta
        if (cestaPendente != null && !cestaPendente.estaVazia()) {
            cestaDAO.salvar(cestaPendente);
        }

        // Limpa estado temporario
        assinaturaPendente = null;
        cestaPendente = null;
        produtosSelecionados.clear();

        return assinaturaPendente;
    }

    /**
     * Etapa 6.3 do Diagrama de Sequencia: Autorizar Transacao
     * Conforme diagrama: autorizarTransacao(valor) - Ator Secundario: Operadora
     * Delega para Pagamento (ESPECIALISTA em validar transacoes).
     */
    private boolean autorizarTransacao(double valor, Pagamento pagamento) {
        // Pagamento valida suas proprias regras (ESPECIALISTA)
        return pagamento.autorizar(valor);
    }

    /**
     * Verifica se o cliente ja possui uma assinatura ativa.
     */
    public boolean clienteTemAssinaturaAtiva(String cpf) throws IOException {
        return assinaturaDAO.clienteTemAssinaturaAtiva(cpf);
    }

    /**
     * Lista assinaturas de um cliente.
     */
    public List<Assinatura> listarAssinaturasCliente(String cpf) throws IOException {
        return assinaturaDAO.buscarPorCpf(cpf);
    }
}

