import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Canvas extends JPanel {
  private final static int STROKE_SIZE = 8;
  private final static int REDO_LIMIT = 10;
  
  // キャンバスに作成されたすべてのパスを保持
  private List<List<ColorPoint>> allPaths = new ArrayList<>();

  // 読み込んだ画像を保存
  private BufferedImage loadedImage = null;

  // 点と点の間に線を引くためのパス
  private List<ColorPoint> currentPath;

  // Redo用のスタック
  private Stack<List<ColorPoint>> redoStack = new Stack<>();

  // デフォルトのドット色は黒色
  private Color color = Color.BLACK;

  // 消しゴムモード
  private boolean eraserMode = false;

  // 塗りつぶしモード
  private boolean fillMode = false;

  // 開始点・終了点を保存
  private Point startPoint = null;
  private Point endPoint = null;

  // デフォルトの筆の太さ
  private int strokeSize = 8;

  public void setStrokeSize(int size) {
    this.strokeSize = size;
    repaint();
  }
  
  public Canvas(int targetWidth, int targetHeight){
    setPreferredSize(new Dimension(targetWidth, targetHeight));
    setOpaque(true);
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createLineBorder(Color.BLACK));

    allPaths = new ArrayList<>();

    MouseAdapter ma = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if(fillMode) {
           fillArea(e.getX(), e.getY(), color);
           return;
        }
          Color drawColor = eraserMode ? Color.WHITE : color;
          currentPath = new ArrayList<>();
          currentPath.add(new ColorPoint(e.getX(), e.getY(), drawColor, strokeSize));
          allPaths.add(currentPath);
          startPoint = e.getPoint();
          redoStack.clear();
          repaint();
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        if(fillMode || currentPath == null) return; // 塗りつぶしモード時は何もしない
          Color drawColor = eraserMode ? Color.WHITE : color;
          currentPath.add(new ColorPoint(e.getX(), e.getY(), drawColor, strokeSize));
          repaint();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
          if(currentPath != null) {
            allPaths.add(currentPath);
            currentPath = null;
          } else {
          endPoint = e.getPoint();
          startPoint = null;
          endPoint = null;
        }
        repaint();
      }
    };

    addMouseListener(ma);
    addMouseMotionListener(ma);
  }

  public void loadImage(BufferedImage image) {
    this.loadedImage = image;
    repaint();
  }
  
  public void setColor(Color color){
    this.color = color;
  }

  public void setEraserMode(boolean mode) {
    this.eraserMode = mode;
  }

  public void setFillMode(boolean mode) {
    this.fillMode = mode;
  }

  public void undo() {
    if (!allPaths.isEmpty()) {
        redoStack.push(new ArrayList<>(allPaths.remove(allPaths.size() - 1))); // Deep Copyで保存
        if (redoStack.size() > REDO_LIMIT) {
            redoStack.remove(0);
        }
        currentPath = null;
        repaint();
    }
  }

  public void redo() {
    if (redoStack.isEmpty()) return; // 空なら処理しない
        allPaths.add(redoStack.pop());
        repaint();
  }

  public void fillArea(int x, int y, Color fillColor) {
    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    paint(g2d); // 現在のキャンバスを画像として取得
    g2d.dispose();

    int targetColor = image.getRGB(x, y);
    int newColor = fillColor.getRGB();

    if(targetColor == newColor) return; // すでに塗りつぶし色なら何もしない

    Queue<Point> queue = new LinkedList<>();
    queue.add(new Point(x, y));

    boolean[][] visited = new boolean[getWidth()][getHeight()];
    List<ColorPoint> fillPath = new ArrayList<>();

    while(!queue.isEmpty()) {
      Point p = queue.poll();
      int px = p.x, py = p.y;

      if(px < 0 || px >= image.getWidth() || py < 0 || py >= getHeight()) continue;
      if(visited[px][py]) continue;
      if(image.getRGB(px, py) != targetColor) continue;

      visited[px][py] = true;
      fillPath.add(new ColorPoint(px, py, fillColor, strokeSize));

      image.setRGB(px, py, newColor);

      queue.add(new Point(px + 1, py));
      queue.add(new Point(px - 1, py));
      queue.add(new Point(px, py + 1));
      queue.add(new Point(px, py - 1));
    }

    if(!fillPath.isEmpty()) {
      allPaths.add(fillPath);
      redoStack.clear();
      repaint();
    }

    Graphics g = getGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
  }


  public void resetCanvas(){
    allPaths.clear();
    redoStack.clear();
    currentPath = null;
    loadedImage = null;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if(loadedImage != null) {
       g.drawImage(loadedImage, 0, 0, getWidth(), getHeight(), this);
    }

    // すべてのパスを再描画
    for (List<ColorPoint> path : allPaths) {
      drawPath(g2d, path);
    }

    // 現在描画中のパスも描画
    if (currentPath != null) {
      drawPath(g2d, currentPath);
    }    
  }

  private void drawPath(Graphics2D g2d, List<ColorPoint> path){
    ColorPoint from = null;
    for (ColorPoint point : path) {
      g2d.setColor(point.getColor());
      g2d.setStroke(new BasicStroke(point.getStrokeSize()));
      if (path.size() == 1) {
        g2d.fillRect(point.getX(), point.getY(), point.getStrokeSize(), point.getStrokeSize());
      }
      if (from != null) {
        g2d.drawLine(from.getX(), from.getY(), point.getX(), point.getY());
      }
      from = point;
    }
  }
}
