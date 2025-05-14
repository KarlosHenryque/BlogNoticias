import java.util.*;

public class Favorito {

    private Set<Integer> favoritos;

    public Favorito() {
        favoritos = new HashSet<>();
    }

    // Adicionar notícia aos favoritos
    public void adicionarFavorito(int idNoticia) {
        if (favoritos.add(idNoticia)) {
            System.out.println("Notícia adicionada aos favoritos.");
        } else {
            System.out.println("Esta notícia já está nos favoritos.");
        }
    }

    // Remover notícia dos favoritos
    public void removerFavorito(int idNoticia) {
        if (favoritos.remove(idNoticia)) {
            System.out.println("Notícia removida com sucesso.");
        } else {
            System.out.println("Essa notícia não está nos favoritos.");
        }
    }

    // Obter o conjunto de favoritos
    public Set<Integer> getFavoritos() {
        return favoritos;
    }

    // Exibir favoritos com base na lista de notícias
    public void exibirFavoritos(List<Noticias> noticiasList) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n----- Notícias Favoritas -----\n");
        for (int id : favoritos) {
            // Verificar se o ID é válido antes de acessar a lista
            if (id >= 0 && id < noticiasList.size()) {
                Noticias noticia = noticiasList.get(id);
                sb.append(noticia).append("\n------------------------------------\n");
            } else {
                sb.append("ID de notícia inválido: ").append(id).append("\n");
            }
        }

        // Se a string estiver vazia, significa que não há favoritos
        if (sb.length() == 0) {
            sb.append("Nenhuma notícia favorita.");
        }

        // Exibe as notícias favoritas na tela (isso pode ser ajustado para usar a interface gráfica)
        System.out.println(sb.toString());
    }
}
