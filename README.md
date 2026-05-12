# Assinar Servico de Feira - Implementacao Java

 
## Integrante
- Rayane Yumi | RA: 10410892


## Estrutura do Projeto (Arquitetura MVC - BCE)

`
AssinarServicoFeira/
|-- src/
|   |-- Main.java                          # Ponto de entrada
|   |-- fronteira/                         
|   |   |-- TelaAssinaturaFeira.java
|   |-- controle/                          # Controller (Logica de negocio)
|   |   |-- AssinaturaController.java
|   |-- entidade/                          # Entity (Dominio)
|   |   |-- Cliente.java
|   |   |-- Assinatura.java
|   |   |-- PlanoFeira.java
|   |-- persistencia/                      # DAO (Acesso a dados - CSV)
|       |-- ClienteDAO.java
|       |-- AssinaturaDAO.java
|       |-- PlanoFeiraDAO.java
|-- dados/                                 # Arquivos CSV de persistencia
|   |-- clientes.csv
|   |-- assinaturas.csv
`

## Correspondencia com os Diagramas UML

### Diagrama de Sequencia -> Codigo
| Mensagem no Diagrama         | Classe/Metodo no Codigo                          |
|------------------------------|--------------------------------------------------|
| Cliente informa CPF          | TelaAssinaturaFeira.fluxoAssinarServico()         |
| Verifica cadastro            | AssinaturaController.buscarCliente(cpf)           |
| Cadastra cliente             | AssinaturaController.cadastrarCliente(cliente)    |
| Exibe planos                 | AssinaturaController.listarPlanosDisponiveis()    |
| Escolhe plano                | TelaAssinaturaFeira (captura entrada)             |
| Confirma assinatura          | AssinaturaController.realizarAssinatura(cpf,plano)|
| Persiste dados               | AssinaturaDAO.salvar(assinatura)                  |

### Diagrama de Classes -> Codigo
| Classe no Diagrama | Arquivo Java                    | Camada      |
|--------------------|---------------------------------|-------------|
| Cliente            | entidade/Cliente.java           | Entidade    |
| Assinatura         | entidade/Assinatura.java        | Entidade    |
| PlanoFeira         | entidade/PlanoFeira.java        | Entidade    |
| TelaAssinatura     | fronteira/TelaAssinaturaFeira.java | Fronteira |
| AssinaturaCtrl     | controle/AssinaturaController.java | Controle  |

## Como Compilar e Executar

### Compilar
`ash
cd AssinarServicoFeira
javac -d bin src/entidade/*.java src/persistencia/*.java src/controle/*.java src/fronteira/*.java src/Main.java
`

### Executar
`ash
cd AssinarServicoFeira
java -cp bin Main
`

## Fluxo Principal do Caso de Uso

1. O sistema exibe o menu principal
2. O cliente seleciona "Assinar Servico de Feira"
3. O sistema solicita o CPF do cliente
4. O sistema verifica se o cliente esta cadastrado
   - Se NAO: solicita dados e realiza cadastro
   - Se SIM: exibe mensagem de boas-vindas
5. O sistema verifica se o cliente ja possui assinatura ativa
6. O sistema exibe os planos disponiveis
7. O cliente escolhe um plano
8. O sistema solicita confirmacao
9. O sistema registra a assinatura e exibe confirmacao

## Persistencia de Dados

Os dados sao persistidos em arquivos CSV na pasta dados/:
- clientes.csv: Armazena dados dos clientes cadastrados
- ssinaturas.csv: Armazena as assinaturas realizadas
