package Project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
按钮总体布局就是左边中心处为按钮，右边为地图，左上为自定义地图大小输入框，按钮共分为5个：分别为生成迷宫，自动寻路，遍历迷宫，单步寻路，删除地图。
整体运行逻辑为：开始时输入方形地图的边长，然后除了生成地图按钮外，其他按钮均处于不可操作状态，当生成地图后，生成地图按钮处于不可操作状态，其他按钮
均处于可操作状态，只有删除当前地图后才会回到初始状态。
 */
public class Mouse extends Application {
    //显示台输出类
    public static Button button1 = new Button("生成迷宫");
    public static Button button2 = new Button("自动寻路");
    public static Button button3 = new Button("遍历迷宫");
    public static Button button4 = new Button("单步寻路");
    public static Button button5 = new Button("重置地图");
    public static Button button6 = new Button("删除地图");
    TextField textField = new TextField();//地图大小输入框
    TextField textFieldX0 = new TextField();//起点横坐标设置
    TextField textFieldY0 = new TextField();//起点纵坐标设置
    TextField textFieldX1 = new TextField();//终点横坐标设置
    TextField textFieldY1 = new TextField();//终点纵坐标设置
    Maze maze;//表示迷宫
    public static MazeCanvas canvas;
    public static BorderPane pane = new BorderPane();
    boolean isStep = false;//判断对象是否已经存在
    StepFinding stepFinding;//单步寻路
    Label label1 = new Label("\t\t请输入方形地图长的一半:\n(默认为10，输入不得低于10，不得高于150)");
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage stage)  {
        //应用布局
        pane.setRight(getVBox());//将子结点移动到右边
        button1.setOnAction(event ->{
            try{
                if(textField.getText().isEmpty()) {
                    //输入框中不存在输入数据时
                    button1.setDisable(true);
                    button2.setDisable(false);
                    button3.setDisable(false);
                    button4.setDisable(false);
                    button6.setDisable(false);
                    textField.setDisable(true);
                    pane.setLeft(setMaze(10));//文本框中无输入默认长度为10
                }
                else {
                    //输入框中存在输入数据时
                    int num = Integer.parseInt(textField.getText());
                    if (num < 10 || num> 150) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("输入的数字超出限制");
                        alert.showAndWait();
                    }
                    else {
                        button1.setDisable(true);
                        button2.setDisable(false);
                        button3.setDisable(false);
                        button4.setDisable(false);
                        button6.setDisable(false);//开启功能
                        textField.setDisable(true);//关闭输入功能
                        pane.setLeft(setMaze(num));//若有输入则以输入为准
                    }
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("请输入合法的数字！");
                alert.showAndWait();
            }
        });
        button2.setOnAction(event->{
            //起点默认左上角，终点默认右下角(自动寻路）
            int[] enter = isEnter();
            if(enter!=null) {
                //检测输入是否有误，有误不执行功能操作
                PathFinding find = new PathFinding(maze.map, enter[0], enter[1], enter[2], enter[3]);
                button2.setDisable(true);
                button4.setDisable(true);
                button5.setDisable(false);//将清除效果按钮开放，将其他改变地图状况按钮关闭
                textFieldX0.setDisable(true);
                textFieldX1.setDisable(true);
                textFieldY1.setDisable(true);
                textFieldY0.setDisable(true);
                maze.map = find.setAction();//使用自动寻路主方法
            }
        });
        button3.setOnAction(event->{
            //遍历迷宫按钮
            int[] enter = isEnter();//检测起点终点自定义输入框
            maze.map = reSetMap();
            canvas.PaintMap(maze.map,0,0,0,0);//重置地图，传递无效坐标
            if(enter!=null) {
                //检测输入是否有误，有误不执行操作
                OverPath overPath = new OverPath(maze.map, enter[0], enter[1], enter[2], enter[3]);
                maze.map = overPath.setAction();
                canvas.PaintMap(maze.map, enter[0], enter[1], enter[2], enter[3]);
                button2.setDisable(true);
                button3.setDisable(true);
                button4.setDisable(true);
                textFieldX0.setDisable(true);
                textFieldX1.setDisable(true);
                textFieldY1.setDisable(true);
                textFieldY0.setDisable(true);
            }
        });
        button4.setOnAction(event->{
            //单步寻路按钮
            int[] enter = isEnter();
            if(enter!=null) {
                if (!isStep) {
                    stepFinding = new StepFinding(maze.map, enter[0], enter[1], enter[2], enter[3]);
                    isStep = true;
                }
                button2.setDisable(true);
                button3.setDisable(true);
                button5.setDisable(false);
                textFieldX0.setDisable(true);
                textFieldX1.setDisable(true);
                textFieldY1.setDisable(true);
                textFieldY0.setDisable(true);
                maze.map = stepFinding.setAction();
                if (maze.map[enter[2]][enter[3]] == 4)
                    button4.setDisable(true);
                canvas.PaintMap(maze.map,enter[0],enter[1],enter[2],enter[3]);
            }
        });
        button5.setOnAction(event->{
            //清除地图效果，恢复地图原始状态（重置地图）
            button2.setDisable(false);
            button3.setDisable(false);
            button4.setDisable(false);
            button5.setDisable(true);//开放改变地图状况按钮，关闭重置地图按钮
            textFieldX0.setDisable(false);
            textFieldX1.setDisable(false);
            textFieldY1.setDisable(false);
            textFieldY0.setDisable(false);
            stepFinding = null;
            isStep = false;
            maze.map = reSetMap();
            canvas.PaintMap(maze.map,0,0,0,0);//重置地图，传递无效坐标
        });
        button6.setOnAction(event->{
            //回到只有第一个按钮可按的初始状态，重新生成地图（删除地图）
            pane.setLeft(ClearMap(maze.map.length));
            button1.setDisable(false);
            button2.setDisable(true);
            button3.setDisable(true);
            button4.setDisable(true);
            button5.setDisable(true);
            button6.setDisable(true);
            textField.setDisable(false);
            textFieldX0.setDisable(false);
            textFieldX1.setDisable(false);
            textFieldY1.setDisable(false);
            textFieldY0.setDisable(false);//恢复输入框的输入状态
        });
        Scene scene1 = new Scene(pane,1000,700);
        stage.setTitle("老鼠迷宫");//设置应用标题
        stage.setScene(scene1);
        stage.show();//展示
    }
    private VBox getVBox(){
        VBox vBox = new VBox(10);
        HBox hBox1 = new HBox(10);
        HBox hBox2 = new HBox(10);
        hBox1.getChildren().addAll(textFieldX0, textFieldY0);//起点设置文本框架
        hBox2.getChildren().addAll(textFieldX1, textFieldY1);//终点设置文本框架
        //设置HBox的布局方式为右对齐
        hBox1.setAlignment(Pos.CENTER_RIGHT);
        hBox2.setAlignment(Pos.CENTER_RIGHT);
        vBox.setPadding(new Insets(5,5,5,5));//设置子节点之间的间距
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
        button5.setDisable(true);
        button6.setDisable(true);//让除了”生成地图“按钮除外的所有按钮变为不可操作状态，防止出现bug。
        textFieldX0.setMaxWidth(50);
        textFieldY0.setMaxWidth(50);
        textFieldX1.setMaxWidth(50);
        textFieldY1.setMaxWidth(50);
        textField.setMaxWidth(100);//设置输入框的长度
        vBox.getChildren().add(label1);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(button1);
        vBox.getChildren().add(new Label("\t\t输入起点坐标\n（不输入或输入不完整默认左上角）："));
        vBox.getChildren().add(hBox1);
        vBox.getChildren().add(new Label("\t\t输入终点坐标\n（不输入或输入不完整默认右下角）："));
        vBox.getChildren().add(hBox2);
        vBox.getChildren().add(button2);
        vBox.getChildren().add(button3);
        vBox.getChildren().add(button4);
        vBox.getChildren().add(button5);
        vBox.getChildren().add(button6);//设置竖直水平上的子节点，安排布局
        vBox.setAlignment(Pos.CENTER_RIGHT);//将子节点置于右边中心处
        return vBox;
    }
    public VBox setMaze(int num){
        //设置地图长度和改变按钮状态模块。
        VBox vBox = new VBox(5);
        maze = new Maze(num,num);
        maze.inti();
        canvas = new MazeCanvas(maze.map.length,(double)650/maze.map.length);
        canvas.PaintMap(maze.map,0,0,0,0);//创建基础画布，并将地图画在画布上
        Label label = new Label("地图长度为：（不包含外围墙）" + (num*2-1));
        Label label2 = new Label("地图内总格数：（不包含外围墙）" + (num*2-1)*(num*2-1));//提示文本创建
        vBox.getChildren().add(canvas);
        vBox.getChildren().add(label);
        vBox.getChildren().add(label2);
        vBox.setAlignment(Pos.CENTER_LEFT);//将子节点置于右边中心处
        vBox.setTranslateX(vBox.getTranslateX()+20);//将子节点向右移动20像素
        return vBox;
    }
    public VBox ClearMap(int num){
        //删除地图方法，重置所有相关功能对象的状态，删除围墙内容
        VBox vBox = new VBox(0);
        maze = new Maze(num,num);
        stepFinding = null;
        isStep = false;//清除单步寻路的进度
        maze.Clear();//清除地图内的墙
        canvas = new MazeCanvas(maze.map.length, (double)650/maze.map.length);
        canvas.PaintMap(maze.map,0,0,0,0);
        canvas.setTranslateX(canvas.getTranslateX() + 20);//将子节点向右移动20像素
        vBox.getChildren().add(canvas);
        return vBox;
    }
    public int[][] reSetMap(){
        //重置地图方法
        for(int i = 0;i < maze.map.length;i++)
            for(int j = 0;j < maze.map.length;j++)
                if(maze.map[i][j] == 4 || maze.map[i][j] == 2 || maze.map[i][j] == 3)
                    maze.map[i][j] = 1;
        return maze.map;
    }
    public int[] isEnter() {
        //判断起点坐标和终点坐标输入值
        int[] enter = new int[]{1,1,maze.map.length-2,maze.map.length-2};
        try {
            //检测两个框是否全部输入
            if (!textFieldX0.getText().isEmpty() && !textFieldY0.getText().isEmpty()) {
                    enter[0] = Integer.parseInt(textFieldX0.getText());
                    enter[1] = Integer.parseInt(textFieldY0.getText());
            }
            //检测输入是否超出限制
            if(enter[0] < 1 || enter[1] < 1 || enter[0] > maze.map.length-2 || enter[1] > maze.map.length-2){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("起点输入超出地图限制！请重新输入。");
                alert.showAndWait();
                return null;
            }
            else if(maze.map[enter[0]][enter[1]] == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("起点坐标此处为墙，请重新输入。");
                alert.showAndWait();
                return null;
            }
            if (!textFieldX1.getText().isEmpty() && !textFieldY1.getText().isEmpty()){
                enter[2] = Integer.parseInt(textFieldX1.getText());
                enter[3] = Integer.parseInt(textFieldY1.getText());
            }
            if(enter[2] < 1 || enter[2] < 1 || enter[3] > maze.map.length-2 || enter[3] > maze.map.length-2){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("终点输入超出地图限制！请重新输入。");
                alert.showAndWait();
                return null;
            }
            else if(maze.map[enter[2]][enter[3]] == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("终点此处为墙，请重新输入。");
                alert.showAndWait();
                return null;
            }
            else if(enter[0] == enter[2] && enter[1] == enter[3]){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("终点和起点设置为了同一点，请重新输入。");
                alert.showAndWait();
                return null;
            }
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("存在非法输入！请仔细检查输入格式。");
            alert.showAndWait();
        }
        return enter;
    }
}