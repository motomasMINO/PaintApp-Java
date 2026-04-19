import java.awt.*;

// 色と座標を保持するクラス
public class ColorPoint {
  private Color color;
  private int x, y;
  private int strokeSize;
  
  public ColorPoint(int x, int y, Color color, int strokeSize){
    this.x = x;
    this.y = y;
    this.color = color;
    this.strokeSize = strokeSize;
  }

  public Color getColor() {
    return color;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  // 線の太さを取得するメソッド
  public int getStrokeSize() {
    return strokeSize;
  }
}