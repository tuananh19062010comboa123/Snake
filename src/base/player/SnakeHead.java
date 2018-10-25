package base.player;

import base.GameObject;
import base.renderer.BoxColliderRenderer;
import base.scene.SceneManager;
import base.snack.Snack;
import base.wall.Wall;
import game.GameCanvas;

import java.awt.*;

public class SnakeHead extends SnakePart {
    public SnakeHead() {
        super();
        this.renderer = new BoxColliderRenderer(Color.green);
    }

    @Override
    public void run() {
        this.checkIntersects();
    }

    private void checkIntersects() {
        //intersect with snake's parts
        Wall wall = GameObject.intersect(Wall.class,this);
        if(wall !=  null){
            SceneManager.currentScene.player.hit();
        }

        SnakePart part = GameObject.intersect(SnakePart.class,this);
        //part must not be the second part
        if(part != null && SceneManager.currentScene.player.parts.indexOf(part ) != 1) {
            SceneManager.currentScene.player.hit();
        }
        //intersect with food
        Snack snack = GameObject.intersect(Snack.class, this);
        if(snack != null) {
            snack.destroy();
            SceneManager.currentScene.player.growUp();
            SceneManager.currentScene.snackManager.spawnSnack();// sinh thuc an
        }

    }
}
