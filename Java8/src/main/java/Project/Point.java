package Project;

public class Point {
    //设置点类
    public int x;
    public int y;
    public Point(int x,int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object obj) {
        //重写比较方法1
        if (obj == this) {
            //检测
            return true;
        }
        if (!(obj instanceof Point)) {
            //检测传入对象是否为Point类型
            return false;
        }
        Point other = (Point) obj;//比较
        return other.x == x && other.y == y;
    }
}

