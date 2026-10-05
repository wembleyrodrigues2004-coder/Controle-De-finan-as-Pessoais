import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorFinanceiro {
    private List<Transacao> transacoes;
    private final String NOME_ARQUIVO = "dados_financeiros.txt";

    public GerenciadorFinanceiro() {
        this.transacoes = new ArrayList<>();
        carregarDoArquivo();
    }

    public void adicionarTransacao(String descricao, double valor, LocalDate data, Transacao.Tipo tipo) {
        Transacao t = new Transacao(descricao, valor, data, tipo);
        transacoes.add(t);
        salvarNoArquivo();
    }

    public void listarTransacoes() {
        if (transacoes.isEmpty()) {
            System.out.println("\nNenhuma transação cadastrada.");
            return;
        }
        System.out.println("\n--- EXTRATO FINANCEIRO ---");
        for (int i = 0; i < transacoes.size(); i++) {
            System.out.println((i + 1) + ". " + transacoes.get(i));
        }
    }

    public double calcularTotalReceitas() {
        return transacoes.stream()
                .filter(t -> t.getTipo() == Transacao.Tipo.RECEITA)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public double calcularTotalDespesas() {
        return transacoes.stream()
                .filter(t -> t.getTipo() == Transacao.Tipo.DESPESA)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public double calcularSaldo() {
        return calcularTotalReceitas() - calcularTotalDespesas();
    }

    // Salva a lista inteira no arquivo de texto
    public void salvarNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Transacao t : transacoes) {
                writer.write(t.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    // Carrega os dados do arquivo ao iniciar o programa
    public void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return; // Se o arquivo não existe, começa com a lista vazia
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Transacao t = Transacao.fromCsv(linha);
                    transacoes.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar os dados: " + e.getMessage());
        }
    }
}