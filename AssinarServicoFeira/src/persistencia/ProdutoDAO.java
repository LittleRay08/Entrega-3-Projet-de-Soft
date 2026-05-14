package persistencia;

import entidade.Produto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Persistencia - Responsavel por salvar e recuperar dados de Produto em arquivo CSV.
 */
public class ProdutoDAO {
    private static final String ARQUIVO = "dados/produtos.csv";

    public ProdutoDAO() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            try {
                arquivo.getParentFile().mkdirs();
                arquivo.createNewFile();
                try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                    pw.println("id;nome;categoria;preco;estoque;unidade");
                    // Dados de exemplo
                    pw.println("1;Maca;FRUTA;5.50;50;KG");
                    pw.println("2;Banana;FRUTA;3.00;80;KG");
                    pw.println("3;Laranja;FRUTA;4.50;60;KG");
                    pw.println("4;Tomate;LEGUME;8.00;40;KG");
                    pw.println("5;Cenoura;LEGUME;6.00;50;KG");
                    pw.println("6;Batata;LEGUME;7.00;70;KG");
                    pw.println("7;Alface;VERDURA;3.50;45;UNIDADE");
                    pw.println("8;Couve;VERDURA;4.00;50;KG");
                    pw.println("9;Espinafre;VERDURA;5.50;30;KG");
                }
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo de produtos: " + e.getMessage());
            }
        }
    }

    public void salvar(Produto produto) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            pw.println(produto.getIdProduto() + ";" + produto.getNome() + ";" +
                      produto.getCategoria() + ";" + produto.getPrecoUnid() + ";" +
                      produto.getEstoqueDisponivel() + ";" + produto.getUnidade());
        }
    }

    public List<Produto> listarProdutosPorCategoria(String categoria) throws IOException {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(";");
                if (campos.length == 6 && campos[2].equalsIgnoreCase(categoria)) {
                    Produto p = new Produto(
                        Integer.parseInt(campos[0]),
                        campos[1],
                        campos[2],
                        Double.parseDouble(campos[3]),
                        Integer.parseInt(campos[4]),
                        campos[5]
                    );
                    produtos.add(p);
                }
            }
        }
        return produtos;
    }

    public List<Produto> listarTodosProdutos() throws IOException {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(";");
                if (campos.length == 6) {
                    Produto p = new Produto(
                        Integer.parseInt(campos[0]),
                        campos[1],
                        campos[2],
                        Double.parseDouble(campos[3]),
                        Integer.parseInt(campos[4]),
                        campos[5]
                    );
                    produtos.add(p);
                }
            }
        }
        return produtos;
    }

    public Produto buscarPorId(int id) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(";");
                if (campos.length == 6 && Integer.parseInt(campos[0]) == id) {
                    return new Produto(
                        Integer.parseInt(campos[0]),
                        campos[1],
                        campos[2],
                        Double.parseDouble(campos[3]),
                        Integer.parseInt(campos[4]),
                        campos[5]
                    );
                }
            }
        }
        return null;
    }
}
