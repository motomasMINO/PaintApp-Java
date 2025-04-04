import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileManager {
    private File currentFile = null; // 上書き保存用ファイル
    private final Component parent;
    private final Canvas canvas;

    public FileManager(Component parent, Canvas canvas) {
        this.parent = parent;
        this.canvas = canvas;
    }

    // 画像を開く
    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("開く");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("画像ファイル (PNG, JPG)", "png", "jpg", "jpeg"));

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                canvas.loadImage(image);
                currentFile = file; // 上書き保存用にセット
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "ファイルを開けませんでした。", "エラー", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 名前を付けて保存
    public void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("名前を付けて保存");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG 画像", "png"));

        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".png");
            saveImage(file);
            currentFile = file; // 次回から上書き保存できるように
        }
    }

    // 上書き保存
    public void saveFile() {
        if (currentFile != null) {
            saveImage(currentFile);
        } else {
            saveAsFile(); // まだ保存していなければ「名前を付けて保存」
        }
    }

    // キャンバスの内容を画像として保存
    private void saveImage(File file) {
        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        canvas.paint(g2d);
        g2d.dispose();

        try {
            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(parent, "保存しました！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "保存に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }
}
