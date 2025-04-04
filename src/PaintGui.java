import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PaintGui
 */
public class PaintGui extends JFrame {
  public PaintGui(){
    super("ペイントアプリ");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(1500, 1000));
    pack();
    setLocationRelativeTo(null);

    addGuiComponents();
  }

private void addGuiComponents() {
    // JPanel 構成
    JPanel canvasPanel = new JPanel();
    SpringLayout springLayout = new SpringLayout();
    canvasPanel.setLayout(springLayout);

    // キャンバス
    Canvas canvas = new Canvas(1500, 950);
    canvasPanel.add(canvas);
    springLayout.putConstraint(SpringLayout.NORTH, canvas, 50, SpringLayout.NORTH, canvasPanel);

    // FileManager & MenuBarの初期化
    FileManager fileManager = new FileManager(this, canvas);
    AppMenuBar menuBar = new AppMenuBar(fileManager);

    // メニューバーの設定
    setJMenuBar(menuBar.createMenuBar());
    
    // カラー選択
    JButton chooseColorButton = new JButton("カラー選択");
    chooseColorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color c = JColorChooser.showDialog(null, "色を選択", Color.BLACK);
        chooseColorButton.setBackground(c);
        canvas.setColor(c);
      }
    });
    canvasPanel.add(chooseColorButton);
    springLayout.putConstraint(SpringLayout.NORTH, chooseColorButton, 10, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, chooseColorButton, 25, SpringLayout.WEST, canvasPanel);

    // リセットボタン
    JButton resetButton = new JButton("リセット");
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        canvas.resetCanvas();
      }
    });
    canvasPanel.add(resetButton);
    springLayout.putConstraint(SpringLayout.NORTH, resetButton, 10, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, resetButton, 150, SpringLayout.WEST, canvasPanel);

    // 消しゴムボタン
    JToggleButton eraserButton = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("./eraser.png")));
    canvasPanel.add(eraserButton);
    springLayout.putConstraint(SpringLayout.NORTH, eraserButton, 5, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, eraserButton, 335, SpringLayout.WEST, canvasPanel);

    // 通常描画ボタン
    JToggleButton freeHandButton = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("./pen.png")));
    canvasPanel.add(freeHandButton);
    springLayout.putConstraint(SpringLayout.NORTH, freeHandButton, 5, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, freeHandButton, 260, SpringLayout.WEST, canvasPanel);

    // 塗りつぶしボタン
    JToggleButton fillButton = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("./fill.png")));
    canvasPanel.add(fillButton);
    springLayout.putConstraint(SpringLayout.NORTH, fillButton, 5, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, fillButton, 410, SpringLayout.WEST, canvasPanel);

    fillButton.addActionListener(e -> {
    canvas.setFillMode(fillButton.isSelected());
    });

    // Undoボタン
    JButton undoButton = new JButton("↩ Undo");
    canvasPanel.add(undoButton);
    springLayout.putConstraint(SpringLayout.NORTH, undoButton, 10, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, undoButton, 505, SpringLayout.WEST, canvasPanel);

    undoButton.addActionListener(e -> canvas.undo());

    // Redoボタン
    JButton redoButton = new JButton("↪ Redo");
    canvasPanel.add(redoButton);
    springLayout.putConstraint(SpringLayout.NORTH, redoButton, 10, SpringLayout.NORTH, canvasPanel);
    springLayout.putConstraint(SpringLayout.WEST, redoButton, 585, SpringLayout.WEST, canvasPanel);

    redoButton.addActionListener(e -> canvas.redo());

    freeHandButton.addActionListener(e -> {
    canvas.setEraserMode(false);
    eraserButton.setSelected(false);
   });

   // 筆の太さ変更スライダー
   JSlider strokeSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 8);
   strokeSlider.setMajorTickSpacing(10);
   strokeSlider.setMinorTickSpacing(1);
   strokeSlider.setPaintTicks(true);
   strokeSlider.setPaintLabels(true);

   strokeSlider.addChangeListener(e -> {
       int value = strokeSlider.getValue();
       canvas.setStrokeSize(value);
   });

   canvasPanel.add(strokeSlider);
   springLayout.putConstraint(SpringLayout.NORTH, strokeSlider, 5, SpringLayout.NORTH, canvasPanel);
   springLayout.putConstraint(SpringLayout.WEST, strokeSlider, 700, SpringLayout.WEST, canvasPanel);

    // 消しゴムの切り替え
    eraserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(eraserButton.isSelected()) {
          canvas.setEraserMode(true);
        } else {
          canvas.setEraserMode(false);
        }
      }
    });

    // モードのグループ化
    ButtonGroup modeGroup = new ButtonGroup();
    modeGroup.add(eraserButton);
    modeGroup.add(freeHandButton);

    this.getContentPane().add(canvasPanel);
  }
}