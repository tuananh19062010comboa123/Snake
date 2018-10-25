package base.scene;

import base.Background;
import base.GameObject;
import base.player.Player;
import base.snack.Snack;
import base.snack.SnackManager;
import base.wall.Wall;

public class SceneStage1 extends Scene {
    @Override
    public void destroy() {
        GameObject.clearAll();
    }

    @Override
    public void init() {
        GameObject.recycle(Background.class);
        this.player = GameObject.recycle(Player.class);
        this.snackManager = new SnackManager();
        this.snackManager.spawnSnack();
       /* Snack snack = GameObject.recycle(Snack.class);
        snack.position.set(190, 450);*/
        Wall wall = GameObject.recycle(Wall.class);
        wall.position.set(170,50);
    }
}
