package fronteira;

import controle.AssinaturaController;
import entidade.Assinatura;
import entidade.Cliente;
import entidade.PlanoFeira;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe Fronteira (Boundary) - Interface com o usuario via console.
 * Corresponde a tela do prototipo e ao ator no diagrama de sequencia.
 * Responsavel por capturar entradas e exibir saidas.
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
     * Segue o diagrama de sequencia:
     * 1. Cliente informa CPF
     * 2. Sistema verifica cadastro
     * 3. Se nao cadastrado, realiza cadastro
     * 4. Exibe planos disponiveis
     * 5. Cliente escolhe plano
     * 6. Sistema confirma assinatura
     */
    private void fluxoAssinarServico() {
        System.out.println("\n--- ASSINAR SERVICO DE FEIRA ---");

        try {
            // Passo 1: Solicita CPF
            System.out.print("Informe seu CPF: ");
            String cpf = scanner.nextLine().trim();

            if (cpf.isEmpty()) {
                System.out.println("CPF nao pode ser vazio.");
                return;
            }

            // Passo 2: Verifica se cliente esta cadastrado
            Cliente cliente = controller.buscarCliente(cpf);

            // Passo 3: Se nao cadastrado, realiza cadastro
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
                System.out.println("Consulte suas assinaturas no menu principal.");
                return;
            }

            // Passo 4: Exibe planos disponiveis
            System.out.println("\n--- PLANOS DISPONIVEIS ---");
            exibirPlanos();

            // Passo 5: Cliente escolhe plano
            System.out.print("\nDigite o codigo do plano desejado (BASICO/PADRAO/PREMIUM): ");
            String codigoPlano = scanner.nextLine().trim().toUpperCase();

            // Confirmacao
            PlanoFeira plano = controller.buscarPlano(codigoPlano);
            if (plano == null) {
                System.out.println("Plano invalido!");
                return;
            }

            System.out.println("\n--- CONFIRMACAO ---");
            System.out.println("Cliente: " + cliente.getNome());
            System.out.println("Plano: " + plano.getNome());
            System.out.println("Valor: R$" + plano.getValorMensal() + "/mes");
            System.out.println("Frequencia: " + plano.getFrequenciaSemanal() + "x por semana");
            System.out.print("\nConfirmar assinatura? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toUpperCase();

            if (!confirmacao.equals("S")) {
                System.out.println("Assinatura cancelada pelo usuario.");
                return;
            }

            // Passo 6: Realiza assinatura
            Assinatura assinatura = controller.realizarAssinatura(cpf, codigoPlano);

            System.out.println("\n============================================");
            System.out.println("   ASSINATURA REALIZADA COM SUCESSO!       ");
            System.out.println("============================================");
            System.out.println(assinatura);

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

        System.out.print("Endereco de entrega: ");
        String endereco = scanner.nextLine().trim();
        if (endereco.isEmpty()) {
            System.out.println("Endereco obrigatorio para entrega.");
            return null;
        }

        Cliente cliente = new Cliente(cpf, nome, email, telefone, endereco);
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
