package org.Modules;


import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;


public class Sprite extends Transition {
    private final ImageView image;
    private final int totalFrames;
    private final int columns;
    private final int frameWidth;
    private final int frameHeight;

    public Sprite(ImageView image, int totalFrames,
                  int columns, int frameWidth, int frameHeight) {
        this.image = image;
        this.totalFrames = totalFrames;
        this.columns = columns;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.setCycleDuration(Duration.millis(48));

    }

    @Override
    protected void interpolate(double v) {
        int index = Math.min((int)Math.floor(v * totalFrames), totalFrames - 1);
        int row = index / columns;
        int col = index % columns;


        this.image.setViewport(new Rectangle2D(col * frameHeight, row * frameHeight, frameWidth, frameHeight));

    }
    //Tanto este metodo como el anterior se ponen 32 porque sabemos con certeza el tamanio del frame del boton, deberiamos
    //de ponerlo mejor
    public void resetAnimation(){
        this.image.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
    }

    public static void setStylesButton (Button button, String path) {
        Image spriteSheet = new Image(Objects.requireNonNull(Sprite.class.getResourceAsStream(path)));
        ImageView image = new ImageView(spriteSheet);
        image.setViewport(new Rectangle2D(0, 0, 32, 32));
        image.setFitWidth(32);
        image.setFitHeight(32);
        button.setGraphic(image);
    }
}