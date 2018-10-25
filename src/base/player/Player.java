package base.player;

import base.GameObject;
import base.Settings;
import base.Vector2D;
import base.counter.FrameCounter;
import base.event.KeyEventPress;
import base.scene.SceneManager;
import base.scene.SceneStage1;
import base.scene.gameoverScen.GameoverScene;

import java.util.ArrayList;

public class Player extends GameObject {
    public ArrayList<SnakePart> parts;
    SnakeHead head;
    FrameCounter moveCounter;
    int direction;
    SnakePart newPart;

    public Player() {
        super();
        this.moveCounter = new FrameCounter(3);
        this.position.set(190, 290);
        this.initParts();
        this.direction = Settings.UP;
        /*this.newPart = GameObject.recycle(SnakePart.class);*/
    }

    private void initParts() {
        this.parts = new ArrayList<>();
        this.head = GameObject.recycle(SnakeHead.class);
        this.head.position.set(this.position);
        SnakePart tail = GameObject.recycle(SnakePart.class);
        tail.position.set(this.head.position.add(0, -Settings.WAY_SIZE));//? sao ko phai la +
        this.parts.add(this.head);
        this.parts.add(tail);
    }

   // FrameCounter newSceneCounter = new FrameCounter(500);
    @Override
    public void run() {
        this.setDirection();
        if(this.moveCounter.run()) {
            this.move();
            this.setPartsPosition();
            this.moveCounter.reset();
        }
       /* if(newSceneCounter.run()) {
            System.out.println("new scene coming");
            SceneManager.signNewScene(new SceneStage1());
        }*/
       this.updatePositionToSnackManager();
    }
    public void updatePositionToSnackManager(){
        SceneManager.currentScene.snackManager.updatePositionToSnackManager(this);
    }

    private void setDirection() {
        if(KeyEventPress.isUpPress && this.direction != Settings.DOWN
                && this.canTurnTo(Settings.UP)) {
            this.direction = Settings.UP;
        } else if(KeyEventPress.isDownPress && this.direction != Settings.UP
                && this.canTurnTo(Settings.DOWN)) {
            this.direction = Settings.DOWN;
        } else if(KeyEventPress.isLeftPress && this.direction != Settings.RIGHT
                && this.canTurnTo(Settings.LEFT)) {
            this.direction = Settings.LEFT;
        } else if(KeyEventPress.isRightPress && this.direction != Settings.LEFT
                && this.canTurnTo(Settings.RIGHT)) {
            this.direction = Settings.RIGHT;
        }
    }

    private boolean canTurnTo(int direction) {
        SnakePart head = this.head;
        SnakePart neck = this.parts.get(1);
        boolean inline = false;
        switch (direction) {
            case Settings.UP:
            case Settings.DOWN:
                //vertical : truc doc
                inline = head.position.x == neck.position.x;
                break;
            case Settings.LEFT:
            case Settings.RIGHT:
                //horiable
                inline = head.position.y == neck.position.y;
                break;
        }
        return !inline;
    }
    private void move() {// ham di chuyen
        int X = (int) this.position.x;
        int Y = (int)this.position.y;
        switch (this.direction) {
            case Settings.UP: {
                Y -= Settings.WAY_SIZE;
                /*this.position.addThis(0, -Settings.WAY_SIZE);*/
                if(Y<0){
                    Y += Settings.SCREEN_HEIGHT;
                }
                break;
            }
            case Settings.DOWN: {
                Y += Settings.WAY_SIZE;
               /* this.position.addThis(0, Settings.WAY_SIZE);*/
                if(Y > Settings.SCREEN_HEIGHT){
                    Y -= Settings.SCREEN_HEIGHT;
                }
                break;
            }
            case Settings.LEFT: {
                X -= Settings.WAY_SIZE;
                /*this.position.addThis(-Settings.WAY_SIZE, 0);*/
                if(X<0){
                    X += Settings.SCREEN_WIDHT;
                }
                break;
            }
            case Settings.RIGHT: {
                X += Settings.WAY_SIZE;
               /* this.position.addThis(Settings.WAY_SIZE, 0);*/
                if(X > Settings.SCREEN_WIDHT){
                    X -= Settings.SCREEN_WIDHT;
                }
                break;
            }
        }
        this.position.set(X,Y);
    }

    private void setPartsPosition() {// set vi tri cua thang moi an
        if(this.newPart != null) {
            parts.add(1, this.newPart);
            this.newPart.position.set(this.head.position);
            this.newPart.reset();//
            this.newPart = null;
        } else {
            for(int i = this.parts.size() - 1; i > 0; i--) {
                SnakePart part = this.parts.get(i);// thang nay la thagn cuoi
                SnakePart prevPart = this.parts.get(i - 1);// thang nay la thang gan cuoi
                part.position.set(prevPart.position);
            }
        }
        this.head.position.set(this.position);
    }

    public void growUp() {
        this.newPart = GameObject.recycle(SnakePart.class);
        this.newPart.destroy();//
    }

    public void hit(){
     this.destroy();
     //khi ma dam vao da thi dau laui lai va mat di mot part
     SnakePart partSecond = this.parts.get(1);
     this.head.position.set(partSecond.position);
     this.parts.remove(partSecond);
     partSecond.destroy();

     for(SnakePart part : this.parts){
         part.isDead = true;
     }
     SceneManager.signNewScene(new GameoverScene());// goi game over
    }
}
