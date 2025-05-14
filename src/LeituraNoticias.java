import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class LeituraNoticias {
    private Set<Integer> lidas = new HashSet<>();
    private Set<Integer> paraLerDepois = new HashSet<>();

    public void marcarComoLida(int id) {
        lidas.add(id);
    }

    public void adicionarParaLerDepois(int id) {
        paraLerDepois.add(id);
    }

    public void exibirListaComOrdenacao(List<Noticias> noticiasList, List<Integer> idList, String titulo, JTextArea resultadoTextArea) {
        if (idList.isEmpty()) {
            resultadoTextArea.setText("\nNenhuma notícia na lista: " + titulo);
            return;
        }

        List<Noticias> lista = idList.stream()
                .filter(id -> id >= 0 && id < noticiasList.size())
                .map(noticiasList::get)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("\n----- " + titulo + " -----\n");

        // Exibe os resultados
        for (Noticias noticia : lista) {
            sb.append(noticia.getTitulo()).append("\n");
        }

        // Exibe o texto na área de resultado
        resultadoTextArea.setText(sb.toString());
    }

    public Set<Integer> getLidas() {
        return lidas;
    }

    public Set<Integer> getParaLerDepois() {
        return paraLerDepois;
    }
}
