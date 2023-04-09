//Feito por: Cláudio Ribeiro de Sousa Júnior; Caio Passos da Silva Moreira, Alessandra Raissa Branco Lima.

package com.example;
import javafx.application.Application; // Esta biblioteca fornece a classe Application, que é o ponto de partida para as aplicações JavaFX.
import javafx.event.EventHandler; // Esta biblioteca fornece a interface EventHandler, que é usada para lidar com eventos em JavaFX.
import javafx.geometry.Insets;
import javafx.geometry.Pos; // Esta biblioteca fornece a classe Pos, que é usada para especificar a posição dos nós em um layout.
import javafx.scene.Scene; // Esta biblioteca fornece a classe Scene, que representa os elementos visuais de uma aplicação JavaFX.
import javafx.scene.control.Label; // Esta biblioteca fornece a classe Label, que é um controle de texto não editável que pode exibir uma única linha de texto.
import javafx.scene.input.MouseEvent; // Esta biblioteca fornece a classe MouseEvent, que representa um evento do mouse em JavaFX.
import javafx.scene.layout.GridPane; // Esta biblioteca fornece a classe GridPane, que é um gerenciador de layout que organiza nós em um padrão de grade.
import javafx.scene.layout.Pane;
import javafx.stage.Stage; // Esta biblioteca fornece a classe Stage, que representa o contêiner de nível superior para uma aplicação JavaFX.
import javafx.scene.control.Alert; //Esta biblioteca fornece a classe Alerta, que permite exibir mensagens atraves de uma GUI do JavaFX
import javafx.scene.control.Alert.AlertType;  //Esta biblioteca fornece a classe Alerta, que permite exibir e escrever mensagens atraves de uma GUI do JavaFX
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration; //novas bibliotecas


public class NumberPuzzle extends Application {
    private Label timerLabel;
    private Timeline timeline;
    private Duration timerDuration = Duration.ZERO;
    private static final int ROWS = 4; // Definindo numero de linhas
    private static final int COLS = 4; //Definindo o numero de colunas 
    private static final int TILE_SIZE = 100; //Define o tamanho dos quadrados do jogo

    private int emptyRow; // Declarando linhas vazia 
    private int emptyCol; //Declarando coluna vazia 


    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane(); //Instanciando o grid do game 
        grid.setAlignment(Pos.CENTER); // Método definindo o padrão de alinhamento do Grid para o centro
        grid.setHgap(10); //Método definindo o gap horizontal entre o itens para 10 px 
        grid.setVgap(10); //Método definindo o gap da altura entre os itens para 10 px 

        int[][] nums = new int[ROWS][COLS]; //Instanciando um array 2D para criação da matriz de linhas e colunas 

        // gera uma sequência aleatória de números de 1 a 15
        int[] sequence = new int[ROWS * COLS - 1];
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = i + 1;
        }
        shuffleArray(sequence); //Método para aleatoriarização da sequencia de valores pre-definidas no array 2D 

        // insire os números na grade
        int index = 0; //inicializando o valor do indice
        for (int i = 0; i < ROWS; i++) { //Iterando o valor inicializando sendo menor do que a quantidade de linhas && i++
            for (int j = 0; j < COLS; j++) {  //iterando o valor inicializado sendo menor do que a quantidade de colunas && j++
                if (i == ROWS - 1 && j == COLS - 1) { //Condicional para que o número de linhas seja igual ao número de colunas
                    // a última célula deve estar vazia
                    //Visualizar parametros para implementação do espaço vazio dinâmico para versão 3 do jogo. Associar .MathRandom para spawn aleatório do emptyRow & emptyCol
                    emptyRow = i; //se a ultima linha for a ultima linha, ela seta esta celula vazia para esta celula
                    emptyCol = j;  //se a ultima coluna for a ultima coluna, ela seta esta celula vazia para esta celula
                } else {
                    nums[i][j] = sequence[index++]; //Guarda um dos os números da sequencia em um dos campos do array. O index++ incrementa para puxar o próximo numero da sequencia
                    Label label = new Label(Integer.toString(nums[i][j])); //cria um label para cada um dos valores (em cast para string ) da matriz pré-determinada
                    label.setPrefSize(TILE_SIZE, TILE_SIZE); //Define o tamanho do label
                    label.setAlignment(Pos.CENTER); //Define o alinhamento do label para o centro
                    label.setStyle("-fx-border-color: black"); //O estilo do label: borda de cor preta
                    label.setOnMouseClicked(new EventHandler<MouseEvent>() { //Adiciona um event listener do tipo MouseEvent,
                                                                            //utilizando o método para criar um eventListener para cada Label.
                                                                            // a classe EventHandler é utilizada para trocar o label para o lugar da Empty Label
                        public void handle(MouseEvent event) { //Passando o parametro para o MouseEvent
                            Label clickedLabel = (Label) event.getSource(); //Recebe o clique em cada label
                            int clickedRow = GridPane.getRowIndex(clickedLabel); //Recebe a label da e linha do Label que recebeu o clique
                            int clickedCol = GridPane.getColumnIndex(clickedLabel); //Recebe a label da coluna do Label que recebeu o clique
                            if (isValidMove(clickedRow, clickedCol)) { //Se for um movimento válido(se pode ser movimentado para o espaço vazio)
                                swapLabels(clickedLabel, emptyRow, emptyCol); //caso seja possível: o label clickado será alternado com o Label vazio
                                emptyRow = clickedRow; //então, a linha vazia será atribuida a linha clickada
                                emptyCol = clickedCol; //então, a coluna vazia será atribuida a linha clickada
                            }
                        }
                    });
                    //Contador deve entrar por aqui.
                    grid.add(label, j, i); //Uma instancia do GridPane, nos argumentos do método Add(): Label a ser adicionado, Linha e coluna da celula onde o label deve ser adicionado

                }
            }
        }

        timerLabel = new Label("00:00");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timerDuration = timerDuration.add(Duration.seconds(1));
            timerLabel.setText(formatDuration(timerDuration));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        grid.setHgap(10);
        grid.setVgap(10);
        Pane pane = new Pane(grid, timerLabel);
        pane.setPadding(new Insets(10));

        Scene scene = new Scene(pane, COLS * TILE_SIZE, ROWS * TILE_SIZE);

        primaryStage.setScene(scene); //Define a cena
        primaryStage.setTitle("Number Puzzle"); //Dá nome a cena
        //Observar para implementar o contador
        timeline.play();
        primaryStage.show(); //Exibe a cena
    }
    private String formatDuration(Duration duration) {
        long seconds = (long) duration.toSeconds();
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Verifica se o bloco clicado pode ser movido
    private boolean isValidMove(int row, int col) {  //Verifica se pode mover, verificando cada lado do label, sendo possível, retornará true, senão, false.
        if (row == emptyRow && col == emptyCol - 1 || // verifica se as linhas e as colunas possuem o mesmo valor em quantia. 
                                                        //ex: se linha= 3 e coluna =3 ou linha=2+1 e coluna=3... será verdadeiro
                row == emptyRow && col == emptyCol + 1 ||
                row == emptyRow - 1 && col == emptyCol ||
                row == emptyRow + 1 && col == emptyCol) {
            return true;
        } else {
            return false;
        }
    }

    // Troca a célula clicada pela célula vazia
    private void swapLabels(Label clickedLabel, int row, int col) { 
        Label emptyLabel = new Label(); //instanciando o Label vazio 
        emptyLabel.setPrefSize(TILE_SIZE, TILE_SIZE); //define o tamanho do Label 
        emptyLabel.setStyle("-fx-border-color: black"); //Define o estilo do label, definindo borda na cor preta
        GridPane.setRowIndex(emptyLabel, emptyRow); //Define a linha da celula trocada pela Label vazia
        GridPane.setColumnIndex(emptyLabel, emptyCol); //Define a coluna da celula trocada pela label vazia

        GridPane grid = (GridPane) clickedLabel.getParent(); //Define grau de parentesco a celula que recebeu o clique 
        grid.getChildren().remove(emptyLabel); //remove o Label da celula vazia clicada 
        grid.add(emptyLabel, emptyCol, emptyRow); //Sobreescrever valores atribuidos previamente ao campos vazios
        grid.getChildren().remove(clickedLabel);//remover o valor atribuído quando clicado na celula filha 
        grid.add(clickedLabel, col, row); //sobreescrever valores ao item clicado 
        grid.getChildren().remove(clickedLabel); //Remove novamente valores atribuídos ao Label clicado
        grid.add(clickedLabel, col, row); //Sobreescreve novamente valores ao item 
    }
    
   // Embaralhe a matriz usando o algoritmo de Fisher-Yates (método para desordenar e mostrar resultados sempre embaralhados)
   private void shuffleArray(int[] array) { //função que embaralha a matriz para sempre resultar em posições pseudoaleatórias
    for (int i = array.length - 1; i > 0; i--) { //o tamanho do array terá "1" sendo decrementado até que itere a quantia tamanho do array
        int j = (int) Math.floor(Math.random() * (i + 1)); // "j" vai receber um valor aleatório para ser usado depois como índice
        int temp = array[i]; //objeto temp recebe o array do "i" do tamanho
        array[i] = array[j]; //o i vai para o j e vice e versa deixando aleatório 
        array[j] = temp; // "j", agora com o índice "i", é atribuido ao objeto "temp"
    }
}

public void endGame(int[][] nums) { //Estrutura para o final do game 
    if (nums[ROWS - 1][COLS - 1] == 0) { //se o valor dentro da estrutura válida de linhas e colunas for == 0 
        boolean ascending = true; // a condição crescente sendo verdadeira 
        for (int l = 0; l < ROWS; l++) { //L de linha, sendo menor que o numero de linhas (incrementando +1)
            for (int a = 0; a < COLS - 1; a++) { ////C de coluna, sendo menor que o numero de Colunas - 1 (incrementando +1)
                //este nested loop permite uma varredura da matriz de linhas e colunas
                if (nums[l][a] > nums[l][a + 1]) { //se o valor correspondente do campo da matriz for maior do que o próxim...
                   ascending = false; //.... a condição ascending será falsa
                    break; //fim do teste
                }
            }
            if (!ascending) { //se não for ascending... 
                break; //então fim do teste
            }
        }
        if (ascending) { //caso seja ascending
            Alert alert = new Alert(AlertType.INFORMATION); //Uma nova instancia do objeto de controle alerta da biblioteca JavaFX
            alert.setTitle("Parabens!"); //Definindo o titulo da aba do objeto
            alert.setHeaderText(null); //Definindo o cabeçalho como 'null'
            alert.setContentText("Voce ganhou!"); //Definindo o conteúdo do alerta
            alert.showAndWait(); //Definindo que irá exibir e aguardar o usuário 
        }
    }
}

public class Chronometer extends Label {
    private long startTime;

    public Chronometer() {
        this.startTime = System.currentTimeMillis();
        setText("0.00");
    }

    public void update() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double seconds = elapsedTime / 1000.0;
        setText(String.format("%.2f", seconds));
    }
}
    public static void main(String[] args) {
        launch(args);  //Método main para inicializar o game 
    }

    public static void setRoot(String string) {
    }
}