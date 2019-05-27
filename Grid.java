import javax.swing.*;
import java.awt.*;

public class Grid extends JButton {
    public boolean mined;
    public boolean opened;
    public boolean flagged;
    public boolean bombed;
    public boolean gameOver;
    public boolean mouseEnter;
    public int minesAround;
    public int gridX;
    public int gridY;
    public int size;
    public int bound;
    public int checkFlag;


    public Grid(int gridX, int gridY, int size, int bound) {
        super();
        this.bound=bound ;
        this.size = size;
        setBounds((gridX - 1) * (this.size +bound)+bound, (gridY - 1) * (this.size +bound)+bound+60, this.size, this.size);
        reset();
        this.gridX = gridX;
        this.gridY = gridY;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!opened && !flagged){
            if (mouseEnter){
            g.drawImage(Position.getImage("mouse"),0,0,null);
        }
        else {
            g.drawImage(Position.getImage("closed"),0,0,null);
        }
        }

        if(flagged){
            g.drawImage(Position.getImage("flagged"),0,0,null);
            if(!mined && gameOver){
                g.drawImage(Position.getImage("nonflagged"),0,0,null);
            }
        }
        if(mined && opened){
            if(bombed){
                g.drawImage(Position.getImage("mined"),0,0,null);
            }
            else{
                g.drawImage(Position.getImage("mine"),0,0,null);
            }
            if(flagged){
                g.drawImage(Position.getImage("nomine"),0,0,null);
            }
        }
        if (opened) {
            if(!mined){
                g.drawImage(Position.getImage(Integer.toString(minesAround)),0,0,null);
            }
        }
    }

    public void reset() {
        mined = false;
        opened = false;
        flagged = false;
        minesAround = 0;
        bombed=false;
        gameOver=false;
        checkFlag=0;
    }

}
class background extends JPanel {
    public void paintComponent(Graphics back) {
        back.setColor(Color.lightGray);
        back.fillRect(0,0,getWidth(),getHeight());
        ImageIcon i = new ImageIcon("img/flag.png");
        i.paintIcon(this, back, getWidth() / 9, 15);;
        ImageIcon timer = new ImageIcon("img/timer.gif");
        timer.paintIcon(this, back, getWidth()*3/4-40,10);
    }

}