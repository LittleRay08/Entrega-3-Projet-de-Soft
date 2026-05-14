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
                    pw.println("id;cpfCliente;plano;dataInicio;dataFim;status;valorMensal;protocolo");
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
            String nomePlano = assinatura.getPlano() != null ? assinatura.getPlano().getNome() : "N/A";
            String idProtocolo = assinatura.getIdProtocolo() != null ? assinatura.getIdProtocolo() : "";
            pw.println(assinatura.getId() + ";" + assinatura.getCpfCliente() + ";" +
                      nomePlano + ";" + assinatura.getDataInicio() + ";" +
                      assinatura.getDataFim() + ";" + assinatura.getStatus() + ";" +
                      assinatura.getValorMensal() + ";" + idProtocolo);
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
                    Assinatura a = new Assinatura();
                    a.setId(Integer.parseInt(dados[0]));
                    a.setCpfCliente(dados[1]);
                    a.setDataInicio(LocalDate.parse(dados[3]));
                    a.setDataFim(LocalDate.parse(dados[4]));
                    a.setStatus(dados[5]);
                    a.setValorMensal(Double.parseDouble(dados[6]));
                    if (dados.length >= 8) {
                        a.setIdProtocolo(dados[7]);
                    }
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
                    Assinatura a = new Assinatura();
                    a.setId(Integer.parseInt(dados[0]));
                    a.setCpfCliente(dados[1]);
                    a.setDataInicio(LocalDate.parse(dados[3]));
                    a.setDataFim(LocalDate.parse(dados[4]));
                    a.setStatus(dados[5]);
                    a.setValorMensal(Double.parseDouble(dados[6]));
                    if (dados.length >= 8) {
                        a.setIdProtocolo(dados[7]);
                    }
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
