import fronteira.TelaAssinaturaFeira;

/**
 * Classe Principal - Ponto de entrada do sistema.
 * Instancia a Fronteira (Boundary) e inicia o fluxo do caso de uso.
 */
public class Main {
    public static void main(String[] args) {
        TelaAssinaturaFeira tela = new TelaAssinaturaFeira();
        tela.executar();
    }
}
