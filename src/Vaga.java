public class Vaga {
    private int numero;
    private int tamanho;
    private boolean disponibilidade;

    public Vaga(int numero, int tamanho, boolean disponibilidade) {
        this.numero = numero;
        this.tamanho = tamanho;
        this.disponibilidade = disponibilidade;
    }

    public Vaga() {

    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
