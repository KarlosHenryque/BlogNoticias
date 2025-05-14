import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MenuNoticias extends JFrame {
    private final Favorito favorito;
    private final LeituraNoticias leitura;
    private final ConexaoAPI conexaoAPI;
    private List<Noticias> noticiasList;

    private JPanel conteudoPanel;
    private JTextArea resultadoTextArea;

    public MenuNoticias() {
        favorito = new Favorito();
        leitura = new LeituraNoticias();
        conexaoAPI = new ConexaoAPI();

        String jsonResponse = conexaoAPI.obterNoticias();
        if (jsonResponse == null) {
            JOptionPane.showMessageDialog(this, "Não foi possível obter as notícias.");
            System.exit(1);
        }

        noticiasList = UtilNoticias.converterJsonParaLista(jsonResponse);
        if (noticiasList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma notícia encontrada.");
            System.exit(1);
        }

        initUI();
    }

    private void initUI() {
        setTitle("Menu de Notícias");
        setSize(850, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuPanel = new JPanel(new GridLayout(12, 1));
        add(menuPanel, BorderLayout.WEST);

        conteudoPanel = new JPanel(new BorderLayout());
        resultadoTextArea = new JTextArea();
        resultadoTextArea.setEditable(false);
        resultadoTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        conteudoPanel.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        add(conteudoPanel, BorderLayout.CENTER);

        // Botões do menu
        JButton buscarTituloBtn = new JButton("1 - Buscar Notícia por Título");
        JButton buscarDataBtn = new JButton("2 - Buscar Notícia por Data");
        JButton adicionarFavoritoBtn = new JButton("3 - Adicionar aos Favoritos");
        JButton removerFavoritoBtn = new JButton("4 - Remover dos Favoritos");
        JButton exibirFavoritosBtn = new JButton("5 - Exibir Favoritos");
        JButton marcarLidaBtn = new JButton("6 - Marcar como Lida");
        JButton lerDepoisBtn = new JButton("7 - Ler Depois");
        JButton exibirLidasBtn = new JButton("8 - Exibir Lidas");
        JButton exibirLerDepoisBtn = new JButton("9 - Exibir Ler Depois");
        JButton limparBtn = new JButton("Limpar");
        JButton sairBtn = new JButton("0 - Sair");

        // Adiciona os botões
        menuPanel.add(buscarTituloBtn);
        menuPanel.add(buscarDataBtn);
        menuPanel.add(adicionarFavoritoBtn);
        menuPanel.add(removerFavoritoBtn);
        menuPanel.add(exibirFavoritosBtn);
        menuPanel.add(marcarLidaBtn);
        menuPanel.add(lerDepoisBtn);
        menuPanel.add(exibirLidasBtn);
        menuPanel.add(exibirLerDepoisBtn);
        menuPanel.add(limparBtn);
        menuPanel.add(sairBtn);

        // Eventos dos botões
        buscarTituloBtn.addActionListener(e -> mostrarCampoBuscaTitulo());
        buscarDataBtn.addActionListener(e -> mostrarCampoBuscaData());
        adicionarFavoritoBtn.addActionListener(e -> mostrarCampoID("Adicionar aos Favoritos", favorito::adicionarFavorito));
        removerFavoritoBtn.addActionListener(e -> mostrarCampoID("Remover dos Favoritos", favorito::removerFavorito));
        exibirFavoritosBtn.addActionListener(e -> exibirLista(new ArrayList<>(favorito.getFavoritos()), "Favoritos"));
        marcarLidaBtn.addActionListener(e -> mostrarCampoID("Marcar como Lida", leitura::marcarComoLida));
        lerDepoisBtn.addActionListener(e -> mostrarCampoID("Ler Depois", leitura::adicionarParaLerDepois));
        exibirLidasBtn.addActionListener(e -> exibirLista(new ArrayList<>(leitura.getLidas()), "Lidas"));
        limparBtn.addActionListener(e -> limparConteudo());
        exibirLerDepoisBtn.addActionListener(e -> exibirLista(new ArrayList<>(leitura.getParaLerDepois()), "Para Ler Depois"));
        sairBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // Método para limpar o conteúdo exibido no conteudoPanel e resultadoTextArea
    private void limparConteudo() {
        resultadoTextArea.setText("");
        conteudoPanel.removeAll();
        conteudoPanel.revalidate();
        conteudoPanel.repaint();
    }

    private void mostrarCampoBuscaTitulo() {
        conteudoPanel.removeAll();

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField input = new JTextField(20);
        JButton buscar = new JButton("Buscar");

        inputPanel.add(new JLabel("Título ou Palavra-chave:"));
        inputPanel.add(input);
        inputPanel.add(buscar);

        buscar.addActionListener(e -> {
            String busca = RemoverAcentos.removerAcentos(input.getText().toLowerCase());
            List<Noticias> resultado = noticiasList.stream()
                    .filter(n -> RemoverAcentos.removerAcentos(n.getTitulo().toLowerCase()).contains(busca))
                    .collect(Collectors.toList());

            resultadoTextArea.setText(resultado.isEmpty()
                    ? "Nenhuma notícia encontrada."
                    : resultado.stream().map(Noticias::toString).collect(Collectors.joining("\n------------------\n")));
        });

        conteudoPanel.add(inputPanel, BorderLayout.NORTH);
        conteudoPanel.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        conteudoPanel.revalidate();
        conteudoPanel.repaint();
    }

    private void mostrarCampoBuscaData() {
        conteudoPanel.removeAll();

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField input = new JTextField(10);
        JButton buscar = new JButton("Buscar");

        inputPanel.add(new JLabel("Data (dd/MM/yyyy):"));
        inputPanel.add(input);
        inputPanel.add(buscar);

        buscar.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(input.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                List<Noticias> resultado = noticiasList.stream()
                        .filter(n -> n.getDataPublicacao().toLocalDate().equals(data))
                        .collect(Collectors.toList());

                resultadoTextArea.setText(resultado.isEmpty()
                        ? "Nenhuma notícia encontrada."
                        : resultado.stream().map(Noticias::toString).collect(Collectors.joining("\n------------------\n")));
            } catch (DateTimeParseException ex) {
                resultadoTextArea.setText("Formato inválido. Use dd/MM/yyyy.");
            }
        });

        conteudoPanel.add(inputPanel, BorderLayout.NORTH);
        conteudoPanel.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        conteudoPanel.revalidate();
        conteudoPanel.repaint();
    }

    private void mostrarCampoID(String titulo, java.util.function.IntConsumer acao) {
        conteudoPanel.removeAll();

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField input = new JTextField(5);
        JButton executar = new JButton("Executar");

        inputPanel.add(new JLabel("ID da Notícia:"));
        inputPanel.add(input);
        inputPanel.add(executar);

        executar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(input.getText());
                Noticias noticia = noticiasList.stream()
                        .filter(n -> n.getId() == id)
                        .findFirst().orElse(null);

                if (noticia != null) {
                    acao.accept(id);
                    resultadoTextArea.setText(titulo + " realizada com sucesso:\n\n" + noticia);
                } else {
                    resultadoTextArea.setText("ID não encontrado.");
                }
            } catch (NumberFormatException ex) {
                resultadoTextArea.setText("Entrada inválida. Digite um número.");
            }
        });

        conteudoPanel.add(inputPanel, BorderLayout.NORTH);
        conteudoPanel.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        conteudoPanel.revalidate();
        conteudoPanel.repaint();
    }

    private void exibirLista(List<Integer> idList, String titulo) {
        conteudoPanel.removeAll();

        List<Noticias> resultado = idList.stream()
                .map(id -> noticiasList.stream().filter(n -> n.getId() == id).findFirst().orElse(null))
                .filter(n -> n != null)
                .collect(Collectors.toList());

        resultadoTextArea.setText(resultado.isEmpty()
                ? "Nenhuma notícia encontrada em " + titulo + "."
                : resultado.stream().map(Noticias::toString).collect(Collectors.joining("\n------------------\n")));

        conteudoPanel.add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        conteudoPanel.revalidate();
        conteudoPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuNoticias::new);
    }
}
