package Project;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeCanvas extends Canvas {
    //地图绘制类
    private final double cellSize;//单元格的大小
    MazeCanvas(int side,double cellSize){
        super(side*cellSize,side*cellSize);
        this.cellSize = cellSize;
    }
    public void PaintMap(int[][] map,int x1,int y1,int x,int y){
        if(x1!=0) {
            //检测是否接收了无效坐标
            map[x1][y1] = 2;
            map[x][y] = 3;//起点和终点绘制
        }
        GraphicsContext gc = getGraphicsContext2D();
        for(int i = 0;i < map.length;i++)
            for(int j = 0;j < map[0].length;j++){
                if(map[i][j]==0){
                    //设置墙颜色
                    gc.setFill(Color.BLACK);
                }
                else if(map[i][j]==1){
                    //设置通路颜色
                    gc.setFill(Color.WHITE);
                }
                else if(map[i][j]==2){
                    //设置起点颜色
                    gc.setFill(Color.BLUE);
                }
                else if(map[i][j]==3){
                    //设置终点颜色
                    gc.setFill(Color.RED);
                }
                else if(map[i][j]==4){
                    //设置遍历格子颜色
                    gc.setFill(Color.PURPLE);
                }
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
    }
}
