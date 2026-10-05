import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorFinanceiro gerenciador = new GerenciadorFinanceiro();
        int opcao = 0;

        do {
            System.out.println("\n=================================");
            System.out.println("   GERENCIADOR DE DESPESAS PESSOAIS");
            System.out.println("=================================");
            System.out.println("1. Adicionar Receita");
            System.out.println("2. Adicionar Despesa");
            System.out.println("3. Listar Transações (Extrato)");
            System.out.println("4. Ver Resumo (Totais e Saldo)");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer do teclado

                switch (opcao) {
                    case 1:
                    case 2:
                        Transacao.Tipo tipo = (opcao == 1) ? Transacao.Tipo.RECEITA : Transacao.Tipo.DESPESA;
                        cadastrarTransacao(scanner, gerenciador, tipo);
                        break;
                    case 3:
                        gerenciador.listarTransacoes();
                        break;
                    case 4:
                        exibirResumo(gerenciador);
                        break;
                    case 5:
                        System.out.println("\nSalvando dados e encerrando o programa. Até logo!");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Escolha entre 1 e 5.");
                }
            } else {
                System.out.println("\nErro: Digite apenas números válidos.");
                scanner.nextLine(); // Descarta entrada inválida
            }

        } while (opcao != 5);

        scanner.close();
    }

    private static void cadastrarTransacao(Scanner scanner, GerenciadorFinanceiro gerenciador, Transacao.Tipo tipo) {
        System.out.println("\n--- Nova " + tipo + " ---");
        
        System.out.print("Descrição (ex: Salário, Mercado, Aluguel): ");
        String descricao = scanner.nextLine();

        double valor = 0;
        while (true) {
            System.out.print("Valor (R$): ");
            if (scanner.hasNextDouble()) {
                valor = scanner.nextDouble();
                scanner.nextLine();
                if (valor > 0) break;
                System.out.println("O valor deve ser maior que zero.");
            } else {
                System.out.println("Valor inválido. Use formato numérico (ex: 150.50).");
                scanner.nextLine();
            }
        }

        LocalDate data = null;
        while (data == null) {
            System.out.print("Data (AAAA-MM-DD) ou pressione ENTER para usar a data de hoje: ");
            String dataStr = scanner.nextLine().trim();
            if (dataStr.isEmpty()) {
                data = LocalDate.now();
            } else {
                try {
                    data = LocalDate.parse(dataStr);
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use o formato AAAA-MM-DD (ex: 2026-06-07).");
                }
            }
        }

        gerenciador.adicionarTransacao(descricao, valor, data, tipo);
        System.out.println("Transação cadastrada e salva com sucesso!");
    }

    private static void exibirResumo(GerenciadorFinanceiro gerenciador) {
        double totalReceitas = gerenciador.calcularTotalReceitas();
        double totalDespesas = gerenciador.calcularTotalDespesas();
        double saldo = gerenciador.calcularSaldo();

        System.out.println("\n--- RESUMO FINANCEIRO ---");
        System.out.printf("Total de Receitas: R$ %.2f\n", totalReceitas);
        System.out.printf("Total de Despesas: R$ %.2f\n", totalDespesas);
        System.out.println("-------------------------");
        System.out.printf("Saldo Atual      : R$ %.2f\n", saldo);
        if (saldo < 0) {
            System.out.println("⚠️ Alerta: Seu saldo está negativo!");
        } else {
            System.out.println("✅ Parabéns, suas finanças estão no azul!");
        }
    }
}