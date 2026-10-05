import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transacao {
    
    public enum Tipo {
        RECEITA, DESPESA
    }

    private String descricao;
    private double valor;
    private LocalDate data;
    private Tipo tipo;

    public Transacao(String descricao, double valor, LocalDate data, Tipo tipo) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public Tipo getTipo() {
        return tipo;
    }

    // Método para converter o objeto em texto simples para salvar no arquivo (Formato CSV)
    public String toCsv() {
        return descricao + ";" + valor + ";" + data.toString() + ";" + tipo;
    }

    // Método estático para recriar o objeto a partir de uma linha do arquivo
    public static Transacao fromCsv(String linha) {
        String[] partes = linha.split(";");
        String desc = partes[0];
        double val = Double.parseDouble(partes[1]);
        LocalDate dt = LocalDate.parse(partes[2]);
        Tipo tp = Tipo.valueOf(partes[3]);
        return new Transacao(desc, val, dt, tp);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = data.format(formatter);
        return String.format("[%s] Data: %s | Descrição: %s | Valor: R$ %.2f", 
                tipo, dataFormatada, descricao, valor);
    }
}