import javax.swing.*;

public class AppMenuBar {
    private final FileManager fileManager;

    public AppMenuBar(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("ファイル");

        JMenuItem openItem = new JMenuItem("開く");
        JMenuItem saveAsItem = new JMenuItem("名前を付けて保存");
        JMenuItem saveItem = new JMenuItem("上書き保存");

        // ファイル操作のイベント処理
        openItem.addActionListener(e -> fileManager.openFile());
        saveAsItem.addActionListener(e -> fileManager.saveAsFile());
        saveItem.addActionListener(e -> fileManager.saveFile());

        fileMenu.add(openItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        return menuBar;
    }
}
