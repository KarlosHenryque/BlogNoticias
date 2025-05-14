import java.util.*;

public class ExibirNoticias {

    public static void exibir(List<Noticias> noticias){
        for (Noticias noticia : noticias){
            System.out.println(noticia);
            System.out.println("------------------------------------");
        }
    }
}