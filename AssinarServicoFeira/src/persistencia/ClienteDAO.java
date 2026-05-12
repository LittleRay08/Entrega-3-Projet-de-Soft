package persistencia;

import entidade.Cliente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Persistencia - Responsavel por salvar e recuperar dados de Cliente em arquivo CSV.
 */
public class ClienteDAO {
    private static final String ARQUIVO = "dados/clientes.csv";

    public ClienteDAO() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            try {
                arquivo.getParentFile().mkdirs();
                arquivo.createNewFile();
                // Escreve cabecalho
                try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                    pw.println("cpf;nome;email;telefone;endereco");
                }
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo de clientes: " + e.getMessage());
            }
        }
    }

    public void salvar(Cliente cliente) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            pw.println(cliente.getCpf() + ";" + cliente.getNome() + ";" + 
                      cliente.getEmail() + ";" + cliente.getTelefone() + ";" + 
                      cliente.getEndereco());
        }
    }

    public Cliente buscarPorCpf(String cpf) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            br.readLine(); // Pula cabecalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 5 && dados[0].equals(cpf)) {
                    return new Cliente(dados[0], dados[1], dados[2], dados[3], dados[4]);
                }
            }
        }
        return null;
    }

    public List<Cliente> listarTodos() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            br.readLine(); // Pula cabecalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 5) {
                    clientes.add(new Cliente(dados[0], dados[1], dados[2], dados[3], dados[4]));
                }
            }
        }
        return clientes;
    }

    public boolean cpfExiste(String cpf) throws IOException {
        return buscarPorCpf(cpf) != null;
    }
}
