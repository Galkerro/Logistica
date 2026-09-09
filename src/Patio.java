import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Patio {
    private ArrayList<Veiculo> listaVeiculos;

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USUARIO = "postgres";
    private final String SENHA = "admin";

    public Patio() {
        this.listaVeiculos = new ArrayList<>();
    }

    private Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public void cadastrarVeiculo(Veiculo veiculo, String dataHora) {
        this.listaVeiculos.add(veiculo);
        String sql = "INSERT INTO veiculos (tipo, modelo, placa, cor, no_patio, ultima_movimentacao) VALUES (?, ?, ?, ?, ?, TO_TIMESTAMP(?, 'DD/MM/YYYY HH24:MI:SS'))";
        try (Connection conexao = obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            String tipo = (veiculo instanceof Carro) ? "Carro" : "Moto";
            comando.setString(1, tipo);
            comando.setString(2, veiculo.getModelo());
            comando.setString(3, veiculo.getPlaca());
            comando.setString(4, veiculo.getCor());
            comando.setBoolean(5, veiculo.isNoPatio());
            comando.setString(6, dataHora);
            comando.executeUpdate();
            System.out.println("[SQL] Sucesso: Gravado no banco de dados");
        } catch (SQLException e) {
            System.out.println("[SQL] Erro ao gravar no banco: " + e.getMessage());
        }
    }

    public void atualizarStatusNoBanco(int id, boolean noPatio, String dataHora) {
        String sql = "UPDATE veiculos SET no_patio = ?, ultima_movimentacao = TO_TIMESTAMP(?, 'DD/MM/YYYY HH24:MI:SS') WHERE id = ?";
        try (Connection conexao = obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setBoolean(1, noPatio);
            comando.setString(2, dataHora);
            comando.setInt(3, id);
            comando.executeUpdate();
            System.out.println("[SQL] Status atualizados no banco.");
        } catch (SQLException e) {
            System.out.println("[SQL] Erro ao atualizar status e hora: " + e.getMessage());
        }
    }

    public ArrayList<Veiculo> carregarVeiculosDoBanco() {
        this.listaVeiculos.clear();
        String sql = "SELECT id, tipo, modelo, placa, cor, no_patio FROM veiculos ORDER BY id ASC";

        try (Connection conexao = obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String tipo = resultado.getString("tipo");
                String modelo = resultado.getString("modelo");
                String placa = resultado.getString("placa");
                String cor = resultado.getString("cor");
                boolean noPatio = resultado.getBoolean("no_patio");

                Veiculo v;
                if (tipo.equalsIgnoreCase("Carro")) {
                    v = new Carro(id, placa, modelo, cor);
                } else {
                    v = new Moto(id, placa, modelo, cor);
                }
                v.setNoPatio(noPatio);
                this.listaVeiculos.add(v);
            }
        } catch (SQLException e) {
            System.out.println("[SQL] Erro ao carregar dados do banco: " + e.getMessage());
        }
        return this.listaVeiculos;
    }

    public ArrayList<Veiculo> getListaVeiculos() {
        return listaVeiculos;
    }
}
