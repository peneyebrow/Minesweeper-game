
import javax.swing.*;

public class levelB {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            System.exit(0);
        }
        catch (ClassNotFoundException e) {
            System.exit(0);
        }
        catch (InstantiationException e) {
            System.exit(0);
        }
        catch (IllegalAccessException e) {
            System.exit(0);
        }

        int x = 9;
        int y = 9;
        int mineNum = 10;
        int size = 30;

        Position techies = new Position();

        techies.setX(x);
        techies.setY(y);
        techies.setMineNum(mineNum);
        techies.setSize(size);
        techies.Start();
    }

}