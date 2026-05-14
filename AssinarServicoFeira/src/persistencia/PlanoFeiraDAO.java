package persistencia;

import entidade.PlanoFeira;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Persistencia - Fornece os planos de feira disponiveis.
 * Em um sistema real, viria de um banco de dados. Aqui usamos dados em memoria.
 */
public class PlanoFeiraDAO {

    private List<PlanoFeira> planos;

    public PlanoFeiraDAO() {
        planos = new ArrayList<>();
        planos.add(new PlanoFeira("BASICO", "Plano Basico", 
            "Cesta com frutas e verduras essenciais", 89.90, 1));
        planos.add(new PlanoFeira("PADRAO", "Plano Padrao", 
            "Cesta completa com frutas, verduras e legumes", 149.90, 2));
        planos.add(new PlanoFeira("PREMIUM", "Plano Premium", 
            "Cesta premium com organicos, frutas, verduras e legumes", 249.90, 3));
    }

    public List<PlanoFeira> listarPlanos() {
        return planos;
    }

    public PlanoFeira buscarPorCodigo(String codigo) {
        for (PlanoFeira plano : planos) {
            if (plano.getCodigo().equalsIgnoreCase(codigo)) {
                return plano;
            }
        }
        return null;
    }
}
