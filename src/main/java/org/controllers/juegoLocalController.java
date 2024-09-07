package org.controllers;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.Modules.*;
import javafx.scene.layout.Pane;

import static org.controllers.App.loadFXML;
public class juegoLocalController implements Initializable{

    public static final Color[] ColoresPosibles =
            {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE};
    public static final Color[] ColoresClaros = {Color.PINK, Color.LIGHTBLUE,
            Color.LIGHTYELLOW, Color.LIGHTGREEN, Color.PEACHPUFF};
    private int puntaje=0;
    private int puntaje1=0;
    private int multiplicador=1;
    private int multiplicador1=1;
    private int cont2=0;
    private int cont=0;
    private Song cancionSeleccionada;
    public Timeline timeline;
    public Timeline timeline2;
    private Tecla aux;
    private Tecla aux2;
    private final ArrayList<Circle> teclasEnPantalla = new ArrayList<>();
    private final ArrayList<Circle> teclasEnPantalla2 = new ArrayList<>();
    private final ArrayList<TeclaLarga> teclasLargasEnPantalla = new ArrayList<>();
    private final ArrayList<TeclaLarga> teclasLargasEnPantalla2 = new ArrayList<>();
    private long t_inicio;      //Tiempo global
    private boolean reproduciendo;
    //Delay necesario para que las teclas aparezcan on time, se modifica en funcion de la velocidad de bajada de las teclas
    private MediaPlayer reproductor;
    private CustomRunnable<Button,Color> effect;
    private long test_time_inicio1;
    private long test_time_inicio2;
    Sprite sprite;
    Sprite guitar;
    Sprite bate;
    Sprite man;
    private final Map<KeyCode,PauseTransition> pausaTeclas = new HashMap<>();


    //Variables de componentes del FXML
    @FXML
    AnchorPane principal;
    @FXML
    Button RedButton;
    @FXML
    Button BlueButton;
    @FXML
    Button YellowButton;
    @FXML
    Button GreenButton;
    @FXML
    Button OrangeButton;
    @FXML
    Button RedButton1;
    @FXML
    Button BlueButton1;
    @FXML
    Button YellowButton1;
    @FXML
    Button GreenButton1;
    @FXML
    Button OrangeButton1;
    @FXML
    Pane PausePane;
    @FXML
    Rectangle HorizontalRect;
    @FXML
    Line l1;
    @FXML
    Line l2;
    @FXML
    Line l3;
    @FXML
    Line l4;
    @FXML
    Line l5;
    @FXML
    Rectangle HorizontalRect1;
    @FXML
    Line l11;
    @FXML
    Line l21;
    @FXML
    Line l31;
    @FXML
    Line l41;
    @FXML
    Line l51;
    @FXML
    Label scoref;
    @FXML
    Label scoref1;
    @FXML
    Label multiplo;
    @FXML
    Label multiplo1;
    @FXML
    Label Ganador;
    @FXML
    Pane finalPanel;
    @FXML
    ImageView bateria;
    @FXML
    ImageView mano;
    @FXML
    ImageView guitarra;
    @FXML
    ImageView manos;
    @FXML
    ImageView piso;
    @FXML
    ImageView piso1;
    @FXML
    ImageView backg2;
    @FXML
    ProgressBar BarraM;
    @FXML
    ProgressBar BarraP1;
    @FXML
    ProgressBar BarraP2;


    public void setCancionSeleccionada(Song cancionSeleccionada) {
        this.cancionSeleccionada = cancionSeleccionada;
    }

    /**
     *Metodo de inicializacion para la Ventana juego, en la cual se settean varios parametros importantes para el juego
     * como la inicializacion de los eventos de teclado y botones,
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initKeyboard();
        this.initButtons();
        this.t_inicio = System.currentTimeMillis();
        this.Ganador.setText("");
    }
    /**
     * Metodo que termina de inicializar la ventana ya teniendo todos los recursos que necesita, como la cancion
     * el mediaPlayer como lo son la timeLine de la animacion, la
     * inicializacion de la cancion y finalmente se da una pausa de 2 segundos para que el jugador se prepare y despues
     * se llama al metodo play de timeline para iniciar la animacion del juego,
     */
    public void postInitialize(){
        this.initReproduction();
        this.initTimeline();

        this.timeline.play();
        this.timeline2.play();
        this.test_time_inicio1 = System.currentTimeMillis();
        this.test_time_inicio2 = System.currentTimeMillis();
        Image img = new Image(Paths.get("src/main/resources/images/mano.png").toUri().toString());
        Image img2 = new Image(Paths.get("src/main/resources/images/guitarra.gif").toUri().toString());
        Image img3 = new Image(Paths.get("src/main/resources/images/bateria.gif").toUri().toString());
        Image img4 = new Image(Paths.get("src/main/resources/images/manis.png").toUri().toString());
        mano.setImage(img);
        guitarra.setImage(img2);
        bateria.setImage(img3);
        manos.setImage(img4);

        this.sprite = new Sprite(mano,4,4,(int)17.5,27,400);
        this.sprite.setCycleCount(Transition.INDEFINITE);
        this.sprite.resetAnimation();
        this.sprite.setAutoReverse(true);
        sprite.play();
        this.guitar = new Sprite(guitarra,6,6,(int)51.2,54,400);
        this.guitar.setCycleCount(Transition.INDEFINITE);
        this.guitar.resetAnimation();
        this.guitar.setAutoReverse(true);
        guitar.play();
        this.bate = new Sprite(bateria,3,3,(int)87.3,69,400);
        this.bate.setCycleCount(Transition.INDEFINITE);
        this.bate.resetAnimation();
        this.bate.setAutoReverse(true);
        bate.play();
        this.man = new Sprite(manos,3,3,(int)38.6,66,400);
        this.man.setCycleCount(Transition.INDEFINITE);
        this.man.resetAnimation();
        this.man.setAutoReverse(true);
        man.play();
    }


    private void initTimeline() {
        //REPETITION_MILIS dicta la frecuencia de refresco de la animacion,
        // mientas menor sea el numero mayor seran los fps del juego
        //Aproximadamente, 50 dan 20fps, 33 son 30 fps, 16 son 60fps
        int REPETITION_MILIS = 16;

        this.timeline = new Timeline(new KeyFrame(Duration.millis(REPETITION_MILIS), event -> print()));    //Animacion
        this.timeline.setCycleCount(Timeline.INDEFINITE);   //Ciclos que durara la animacion
        this.timeline2 = new Timeline(new KeyFrame(Duration.millis(REPETITION_MILIS), event -> print2()));    //Animacion
        this.timeline2.setCycleCount(Timeline.INDEFINITE);   //Ciclos que durara la animacion
        this.t_inicio = System.currentTimeMillis();
    }

    private void initReproduction(){
        this.aux = cancionSeleccionada.getTeclas_pulsadas().getVertice(0);
        this.aux2 = cancionSeleccionada.getTeclas_pulsadas().getVertice(0);

        //Settea reproductor con la cancion seleccionada
        String audioFilePath = cancionSeleccionada.getRuta();
        String mediaUrl = new File(audioFilePath).toURI().toString();
        Media media = new Media(mediaUrl);
        this.reproductor = new MediaPlayer(media);
        this.reproduciendo = false;
    }

    private void print() {
        long test_time_fin = System.currentTimeMillis();
        double frameDt = test_time_fin - test_time_inicio1;
        test_time_inicio1 = test_time_fin;
        updateProgress();
        updateScoreProgress();
        int xDef = 0;
        Tecla lastTecla = null;
        long t_final = System.currentTimeMillis();
        //Esto sera en milisegundos
        final long DELAY = 1580;
        if (t_final - t_inicio > DELAY && !reproduciendo) {
            reproduciendo = true;
            reproductor.play();
        }

        multiplo.setVisible(multiplicador != 1);
        multiplo.setText("x" + multiplicador);

        //El tiempo actual el cual se encuentra la cancion, se usa adelante
        double MediaPlayerCurrentTime = this.reproductor.getCurrentTime().toMillis();
        //Primero desplaza todas las teclas, tanto normales como largas, evalua si ya estan demasiado abajo (ya no se
        //presionaron) para mandar a borrarlas, tambien en las teclas largas se evalua si la tecla debe de seguir creciendo
        //o cambia de estado a transladarse
         try{
            for (Circle circulo : teclasEnPantalla) {
                fisicaCirculo(circulo, frameDt);

                if (circulo.getCenterY() > 620) {
                    if(circulo.isVisible()){
                        multiplicador=1;
                        cont = 0;
                    }
                    this.removeCircle(circulo);
                }
            }
             for (TeclaLarga teclaLarga : teclasLargasEnPantalla) {
                 if (MediaPlayerCurrentTime > teclaLarga.getTiempoFin() - DELAY && teclaLarga.isState1()){
                     teclaLarga.setEstado(ESTADOS.TRANSITION);
                 }
                 if (MediaPlayerCurrentTime > teclaLarga.getTiempoFin() - DELAY && teclaLarga.getEstado() == ESTADOS.STOPPED){
                     teclaLarga.setEstado(ESTADOS.DECREASE);
                 }
                 teclaLarga.fisica(frameDt,2);
                 if (teclaLarga.getLayoutY() + teclaLarga.getHeight() - 15 > 620) {
                     if(teclaLarga.isVisible()){
                         multiplicador=1;
                         cont = 0;
                     }
                     this.removeTeclaLarga(teclaLarga);
                 }
                 if (!teclaLarga.isState1()){
                     puntaje = puntaje+(multiplicador * 3);
                     System.out.println(puntaje);
                 }
                 if(teclaLarga.getRhomboid().getHeight() <= 0){
                     this.removeTeclaLarga(teclaLarga);
                 }
             }

        }catch (Exception ignored){}
        //Agregamos las nuevas teclas si deben de aparecer
        if (aux.getNumAristasAdyacentes() != 0) {
            if (aux.getTeclaSig().getTiempoInicio() - DELAY <= this.reproductor.getCurrentTime().toMillis()) {   //

                for (Arista i : aux.getAristasAdyacentes()) {
                    switch (i.destino().numColor) { //Dependiendo del color se inician en coordenadas en X diferentes
                        case 0:
                            xDef = 161;
                            break;
                        case 1:
                            xDef = 222;
                            break;
                        case 2:
                            xDef = 285;
                            break;
                        case 3:
                            xDef = 343;
                            break;
                        case 4:
                            xDef = 406;
                            break;
                    }
                    if (i.destino().getTiempoFin() - i.destino().getTiempoInicio() == 0 ){
                        Circle circle = new Circle(xDef, 155, 20, ColoresPosibles[i.destino().numColor]);
                        circle.setStrokeWidth(3);
                        circle.setStroke(ColoresClaros[i.destino().numColor]);
                        circle.setStrokeType(StrokeType.CENTERED);
                        principal.getChildren().add(circle);
                        teclasEnPantalla.add(circle);
                        circle.toBack();
                    }else {
                        boolean largerThanScreen = (i.destino().getTiempoFin() - i.destino().getTiempoInicio() > DELAY);
                        TeclaLarga teclaLarga = new TeclaLarga(i.destino().numColor,xDef, i.destino().getTiempoFin(),
                                largerThanScreen);
                        principal.getChildren().add(teclaLarga);
                        teclasLargasEnPantalla.add(teclaLarga);
                        teclaLarga.toBack();
                    }
                    lastTecla = i.destino();
                }
                aux = lastTecla;
            }
            this.setPositions();
        }else {
            if (teclasEnPantalla.isEmpty()) {
                scoref.setText(String.valueOf(puntaje));
                pantallaFinal();
            }
        }
    }

    private void print2() {
        long test_time_fin = System.currentTimeMillis();
        double frameDt2 = test_time_fin - test_time_inicio2;
        test_time_inicio2 = test_time_fin;
        updateScoreProgress();
        int xDef = 0;
        Tecla lastTecla = null;
        long t_final = System.currentTimeMillis();
        //Esto sera en milisegundos
        final long DELAY = 1580;
        if (t_final - t_inicio > DELAY && !reproduciendo) {
            reproduciendo = true;
            reproductor.play();
        }

        multiplo1.setVisible(multiplicador1 != 1);
        multiplo1.setText("x" + multiplicador1);

        //El tiempo actual el cual se encuentra la cancion, se usa adelante
        double MediaPlayerCurrentTime = this.reproductor.getCurrentTime().toMillis();
        //Primero desplaza todas las teclas, tanto normales como largas, evalua si ya estan demasiado abajo (ya no se
        //presionaron) para mandar a borrarlas, tambien en las teclas largas se evalua si la tecla debe de seguir creciendo
        //o cambia de estado a transladarse
        try{
            for (Circle circulo : teclasEnPantalla2) {
                fisicaCirculo2(circulo, frameDt2);

                if (circulo.getCenterY() > 620) {
                    if(circulo.isVisible()){
                        multiplicador1=1;
                        cont2 = 0;
                    }
                    this.removeCircle2(circulo);
                }
            }
            for (TeclaLarga teclaLarga : teclasLargasEnPantalla2) {
                if (MediaPlayerCurrentTime > teclaLarga.getTiempoFin() - DELAY && teclaLarga.isState1()){
                    teclaLarga.setEstado(ESTADOS.TRANSITION);
                }
                if (MediaPlayerCurrentTime > teclaLarga.getTiempoFin() - DELAY && teclaLarga.getEstado() == ESTADOS.STOPPED){
                    teclaLarga.setEstado(ESTADOS.DECREASE);
                }
                teclaLarga.fisica(frameDt2,3);
                if (teclaLarga.getLayoutY() + teclaLarga.getHeight() - 15 > 620) {
                    if(teclaLarga.isVisible()){
                        multiplicador1=1;
                        cont2 = 0;
                    }
                    this.removeTeclaLarga2(teclaLarga);
                }
                if (!teclaLarga.isState1()){
                    puntaje1 = puntaje1+(multiplicador1 * 3);
                }
                if(teclaLarga.getRhomboid().getHeight() <= 0){
                    this.removeTeclaLarga2(teclaLarga);
                }
            }
        }catch (Exception ignored){}
        //Agregamos las nuevas teclas si deben de aparecer
        if (aux2.getNumAristasAdyacentes() != 0) {
            if (aux2.getTeclaSig().getTiempoInicio() - DELAY <= this.reproductor.getCurrentTime().toMillis()) {   //

                for (Arista i : aux2.getAristasAdyacentes()) {
                    switch (i.destino().numColor) { //Dependiendo del color se inician en coordenadas en X diferentes
                        case 0:
                            xDef = 797;
                            break;
                        case 1:
                            xDef = 860;
                            break;
                        case 2:
                            xDef = 915;
                            break;
                        case 3:
                            xDef = 968;
                            break;
                        case 4:
                            xDef = 1031;
                            break;
                    }
                    if (i.destino().getTiempoFin() - i.destino().getTiempoInicio() == 0 ){
                        Circle circle = new Circle(xDef, 155, 20, ColoresPosibles[i.destino().numColor]);
                        circle.setStrokeWidth(3);
                        circle.setStroke(ColoresClaros[i.destino().numColor]);
                        circle.setStrokeType(StrokeType.CENTERED);
                        principal.getChildren().add(circle);
                        teclasEnPantalla2.add(circle);
                        circle.toBack();
                    }else {
                        boolean largerThanScreen = (i.destino().getTiempoFin() - i.destino().getTiempoInicio() > DELAY);
                        TeclaLarga teclaLarga = new TeclaLarga(i.destino().numColor,xDef, i.destino().getTiempoFin(),
                                largerThanScreen);
                        principal.getChildren().add(teclaLarga);
                        teclasLargasEnPantalla2.add(teclaLarga);
                        teclaLarga.toBack();
                    }
                    lastTecla = i.destino();
                }
                aux2 = lastTecla;
            }
            this.setPositions();
        }else {
            if (teclasEnPantalla2.isEmpty()) {
                scoref1.setText(String.valueOf(puntaje1));
                pantallaFinal();
            }
        }
    }

    public void removeCircle(Circle circle) {
        Platform.runLater(() -> {
            principal.getChildren().remove(circle);
            teclasEnPantalla.remove(circle);
        });

    }

    public void removeCircle2(Circle circle) {
        Platform.runLater(() -> {
            principal.getChildren().remove(circle);
            teclasEnPantalla2.remove(circle);
        });
    }

    public void removeTeclaLarga(TeclaLarga teclaLarga){
        Platform.runLater(() -> {
            principal.getChildren().remove(teclaLarga);
            teclasLargasEnPantalla.remove(teclaLarga);
            teclaLarga.clearAll();
        });
    }

    public void removeTeclaLarga2(TeclaLarga teclaLarga){
        Platform.runLater(() -> {
            principal.getChildren().remove(teclaLarga);
            teclasLargasEnPantalla2.remove(teclaLarga);
            teclaLarga.clearAll();
        });
    }

    /**
     * KeyListener para los eventos del teclado, registra 2, cuando la tecla se presiona, el cual maneja en general los
     * eventos, y cuando se suelta que manera la logica de cuando se sueltan las teclas largas
     */
    private void initKeyboard(){
        this.principal.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        this.principal.addEventHandler(KeyEvent.KEY_RELEASED, this::handleKeyReleased);
    }

    /**
     * Metodo para manejar el evento de keyPressed, el cual primero revisa si el juego esta activo, checa si no es pausa,
     * checa si esa tecla no estaba ya pulsada y si no la guarda en el set, despues inicia todos los eventos y animaciones
     */
    private void handleKeyPressed(KeyEvent keyEvent){
        {
            if(this.timeline.getStatus() != Animation.Status.RUNNING) return;
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.ESCAPE) {
                this.pause();
            }
            if (pausaTeclas.containsKey(code)){
                return;
            }
            //Preparamos el pauseTransition
            PauseTransition aux = new PauseTransition(Duration.millis(250));
            //TODO usar el SetOnFisished para darle un aspecto lindo al boton cuando se pulso
            //Accionamos todas las teclas
            switch (code) {
                case Q:
                    this.RedButton.setFocusTraversable(true);
                    this.RedButton.fire();
//                    aux.setOnFinished(event -> botonRojoEfectoBoninto);
                    break;
                case W:
                    this.BlueButton.setFocusTraversable(true);
                    this.BlueButton.fire();
                    //aux.setOnFinished(event -> botonAzulEfectoBoninto);
                    break;
                case E:
                    this.YellowButton.setFocusTraversable(true);
                    this.YellowButton.fire();
//                    aux.setOnFinished(event -> botonAmarilloEfectoBoninto);
                    break;
                case O:
                    this.GreenButton.setFocusTraversable(true);
                    this.GreenButton.fire();
//                    aux.setOnFinished(event -> botonVerdeEfectoBoninto);
                    break;
                case P:
                    this.OrangeButton.setFocusTraversable(true);
                    this.OrangeButton.fire();
                    break;
                case A:
                    this.RedButton1.setFocusTraversable(true);
                    this.RedButton1.fire();
                    //aux.setOnFinished(event -> botonRojoEfectoBoninto);
                    break;
                case S:
                    this.BlueButton1.setFocusTraversable(true);
                    this.BlueButton1.fire();
                    //aux.setOnFinished(event -> botonAzulEfectoBoninto);
                    break;
                case D:
                    this.YellowButton1.setFocusTraversable(true);
                    this.YellowButton1.fire();
//                    aux.setOnFinished(event -> botonAmarilloEfectoBoninto);
                    break;
                case K:
                    this.GreenButton1.setFocusTraversable(true);
                    this.GreenButton1.fire();
//                    aux.setOnFinished(event -> botonVerdeEfectoBoninto);
                    break;
                case L:
                    this.OrangeButton1.setFocusTraversable(true);
                    this.OrangeButton1.fire();
//                    aux.setOnFinished(event -> botonNaranjaEfectoBoninto);
                    break;
                default:
                    break;
            }


            pausaTeclas.put(code, aux);
            aux.play();
        }
    }

    /**
     * Manejo de la liberacion de tecla, lo cual primero evalua si el juego esta corriendo, luego quita la tecla del set
     * para que pueda volver a ser pulsada y checa si hay una tecla larga en su posicion que todavia no  deja de ser
     * pulsada
     */
    private void handleKeyReleased(KeyEvent keyEvent){
        if(this.timeline.getStatus() != Animation.Status.RUNNING) return;
        KeyCode code = keyEvent.getCode();
        PauseTransition aux = pausaTeclas.get(code);
        pausaTeclas.remove(code);

        //Fue una tecla corta, no hagas nada
        if (aux != null && aux.getStatus() == Animation.Status.RUNNING) {
            aux.stop();
        }
        //todos los eventos para manejar las teclas largas deben de ir aqui
        switch (code) {
            case Q:
                this.RedButton.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.RedButton));
                break;
            case W:
                this.BlueButton.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.BlueButton));
                break;
            case E:
                this.YellowButton.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.YellowButton));
                break;
            case O:
                this.GreenButton.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.GreenButton));
                break;
            case P:
                this.OrangeButton.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.OrangeButton));
                break;
            case A:
                this.RedButton1.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.RedButton1));
                break;
            case S:
                this.BlueButton1.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.BlueButton1));
                break;
            case D:
                this.YellowButton1.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.YellowButton1));
                break;
            case K:
                this.GreenButton1.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.GreenButton1));
                break;
            case L:

                this.OrangeButton1.setFocusTraversable(true);
                checkColitionsForKeyRelease(makeRect(this.OrangeButton1));
                break;
            default:
                break;
        }
    }



    private void initButtons(){
        this.RedButton.toFront();
        this.BlueButton.toFront();
        this.YellowButton.toFront();
        this.GreenButton.toFront();
        this.OrangeButton.toFront();
        this.RedButton1.toFront();
        this.BlueButton1.toFront();
        this.YellowButton1.toFront();
        this.GreenButton1.toFront();
        this.OrangeButton1.toFront();
        this.effect = (Button btn, Color color) -> {
            Rectangle aux = new Rectangle();
            aux.setWidth(btn.getWidth());
            aux.setHeight(btn.getHeight());
            aux.setArcWidth(30);
            aux.setArcHeight(30);
            aux.setFill(color.deriveColor(1, 1, 1, 0.5));
            aux.setX(btn.getLayoutX());
            aux.setY(btn.getLayoutY());

            principal.getChildren().add(aux);

            Timeline timeline = new Timeline();
            KeyValue kvWidth = new KeyValue(aux.widthProperty(), btn.getWidth() * 2);
            KeyValue kvHeight = new KeyValue(aux.heightProperty(), btn.getHeight() * 2);
            KeyValue kvX = new KeyValue(aux.xProperty(), btn.getLayoutX() - btn.getWidth() / 2);
            KeyValue kvY = new KeyValue(aux.yProperty(), btn.getLayoutY() - btn.getHeight() / 2);
            KeyValue kvOpacity = new KeyValue(aux.opacityProperty(), 0);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kvWidth, kvOpacity, kvX, kvHeight, kvY);

            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(event -> principal.getChildren().remove(aux));
            timeline.play();
        };
    }

    private void pantallaFinal(){
        if(puntaje > puntaje1){
            Ganador.setText("Player 1");
        } else if (puntaje == puntaje1) {
            Ganador.setText("Draw");
        } else{
            Ganador.setText("Player 2");
        }

        timeline.pause();
        timeline2.pause();
        RedButton.setDisable(true);
        BlueButton.setDisable(true);
        YellowButton.setDisable(true);
        GreenButton.setDisable(true);
        OrangeButton.setDisable(true);
        RedButton1.setDisable(true);
        BlueButton1.setDisable(true);
        YellowButton1.setDisable(true);
        GreenButton1.setDisable(true);
        OrangeButton1.setDisable(true);
        finalPanel.toFront();
        finalPanel.setVisible(true);
    }

    private void pause(){
        timeline.pause();
        timeline2.pause();
        reproductor.pause();
        RedButton.setDisable(true);
        BlueButton.setDisable(true);
        YellowButton.setDisable(true);
        GreenButton.setDisable(true);
        OrangeButton.setDisable(true);
        RedButton1.setDisable(true);
        BlueButton1.setDisable(true);
        YellowButton1.setDisable(true);
        GreenButton1.setDisable(true);
        OrangeButton1.setDisable(true);
        PausePane.toFront();
        PausePane.setVisible(true);

    }


    @FXML
    public void continuar() {
        PausePane.setVisible(false);
        RedButton.setDisable(false);
        BlueButton.setDisable(false);
        YellowButton.setDisable(false);
        GreenButton.setDisable(false);
        OrangeButton.setDisable(false);
        RedButton1.setDisable(false);
        BlueButton1.setDisable(false);
        YellowButton1.setDisable(false);
        GreenButton1.setDisable(false);
        OrangeButton1.setDisable(false);
        timeline.play();
        timeline2.play();
        reproductor.play();
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Pane root = loadFXML("Menu");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reproductor.stop();
        timeline.stop();
        timeline2.stop();
    }

    @FXML
    private void btnRActivado(){
        Rectangle rect = this.makeRect(RedButton);
        checkColitions(rect);
        effect.run(RedButton,Color.RED);
    }


    @FXML
    private void btnBActivado(){
        Rectangle rect = this.makeRect(BlueButton);
        checkColitions(rect);
        effect.run(BlueButton,Color.BLUE);


    }


    @FXML
    private void btnYActivado(){
        Rectangle rect = this.makeRect(YellowButton);
        checkColitions(rect);
        effect.run(YellowButton,Color.YELLOW);


    }


    @FXML
    private void btnGActivado(){
        Rectangle rect = this.makeRect(GreenButton);
        checkColitions(rect);
        effect.run(GreenButton,Color.GREEN);


    }


    @FXML
    private void btnOActivado(){
        Rectangle rect = this.makeRect(OrangeButton);
        checkColitions(rect);
        effect.run(OrangeButton,Color.ORANGE);

    }
    @FXML
    private void btnR1Activado(){
        Rectangle rect = this.makeRect(RedButton1);
        checkColitions2(rect);
        effect.run(RedButton1,Color.RED);

    }


    @FXML
    private void btnB1Activado(){
        Rectangle rect = this.makeRect(BlueButton1);
        checkColitions2(rect);
        effect.run(BlueButton1,Color.BLUE);


    }


    @FXML
    private void btnY1Activado(){
        Rectangle rect = this.makeRect(YellowButton1);
        checkColitions2(rect);
        effect.run(YellowButton1,Color.YELLOW);

    }


    @FXML
    private void btnG1Activado(){
        Rectangle rect = this.makeRect(GreenButton1);
        checkColitions2(rect);
        effect.run(GreenButton1,Color.GREEN);

    }


    @FXML
    private void btnO1Activado(){
        Rectangle rect = this.makeRect(OrangeButton1);
        checkColitions2(rect);
        effect.run(OrangeButton1,Color.ORANGE);

    }

    private Rectangle makeRect(Button btn){
        return new Rectangle(btn.getLayoutX(), btn.getLayoutY(), btn.getWidth(), btn.getHeight());
    }

    private void setPositions(){

        this.HorizontalRect.toBack();
        this.l1.toBack();
        this.l2.toBack();
        this.l3.toBack();
        this.l4.toBack();
        this.l5.toBack();
        this.HorizontalRect1.toBack();
        this.l11.toBack();
        this.l21.toBack();
        this.l31.toBack();
        this.l41.toBack();
        this.l51.toBack();
        this.piso.toBack();
        this.piso1.toBack();
        this.backg2.toBack();
        this.finalPanel.toFront();
        this.PausePane.toFront();
    }

    private void checkColitions(Rectangle rect){
        for (Circle circle : teclasEnPantalla) {
            if (checkCircle(rect,circle)) return;
        }
        for (TeclaLarga t : teclasLargasEnPantalla) {
            Circle aux = t.getCircle();
            if (checkCircle(rect,aux)) {
                t.toggleState();
                if (t.isLargerThanScreen()){
                    System.out.println("PARADOOOO");
                    t.setEstado(ESTADOS.STOPPED);
                }else {
                    System.out.println("DECRECE 222");
                    t.setEstado(ESTADOS.DECREASE);
                }
                return;
            }
        }
        multiplicador=1;
        cont = 0;
    }
    private void checkColitions2(Rectangle rect){
        for (Circle circle : teclasEnPantalla2) {
            if (checkCircle(rect,circle)) return;
        }
        for (TeclaLarga t : teclasLargasEnPantalla2) {
            Circle aux = t.getCircle();
            if (checkCircle2(rect,aux)) {
                t.toggleState();
                if (t.isLargerThanScreen()){
                    System.out.println("PARADOOOO");
                    t.setEstado(ESTADOS.STOPPED);
                }else {
                    System.out.println("DECRECE 222");
                    t.setEstado(ESTADOS.DECREASE);
                }
                return;
            }
        }
        multiplicador1=1;
        cont2 = 0;
    }

    private boolean checkCircle(Rectangle rect, Circle circle){
        if (rect.contains(circle.getCenterX(), circle.getCenterY()) ||
                rect.contains(circle.getCenterX(), circle.getCenterY() + circle.getRadius()) ||
                rect.contains(circle.getCenterX(), circle.getCenterY() - circle.getRadius())){

            puntaje = puntaje+(multiplicador * 15);
            cont++;

            if(cont>10 && cont<16){
                multiplicador=2;
            }
            else {
                if (cont >= 16 && cont < 35) {
                    multiplicador = 4;
                }
                else {
                    if (cont >= 35) {
                        multiplicador = 8;
                    }
                }
            }
            circle.setVisible(false);

            //Esto es mil veces mejor que destruirlo aqui
            return true;
        }
        return false;
    }

    private boolean checkCircle2(Rectangle rect, Circle circle){
        if (rect.contains(circle.getCenterX(), circle.getCenterY()) ||
                rect.contains(circle.getCenterX(), circle.getCenterY() + circle.getRadius()) ||
                rect.contains(circle.getCenterX(), circle.getCenterY() - circle.getRadius())){

            puntaje1 = puntaje1+(multiplicador1 * 15);
            cont2++;

            if(cont2>10 && cont2<16){
                multiplicador1=2;
            }
            else {
                if (cont2 >= 16 && cont2 < 35) {
                    multiplicador1 = 4;
                }
                else {
                    if (cont2 >= 35) {
                        multiplicador1 = 8;
                    }
                }
            }
            circle.setVisible(false);

            //Esto es mil veces mejor que destruirlo aqui
            return true;
        }
        return false;
    }

    private void checkColitionsForKeyRelease(Rectangle rect){
        TeclaLarga choosenOne = null;
        for (TeclaLarga t : teclasLargasEnPantalla) {
            Circle aux = t.getCircle();
            if (rect.contains(aux.getCenterX(), aux.getCenterY()) ||
                    rect.contains(aux.getCenterX(), aux.getCenterY() + aux.getRadius()) ||
                    rect.contains(aux.getCenterX(), aux.getCenterY() - aux.getRadius())){
                if (t.getRhomboid().getHeight() >= 30){
                    multiplicador=1;
                    cont = 0;
                    choosenOne = t;
                    break;
                }
            }
        }
        if (choosenOne != null) {
            this.removeTeclaLarga(choosenOne);
        }
    }

    private void checkColitionsForKeyRelease2(Rectangle rect){
        TeclaLarga choosenOne = null;
        for (TeclaLarga t : teclasLargasEnPantalla2) {
            Circle aux = t.getCircle();
            if (rect.contains(aux.getCenterX(), aux.getCenterY()) ||
                    rect.contains(aux.getCenterX(), aux.getCenterY() + aux.getRadius()) ||
                    rect.contains(aux.getCenterX(), aux.getCenterY() - aux.getRadius())){
                if (t.getRhomboid().getHeight() >= 30){
                    multiplicador1=1;
                    cont2 = 0;
                    choosenOne = t;
                    break;
                }
            }
        }
        if (choosenOne != null) {
            this.removeTeclaLarga2(choosenOne);
        }
    }

    public static void fisicaCirculo(Circle circulo,double dt){
        double factor = dt / 4 ;
        double factor1 = factor / 6;
        double factor2 = factor / 12;
        circulo.setCenterY(circulo.getCenterY() + factor);
        if(circulo.getCenterX() < 160 ){
            circulo.setCenterX(circulo.getCenterX() - factor1);
        }
        else if(circulo.getCenterX() > 405){
            circulo.setCenterX(circulo.getCenterX() + factor1);
        }
        else if(circulo.getCenterX() < 285){
            circulo.setCenterX(circulo.getCenterX() - factor2);
        }else if(circulo.getCenterX() > 285){
            circulo.setCenterX(circulo.getCenterX() + factor2);
        }
    }

    public static void fisicaCirculo2(Circle circulo,double dt){
        double factor = dt / 4 ;
        double factor1 = factor / 6;
        double factor2 = factor / 12;
        circulo.setCenterY(circulo.getCenterY() + factor);
        if(circulo.getCenterX() < 796 ){
            circulo.setCenterX(circulo.getCenterX() - factor1);
        }
        else if(circulo.getCenterX() >1030){
            circulo.setCenterX(circulo.getCenterX() + factor1);
        }
        else if(circulo.getCenterX() < 915){
            circulo.setCenterX(circulo.getCenterX() - factor2);
        }else if(circulo.getCenterX() > 915){
            circulo.setCenterX(circulo.getCenterX() + factor2);
        }
    }

    private void updateProgress() {
        if (reproductor != null && reproductor.getStatus() == MediaPlayer.Status.PLAYING) {
            double currentTime = reproductor.getCurrentTime().toMillis();
            double total = reproductor.getTotalDuration().toMillis();
            BarraM.setProgress(currentTime / total);
        }
    }

    private void updateScoreProgress() {
        double score1Percentage = Math.min(puntaje / 50000.0, 1);
        BarraP1.setProgress(score1Percentage);

        double score2Percentage = Math.min(puntaje1 / 50000.0, 1);
        BarraP2.setProgress(score2Percentage);
    }

}