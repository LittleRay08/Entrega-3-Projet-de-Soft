package fronteira;

import controle.AssinaturaController;
import entidade.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe Fronteira (Boundary) - Interface com o usuario via console.
 * Corresponde a tela do prototipo e ao ator no diagrama de sequencia.
 * Responsavel por capturar entradas e exibir saidas.
 * 
 * Fluxo conforme Diagrama de Sequencia:
 * 1. Valida Acesso (celular + SMS)
 * 2. Busca e Seleciona Plano
 * 3. Monta Cesta (loop por categorias)
 * 4. Informa Endereco e Cartao
 * 5. Finaliza com Protocolo
 */
public class TelaAssinaturaFeira {
    private AssinaturaController controller;
    private Scanner scanner;

    public TelaAssinaturaFeira() {
        this.controller = new AssinaturaController();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Metodo principal que executa o fluxo da tela.
     */
    public void executar() {
        System.out.println("==============================================");
        System.out.println("   SISTEMA DE ASSINATURA - SERVICO DE FEIRA   ");
        System.out.println("==============================================");

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Assinar Servico de Feira");
            System.out.println("2. Consultar Minhas Assinaturas");
            System.out.println("3. Ver Planos Disponiveis");
            System.out.println("0. Sair");
            System.out.print("\nEscolha uma opcao: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    fluxoAssinarServico();
                    break;
                case "2":
                    fluxoConsultarAssinaturas();
                    break;
                case "3":
                    exibirPlanos();
                    break;
                case "0":
                    continuar = false;
                    System.out.println("\nObrigado por utilizar nosso sistema! Ate logo.");
                    break;
                default:
                    System.out.println("\nOpcao invalida. Tente novamente.");
            }
        }
        scanner.close();
    }

    /**
     * Fluxo principal do caso de uso: Assinar Servico de Feira.
     * Conforme Diagrama de Sequencia com todas as etapas.
     */
    private void fluxoAssinarServico() {
        System.out.println("\n--- ASSINAR SERVICO DE FEIRA ---");

        try {
            // ETAPA 1: Validar Acesso (celular + SMS)
            System.out.println("\n=== ETAPA 1: VALIDACAO DE ACESSO ===");
            System.out.print("Informe seu celular (DDD + numero): ");
            String celular = scanner.nextLine().trim();

            if (!controller.validarAcesso(celular)) {
                System.out.println("Celular invalido!");
                return;
            }

            System.out.print("\nDigite o codigo SMS recebido: ");
            String codigoSMS = scanner.nextLine().trim();

            if (!controller.confirmarCodigo(codigoSMS)) {
                System.out.println("Codigo SMS invalido!");
                return;
            }

            System.out.println("Acesso validado com sucesso!");

            // Busca/Cadastra Cliente
            System.out.print("\nInforme seu CPF: ");
            String cpf = scanner.nextLine().trim();

            Cliente cliente = controller.buscarCliente(cpf);
            if (cliente == null) {
                System.out.println("\nCliente nao encontrado. Vamos realizar seu cadastro.");
                cliente = realizarCadastro(cpf);
                if (cliente == null) return;
            } else {
                System.out.println("\nBem-vindo(a) de volta, " + cliente.getNome() + "!");
            }

            // Verifica se ja tem assinatura ativa
            if (controller.clienteTemAssinaturaAtiva(cpf)) {
                System.out.println("\nVoce ja possui uma assinatura ativa!");
                return;
            }

            // ETAPA 2: Selecionar Plano
            System.out.println("\n=== ETAPA 2: SELECAO DE PLANO ===");
            System.out.println("--- PLANOS DISPONIVEIS ---");
            exibirPlanos();

            System.out.print("\nDigite o codigo do plano desejado (BASICO/PADRAO/PREMIUM): ");
            String codigoPlano = scanner.nextLine().trim().toUpperCase();

            PlanoFeira plano = controller.buscarPlano(codigoPlano);
            if (plano == null) {
                System.out.println("Plano invalido!");
                return;
            }

            System.out.println("\nPlano selecionado: " + plano.getNome());
            System.out.println("Valor: R$" + String.format("%.2f", plano.getValorMensal()) + "/mes");
            System.out.println("Frequencia: " + plano.getFrequenciaSemanal() + "x por semana");
            System.out.println("Limite de itens por cesta: " + plano.getLimiteItens());

            // Inicia Assinatura
            Assinatura assinatura = controller.iniciarAssinatura(cpf, codigoPlano);
            CestaSemanal cesta = controller.getCestaPendente();

            if (assinatura == null || cesta == null) {
                System.out.println("Erro ao iniciar assinatura!");
                return;
            }

            // ETAPA 3: Montar Cesta Semanal (loop por categorias)
            System.out.println("\n=== ETAPA 3: MONTAGEM DA CESTA ===");
            System.out.println("Limite: " + plano.getLimiteItens() + " itens");

            String[] categorias = {"FRUTA", "LEGUME", "VERDURA"};
            boolean montarMais = true;

            for (String categoria : categorias) {
                if (!montarMais) break;

                System.out.println("\n--- " + categoria + "S ---");
                List<Produto> produtos = controller.buscarProdutos(categoria);

                if (produtos.isEmpty()) {
                    System.out.println("Nenhum produto disponivel nesta categoria.");
                    continue;
                }

                // Exibe produtos
                for (Produto p : produtos) {
                    System.out.println("  " + p);
                }

                // Usuario escolhe produtos
                boolean escolherMais = true;
                while (escolherMais && cesta.quantidadeItens() < plano.getLimiteItens()) {
                    System.out.print("\nDeseja adicionar um " + categoria.toLowerCase() + "? (S/N): ");
                    String resposta = scanner.nextLine().trim().toUpperCase();

                    if (resposta.equals("N")) {
                        escolherMais = false;
                    } else if (resposta.equals("S")) {
                        System.out.print("ID do produto: ");
                        try {
                            int idProduto = Integer.parseInt(scanner.nextLine().trim());
                            Produto produtoSelecionado = produtos.stream()
                                .filter(p -> p.getIdProduto() == idProduto)
                                .findFirst()
                                .orElse(null);

                            if (produtoSelecionado == null) {
                                System.out.println("Produto nao encontrado!");
                                continue;
                            }

                            System.out.print("Quantidade: ");
                            int quantidade = Integer.parseInt(scanner.nextLine().trim());

                            if (controller.adicionarProdutoNaCesta(produtoSelecionado, quantidade)) {
                                System.out.println("Produto adicionado com sucesso!");
                                System.out.println("Itens na cesta: " + cesta.quantidadeItens() + "/" + plano.getLimiteItens());
                                System.out.println("Total: R$" + String.format("%.2f", cesta.calcularTotal()));
                            } else {
                                System.out.println("Nao foi possivel adicionar o produto (estoque insuficiente ou limite atingido).");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Digite um numero valido!");
                        }
                    }
                }

                if (cesta.quantidadeItens() >= plano.getLimiteItens()) {
                    System.out.println("\nLimite de itens atingido!");
                    montarMais = false;
                }
            }

            // Exibe resumo da cesta
            System.out.println("\n--- RESUMO DA CESTA ---");
            if (cesta.estaVazia()) {
                System.out.println("Cesta vazia!");
                return;
            }

            for (Produto p : cesta.listarProdutos()) {
                int qtd = cesta.getProdutosEscolhidos().get(p);
                System.out.println("  " + qtd + "x " + p.getNome() + " - R$" + String.format("%.2f", p.getPrecoUnid() * qtd));
            }
            System.out.println("Total: R$" + String.format("%.2f", cesta.calcularTotal()));

            // ETAPA 4: Endereco e Cartao
            System.out.println("\n=== ETAPA 4: ENDERECO E PAGAMENTO ===");

            // Endereco
            System.out.print("Logradouro: ");
            String logradouro = scanner.nextLine().trim();
            System.out.print("Complemento: ");
            String complemento = scanner.nextLine().trim();
            System.out.print("CEP: ");
            String cep = scanner.nextLine().trim();
            System.out.print("Cidade: ");
            String cidade = scanner.nextLine().trim();
            System.out.print("Estado: ");
            String estado = scanner.nextLine().trim();

            Endereco endereco = new Endereco(logradouro, complemento, cep, cidade, estado);
            cliente.vincularEndereco(endereco);

            // Cartao
            System.out.print("\nNumero do Cartao: ");
            String numeroCartao = scanner.nextLine().trim();
            System.out.print("Nome no Cartao: ");
            String nomeCartao = scanner.nextLine().trim();
            System.out.print("Validade (MM/AA): ");
            String validade = scanner.nextLine().trim();
            System.out.print("CVV: ");
            String cvv = scanner.nextLine().trim();

            // ETAPA 5: Finalizar Assinatura
            System.out.println("\n=== ETAPA 5: FINALIZANDO ASSINATURA ===");
            System.out.println("Processando transacao...");

            Assinatura assinaturaSalva = controller.finalizarAssinatura(endereco, numeroCartao, 
                                                                         nomeCartao, validade, cvv);

            System.out.println("\n============================================");
            System.out.println("   ASSINATURA REALIZADA COM SUCESSO!       ");
            System.out.println("============================================");
            System.out.println("Protocolo: " + assinatura.getIdProtocolo());
            System.out.println("Plano: " + plano.getNome());
            System.out.println("Valor: R$" + String.format("%.2f", plano.getValorMensal()));
            System.out.println("Endereco de entrega: " + endereco.toString());
            System.out.println("Obrigado por assinar! Sua cesta chegara em breve.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\nErro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("\nErro ao acessar dados: " + e.getMessage());
        }
    }

    /**
     * Realiza o cadastro de um novo cliente.
     */
    private Cliente realizarCadastro(String cpf) throws IOException {
        System.out.println("\n--- CADASTRO DE CLIENTE ---");

        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("Nome obrigatorio.");
            return null;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Email obrigatorio.");
            return null;
        }

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();

        Cliente cliente = new Cliente(cpf, nome, email, telefone);
        controller.cadastrarCliente(cliente);
        System.out.println("\nCadastro realizado com sucesso!");
        return cliente;
    }

    /**
     * Consulta assinaturas do cliente.
     */
    private void fluxoConsultarAssinaturas() {
        System.out.println("\n--- CONSULTAR ASSINATURAS ---");
        System.out.print("Informe seu CPF: ");
        String cpf = scanner.nextLine().trim();

        try {
            List<Assinatura> assinaturas = controller.listarAssinaturasCliente(cpf);
            if (assinaturas.isEmpty()) {
                System.out.println("Nenhuma assinatura encontrada para este CPF.");
            } else {
                System.out.println("\nSuas assinaturas:");
                for (Assinatura a : assinaturas) {
                    System.out.println("  " + a);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao consultar: " + e.getMessage());
        }
    }

    /**
     * Exibe os planos disponiveis.
     */
    private void exibirPlanos() {
        List<PlanoFeira> planos = controller.listarPlanosDisponiveis();
        for (PlanoFeira plano : planos) {
            System.out.println("  " + plano);
        }
    }
}
