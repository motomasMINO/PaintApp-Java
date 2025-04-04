import java.awt.*;

/**
 * ColorPoint
 */
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

  public int getStrokeSize() {
    return strokeSize;
  }
}