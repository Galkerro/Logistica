import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main extends JFrame {
    private Patio meuPatio;

    private JPanel painelFundo;
    private JPanel blocoCentral;
    private JTextField txtPlaca;
    private DefaultTableModel modeloTabela;
    private JTable tabelaConfirmacao;

    public Main() {
        meuPatio = new Patio();

        setTitle("Terminal Portuário - Sistema de Pátio");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        java.io.File arquivoImagem = new java.io.File("src/containers.jpg");
        if (!arquivoImagem.exists()) {
            arquivoImagem = new java.io.File("containers.jpg");
        }
        ImageIcon iconeFundo = new ImageIcon(arquivoImagem.getAbsolutePath());
        painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (iconeFundo.getImage() != null) {
                    g.drawImage(iconeFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        painelFundo.setLayout(new GridBagLayout());
        setContentPane(painelFundo);

        blocoCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        blocoCentral.setOpaque(false);
        blocoCentral.setLayout(new CardLayout());
        blocoCentral.setPreferredSize(new Dimension(650, 420));

        JPanel telaPortaria = new JPanel();
        telaPortaria.setOpaque(false);
        telaPortaria.setLayout(new BoxLayout(telaPortaria, BoxLayout.Y_AXIS));
        telaPortaria.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblTitulo = new JLabel("PORTARIA DO PORTO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 41, 59));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInstrucao = new JLabel("Identifique a placa do veículo:");
        lblInstrucao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstrucao.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInstrucao.setBorder(BorderFactory.createEmptyBorder(25, 0, 5, 0));

        txtPlaca = new JTextField();
        txtPlaca.setFont(new Font("Segoe UI", Font.BOLD, 24));
        txtPlaca.setHorizontalAlignment(JTextField.CENTER);
        txtPlaca.setMaximumSize(new Dimension(350, 50));
        txtPlaca.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 20, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.setMaximumSize(new Dimension(350, 55));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton btnSaida = new JButton("Registrar Saída");
        btnSaida.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSaida.setBackground(new Color(239, 68, 68));
        btnSaida.setForeground(Color.WHITE);

        JButton btnEntrada = new JButton("Registrar Entrada");
        btnEntrada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrada.setBackground(new Color(34, 197, 94));
        btnEntrada.setForeground(Color.WHITE);

        painelBotoes.add(btnSaida);
        painelBotoes.add(btnEntrada);

        telaPortaria.add(lblTitulo);
        telaPortaria.add(lblInstrucao);
        telaPortaria.add(txtPlaca);
        telaPortaria.add(painelBotoes);

        JPanel telaTabela = new JPanel(new BorderLayout(15, 15));
        telaTabela.setOpaque(false);
        telaTabela.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTituloTabela = new JLabel("REGISTRO DE MOVIMENTAÇÃO ATUAL");
        lblTituloTabela.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloTabela.setForeground(new Color(30, 41, 59));
        telaTabela.add(lblTituloTabela, BorderLayout.NORTH);

        String[] colunas = {"ID", "Tipo", "Modelo", "Placa", "Cor", "Novo Status Logístico"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaConfirmacao = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaConfirmacao);
        telaTabela.add(scrollTabela, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar para a Portaria");
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        telaTabela.add(btnVoltar, BorderLayout.SOUTH);

        blocoCentral.add(telaPortaria, "PORTARIA");
        blocoCentral.add(telaTabela, "TABELA");

        painelFundo.add(blocoCentral);

        CardLayout organizadorTelas = (CardLayout) blocoCentral.getLayout();

        btnSaida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placaDigitada = txtPlaca.getText().trim().toUpperCase();
                if (validarEProcessarMovimento(placaDigitada, false)) {
                    organizadorTelas.show(blocoCentral, "TABELA");
                }
            }
        });

        btnEntrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placaDigitada = txtPlaca.getText().trim().toUpperCase();
                if (validarEProcessarMovimento(placaDigitada, true)) {
                    organizadorTelas.show(blocoCentral, "TABELA");
                }
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtPlaca.setText("");
                organizadorTelas.show(blocoCentral, "PORTARIA");
            }
        });
    }

    private boolean validarEProcessarMovimento(String placa, boolean desejaEntrada) {
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, digite a placa do veículo!");
            return false;
        }

        String dataHoraAtual = obterDataHoraBrasilia();

        ArrayList<Veiculo> frotaTotal = meuPatio.carregarVeiculosDoBanco();
        Veiculo veiculoEncontrado = null;

        for (Veiculo v : frotaTotal) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                veiculoEncontrado = v;
                break;
            }
        }

        if (veiculoEncontrado == null) {
            int resposta = JOptionPane.showConfirmDialog(null,
                    "Placa [" + placa + "] não encontrada. Deseja cadastrar este novo veículo vindo do navio?",
                    "Novo Veículo Detectado", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                String[] opcoesTipo = {"Carro", "Moto"};
                String tipo = (String) JOptionPane.showInputDialog
                (null, "Escolha o tipo:", "Cadastro", JOptionPane.QUESTION_MESSAGE, null, opcoesTipo, opcoesTipo);
                if (tipo == null) return false;

                String modelo = JOptionPane.showInputDialog("Digite o Modelo:");
                if (modelo == null || modelo.trim().isEmpty()) return false;

                String cor = JOptionPane.showInputDialog("Digite a Cor:");
                if (cor == null || cor.trim().isEmpty()) return false;

                if (tipo.equals("Carro")) {
                    veiculoEncontrado = new Carro(0, placa, modelo, cor);
                } else {
                    veiculoEncontrado = new Moto(0, placa, modelo, cor);
                }

                meuPatio.cadastrarVeiculo(veiculoEncontrado, dataHoraAtual);

                frotaTotal = meuPatio.carregarVeiculosDoBanco();
                for (Veiculo v : frotaTotal) {
                    if (v.getPlaca().equalsIgnoreCase(placa)) {
                        veiculoEncontrado = v;
                        break;
                    }
                }

                atualizarTabelaVisualUnitaria(veiculoEncontrado, "Cadastrado em: " + dataHoraAtual);
                return true;
            } else {
                return false;
            }
        }

        if (veiculoEncontrado != null) {
            if (desejaEntrada) {
                if (!veiculoEncontrado.isNoPatio()) {
                    veiculoEncontrado.registrarEntrada();

                    meuPatio.atualizarStatusNoBanco(veiculoEncontrado.getId(), true, dataHoraAtual);
                    atualizarTabelaVisualUnitaria(veiculoEncontrado, " Entrada em: " + dataHoraAtual);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Bloqueado: Este veículo já está no pátio!");
                    return false;
                }
            } else {
                if (veiculoEncontrado.isNoPatio()) {
                    veiculoEncontrado.registrarSaida();

                    meuPatio.atualizarStatusNoBanco(veiculoEncontrado.getId(), false, dataHoraAtual);
                    atualizarTabelaVisualUnitaria(veiculoEncontrado, " Saída em: " + dataHoraAtual);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Bloqueado: Este veículo já Saiu do Patio!");
                    return false;
                }
            }
        }
        return false;
    }

    private void atualizarTabelaVisualUnitaria(Veiculo v, String textoStatus) {
        modeloTabela.setRowCount(0);

        String tipo = (v instanceof Carro ? "Carro" : "Moto");modeloTabela.addRow(new Object[] {
                v.getId(), tipo, v.getModelo(), v.getPlaca(), v.getCor(), textoStatus });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {new Main().setVisible(true);});


    }

    private String obterDataHoraBrasilia() {
        try {

            java.net.URL url = new java.net.URL("http://worldtimeapi.org");
            java.net.HttpURLConnection conexao = (java.net.HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(2000);
            conexao.setReadTimeout(2000);

            if (conexao.getResponseCode() == 200) {
                java.io.BufferedReader leitor = new java.io.BufferedReader(new java.io.InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }
                leitor.close();

                String json = resposta.toString();
                String datetime = json.split("\"datetime\":\"")[1].split("\"")[0];

                String data = datetime.substring(8, 10) + "/" + datetime.substring(5, 7) + "/" + datetime.substring(0, 4);
                String hora = datetime.substring(11, 19);
                return data + " " + hora;
            }
        } catch (Exception ex) {
            System.out.println("[API] Falha ao conectar na API de horário. Usando contingência local de Brasília.");
        }

        java.time.ZonedDateTime agoraBrasilia = java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Sao_Paulo"));
        java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agoraBrasilia.format(formatador);
    }

}