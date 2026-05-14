package persistencia;

import entidade.CestaSemanal;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Persistencia - Responsavel por salvar e recuperar dados de CestaSemanal em arquivo CSV.
 */
public class CestaSemanalDAO {
    private static final String ARQUIVO = "dados/cestas.csv";
    private int proximoId = 1;

    public CestaSemanalDAO() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            try {
                arquivo.getParentFile().mkdirs();
                arquivo.createNewFile();
                try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                    pw.println("id;dataReferencia;status;planoNome");
                }
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo de cestas: " + e.getMessage());
            }
        }
    }

    public int getProximoId() {
        return proximoId++;
    }

    public void salvar(CestaSemanal cesta) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            String planoNome = cesta.getPlano() != null ? cesta.getPlano().getNome() : "N/A";
            pw.println(cesta.getIdCesta() + ";" + cesta.getDataReferencia() + ";" +
                      cesta.getStatus() + ";" + planoNome);
        }
    }

    public List<CestaSemanal> listarTodas() throws IOException {
        List<CestaSemanal> cestas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(";");
                if (campos.length == 4) {
                    CestaSemanal c = new CestaSemanal();
                    c.setIdCesta(Integer.parseInt(campos[0]));
                    c.setDataReferencia(LocalDate.parse(campos[1]));
                    c.setStatus(campos[2]);
                    cestas.add(c);
                }
            }
        }
        return cestas;
    }
}
