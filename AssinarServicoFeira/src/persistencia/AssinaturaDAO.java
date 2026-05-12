package persistencia;

import entidade.Assinatura;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Persistencia - Responsavel por salvar e recuperar dados de Assinatura em arquivo CSV.
 */
public class AssinaturaDAO {
    private static final String ARQUIVO = "dados/assinaturas.csv";
    private int proximoId = 1;

    public AssinaturaDAO() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            try {
                arquivo.getParentFile().mkdirs();
                arquivo.createNewFile();
                try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                    pw.println("id;cpfCliente;plano;dataInicio;dataFim;status;valorMensal");
                }
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo de assinaturas: " + e.getMessage());
            }
        } else {
            // Determina proximo ID
            try {
                List<Assinatura> assinaturas = listarTodas();
                for (Assinatura a : assinaturas) {
                    if (a.getId() >= proximoId) {
                        proximoId = a.getId() + 1;
                    }
                }
            } catch (IOException e) {
                // Ignora
            }
        }
    }

    public int getProximoId() {
        return proximoId++;
    }

    public void salvar(Assinatura assinatura) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            pw.println(assinatura.getId() + ";" + assinatura.getCpfCliente() + ";" +
                      assinatura.getPlano() + ";" + assinatura.getDataInicio() + ";" +
                      assinatura.getDataFim() + ";" + assinatura.getStatus() + ";" +
                      assinatura.getValorMensal());
        }
    }

    public List<Assinatura> buscarPorCpf(String cpf) throws IOException {
        List<Assinatura> resultado = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            br.readLine(); // Pula cabecalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 7 && dados[1].equals(cpf)) {
                    Assinatura a = new Assinatura(
                        Integer.parseInt(dados[0]),
                        dados[1], dados[2],
                        LocalDate.parse(dados[3]),
                        LocalDate.parse(dados[4]),
                        dados[5],
                        Double.parseDouble(dados[6])
                    );
                    resultado.add(a);
                }
            }
        }
        return resultado;
    }

    public List<Assinatura> listarTodas() throws IOException {
        List<Assinatura> assinaturas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            br.readLine(); // Pula cabecalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 7) {
                    Assinatura a = new Assinatura(
                        Integer.parseInt(dados[0]),
                        dados[1], dados[2],
                        LocalDate.parse(dados[3]),
                        LocalDate.parse(dados[4]),
                        dados[5],
                        Double.parseDouble(dados[6])
                    );
                    assinaturas.add(a);
                }
            }
        }
        return assinaturas;
    }

    public boolean clienteTemAssinaturaAtiva(String cpf) throws IOException {
        List<Assinatura> assinaturas = buscarPorCpf(cpf);
        for (Assinatura a : assinaturas) {
            if ("ATIVA".equals(a.getStatus())) {
                return true;
            }
        }
        return false;
    }
}
