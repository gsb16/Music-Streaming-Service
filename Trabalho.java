/*
Trabalho - Técnicas Alternativas de Programação

Autores:
	Gabriel de Souza Barreto - 20166812
	Guilherme Bastos de Oliveira - 20167818
*/

import java.util.Random;
import java.util.ArrayList;

/*	Nome: Visitante
	Padrão: Visitor
	Elemento do padrão: Visitor
	Objetivo: Fornecer a interface base para construir e receber visitantes */
interface Visitante{
	abstract public void visita(Colecao colecao);
	abstract public void visita(Musica musica);
}

/*	Nome: Elemento
	Padrão: Visitor
	Elemento do padrão: Element
	Objetivo: Criar a base para recebimento de visitantes */
interface Elemento{
	void recebe(Visitante visitante);
}

/*	Nome: Colecao
	Padrão: Composite
	Elemento do padrão: Component
	Objetivo: Classe base para composite e leaf */
abstract class Colecao implements Elemento{
	String nome;
	String estilo;
}

/*	Nome: ImprimeCompleto
	Padrão: Visitor
	Elemento do padrão: ConcreteVisitor
	Objetivo: Imprime todos os elementos do diretorio */
class ImprimeCompleto implements Visitante{
	public void visita(Colecao colecao){
		if(colecao instanceof Playlist){
			System.out.println(" "+colecao.nome+" - "+colecao.estilo);
		} else {
			System.out.println("   "+colecao.nome+" - "+colecao.estilo);
		}
	}

	public void visita(Musica musica){
		System.out.println("     "+musica.nome+" - "+musica.estilo);
	}
}

/*	Nome: ImprimeMusicasEstilo
	Padrão: Visitor
	Elemento do padrão: ConcreteVisitor
	Objetivo: Imprime todas as musicas de determinado estilo */
class ImprimeMusicasEstilo implements Visitante{
	String estilo;

	ImprimeMusicasEstilo(String e){
		estilo = e;
	}

	public void visita(Colecao colecao){}

	public void visita(Musica musica){
		if(musica.estilo.equals(estilo)){
			System.out.println("     "+musica.nome+" - "+musica.estilo);
		}
	}
}

/*	Nome: ImprimeEstiloeTempo
	Padrão: Visitor
	Elemento do padrão: ConcreteVisitor
	Objetivo: Imprime todas as musicas de determinado estilo e com duração
	superior a 3 minutos */
class ImprimeEstiloeTempo implements Visitante{
	String estilo;

	ImprimeEstiloeTempo(String e){
		estilo = e;
	}

	public void visita(Colecao colecao){}

	public void visita(Musica musica){
		if((musica.estilo.equals(estilo)) && (musica.duracao > 180)){
			System.out.println("     "+musica.nome+" - "+musica.estilo);
		}
	}
}

/*	Nome: Playlist
	Padrão: Visitor e Composite
	Elemento do padrão: ConcreteElement e Composite
	Objetivo: Armazena albuns e playlists */
class Playlist extends Colecao implements Elemento{
	String estiloSecundario;
	ArrayList<Colecao> elementos = new ArrayList<Colecao>();

	Playlist(String n, String e1, String e2){
		nome = n;
		estilo = e1;
		estiloSecundario = e2;
	}

	public void adicionaColecao(Colecao colecao){
		elementos.add(colecao);
	}

	public void recebe(Visitante visitante){
		visitante.visita(this);
		for(Colecao colecao : elementos){
			colecao.recebe(visitante);
		}
	}

}

/*	Nome: Album
	Padrão: Visitor e Composite
	Elemento do padrão: ConcreteElement e Leaf
	Objetivo: Armazena musicas */
class Album extends Colecao implements Elemento{
	String nomeBanda;
	ArrayList<Musica> musicas = new ArrayList<Musica>();

	Album(String n, String e, String nb){
		nome = n;
		estilo = e;
		nomeBanda = nb;
	}

	public void adicionaMusica(Musica musica){
		musicas.add(musica);
	}

	public void recebe(Visitante visitante){
		visitante.visita(this);
		for(Musica musica : musicas){
			musica.recebe(visitante);
		}
	}
}

/*	Nome: Playlist
	Padrão: Visitor
	Elemento do padrão: ConcreteElement
	Objetivo: Armazena informações sobre uma musica */
class Musica implements Elemento{
	String nome;
	String estilo;
	int duracao; //duração em segundos

	Musica(String n, String e, int d){
		nome = n;
		estilo = e;
		duracao = d;
	}

	public void recebe(Visitante visitante){
		visitante.visita(this);
	}
}

/*	Nome: ServicoStreaming
	Padrão: Visitor e Singleton
	Elemento do padrão: ConcreteElement e Singleton
	Objetivo: Armazena dados do servico e usuário */
class ServicoStreaming implements Elemento{
	String login;
	String senha;
	String plano;
	private static ServicoStreaming instancia;
	ArrayList<Playlist> diretorio = new ArrayList<Playlist>();

	//getInstance()
	public static ServicoStreaming instanciaServico(){
		if(instancia == null){
			instancia = new ServicoStreaming();
		}
		return instancia;
	}

	public void adicionaPlaylist(Playlist plist){
		diretorio.add(plist);
	}

	public void recebe(Visitante visitante){
		for(Playlist playlist : diretorio){
			playlist.recebe(visitante);
		}
	}
}

public class Trabalho{
	public static void main(String[] args){
		//Instanciação e definição de parametros de um serviço de streaming
		ServicoStreaming servico = ServicoStreaming.instanciaServico();
		servico.login = "dummy";
		servico.senha = "password";
		servico.plano = "free";

		//Para gerar numeros aleatorios
		Random aleatorio = new Random();

		//Instanciando playlists, albuns e musicas
		for(int i = 1; i <= 5; i++){
			Playlist p = new Playlist("Playlist"+i, "Estilo"+(aleatorio.nextInt(10)+1), "EstiloSecundario"+(aleatorio.nextInt(10)+1));
			for(int j = 1; j <= 4; j++){
				Album a = new Album("Album"+(i*4+j), "Estilo"+(aleatorio.nextInt(10)+1), "NomeBanda"+j);
				for(int k = 1; k <= 5; k++){
					Musica m = new Musica("Musica"+(i*4+j)+"-"+k, "Estilo"+(aleatorio.nextInt(10)+1), 120+aleatorio.nextInt(121));
					a.adicionaMusica(m);
				}
				p.adicionaColecao(a);
			}
			servico.adicionaPlaylist(p);
		}

		//P1 recebe P3
		servico.diretorio.get(0).adicionaColecao(servico.diretorio.get(2));
		//P2 recebe P5
		servico.diretorio.get(1).adicionaColecao(servico.diretorio.get(4));

		//Chamando os visitantes para realizar as impressões
		System.out.println("Imprimindo todos os elementos:");
		servico.recebe(new ImprimeCompleto());
		int rand = (aleatorio.nextInt(10)+1);
		System.out.println("Imprimindo as musicas do estilo 'Estilo"+rand+"'");
		servico.recebe(new ImprimeMusicasEstilo("Estilo"+rand));
		rand = (aleatorio.nextInt(10)+1);
		System.out.println("Imprimindo as musicas do estilo 'Estilo"+rand+"' e duração maior que 3 minutos");
		servico.recebe(new ImprimeEstiloeTempo("Estilo"+rand));
	}
}
