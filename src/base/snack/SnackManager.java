package base.snack;

import base.GameObject;
import base.Settings;
import base.Vector2D;
import base.player.Player;
import base.player.SnakePart;
import tklibs.Mathx;

import java.util.ArrayList;
import java.util.Random;

public class SnackManager {
    boolean [][] map;
    Vector2D currentSnakePosition;
    Random random;
    ArrayList<String> lastPlayerPositionUpdate ;

    public SnackManager(){
        random = new Random();
        this.lastPlayerPositionUpdate = new ArrayList<>();
        this.map = new boolean[Settings.COL_COUNT][Settings.ROW_COUNT];
        for(int j =0 ;j< Settings.ROW_COUNT;j++){
            for (int i =0;i<Settings.COL_COUNT;i++){
                map[i][j] = true;
            }
        }
        this.currentSnakePosition = new Vector2D(-1,-1);
    }
    public void spawnSnack(){
        //convert map >> arrayList<point> validate
        //0 >> list.size
        String position = this.randomPosition();
        Snack newSnack = GameObject.recycle(Snack.class);
        this.mappositionWithWaysize(newSnack,position);

    }

    private void mappositionWithWaysize(Snack newSnack, String position) {
        // cho thang nay ra giua
        newSnack.position.set(position).scaleThis(Settings.WAY_SIZE).addThis(Settings.WAY_SIZE/2,Settings.WAY_SIZE/2);
    }

    private String randomPosition() {
        ArrayList<String> points = new ArrayList<>();
        Vector2D temp = new Vector2D();
        for(int j =0 ;j< Settings.ROW_COUNT;j++){
            for (int i =0;i<Settings.COL_COUNT;i++){
                if(map[i][j]){
                    points.add(temp.set(i,j).toString());
                }

            }
        }
        int index = this.random.nextInt(points.size());
       return points.get(index);
    }


    public void updatePositionToSnackManager(Player player){
        //clear map last Player's update;
        //update curren player position
        for(String position : this.lastPlayerPositionUpdate){
            this.setToMap(position,true);
        }

        for (SnakePart part : player.parts){
            String position = part.position.add(Settings.WAY_SIZE/2,Settings.WAY_SIZE/2).scale(1.0f / Settings.WAY_SIZE).toString();
            this.setToMap(position,false);
            this.lastPlayerPositionUpdate.add(position);
        }

    }
    /**
     *
     * @param position ~ (x : 1,y:2)
     * @param value ~ true/false
     */
    // lay vi tri dau tien cua thang nay trong map
    public void setToMap(String position,boolean value){
        Vector2D temp = new Vector2D();// cho no di xuyen tuong
        temp.set(position);
        int i = (int)temp.x;
        int j = (int)temp.y;
        i = Mathx.clamp(i,0,Settings.COL_COUNT - 1);
        j = Mathx.clamp(j,0,Settings.ROW_COUNT - 1);
        map[i][j] = value;
    }
}
