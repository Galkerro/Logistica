public class Veiculo {
    protected int id;
    protected String placa;
    protected String modelo;
    protected String cor;
    protected Boolean noPatio;

    public Veiculo(int id, String placa, String modelo, String cor) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.noPatio = true;
    }

    public void registrarSaida() {
        if (this.noPatio) {
            this.noPatio = false;
            System.out.println("Saida autorizada, Veiculo de placa: [" + this.placa + "] foi retirado do patio");
        } else {
            System.out.println("Erro, o Veiculo de placa: [" + this.placa + "] não se encontra no patio");
        }
    }

    public void registrarEntrada() {
        if (!this.noPatio) {
            this.noPatio = true;
            System.out.println("Veiculo de placa: [" + this.placa + "] entrou no patio");
        } else {
            System.out.println("Erro, Veiculo de placa: [" + this.placa + " ja se encontra no patio");
        }
    }

    public int getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public boolean isNoPatio() {
        return noPatio;
    }

    public void setNoPatio(boolean noPatio) {
        this.noPatio = noPatio;
    }

}
