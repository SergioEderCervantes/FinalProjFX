package org.Modules;


import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;




public class Sprite extends Transition {
    private final ImageView image;
    private final int totalFrames;
    private final int columns;
    private final int frameWidth;
    private final int frameHeight;

    public Sprite(ImageView image, int totalFrames,
                  int columns, int frameWidth, int frameHeight, double totalDuration) {
        this.image = image;
        this.totalFrames = totalFrames;
        this.columns = columns;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;


        this.setCycleDuration(Duration.millis(totalDuration));

    }

    @Override
    protected void interpolate(double v) {
        int index = (int)Math.floor(v * totalFrames);
        if (index == totalFrames) index = 0;
        int row = index / columns;
        int col = index % columns;


        this.image.setViewport(new Rectangle2D(col * frameWidth, row * frameHeight, frameWidth, frameHeight));

    }
    //Tanto este metodo como el anterior se ponen 32 porque sabemos con certeza el tamanio del frame del boton, deberiamos
    //de ponerlo mejor
    public void resetAnimation(){
        this.image.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
    }


}