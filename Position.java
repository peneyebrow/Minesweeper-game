import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Position {
    private Grid[][] area;
    private JFrame window;
    private MouseListener mouseListener;
    private int x;
    private int y;
    private int size;
    private int mineNum;
    private int flagNum;
    private int flagRem;
    private int openNum;
    private int bound;
    private int rand;
    private int rand1=1;
    private int randCount=0;
    private int ch_flagNum;
    private Timer timer;

    background back = new background();
    JLabel flagRemNum = new JLabel();
    JButton reset = new JButton();


    public Position() {
        x = 9;
        y = 9;
        size = 30;
        mineNum = 10;
        bound = 5;

        mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                ImageIcon icon= new ImageIcon("img/happy.png");
                reset.setIcon(icon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                processClick(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Grid area = (Grid) e.getSource();
                if(!area.gameOver) {
                    area.mouseEnter = true;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Grid area = (Grid) e.getSource();
                area.mouseEnter=false;
            }

            public void processClick(MouseEvent e) {
                Grid area = (Grid) e.getSource();

                ImageIcon icon1 = new ImageIcon("img/smiley.png");
                reset.setIcon(icon1);

                if (!area.opened && !area.gameOver) {
                    randCount++;
                    if (SwingUtilities.isLeftMouseButton(e) && !area.flagged){
                        if(!area.mined){
                            if(randCount%rand1==0){
                                rand=(int)(33*Math.random());
                                rand1=(int)(3*Math.random()+2);
                                File step = new File("music\\step"+rand+".wav");
                                playSound(step);
                            }
                        }
                        openArea(area);
                    }
                    else if (SwingUtilities.isRightMouseButton(e)){
                        if(randCount%rand1==0){
                            rand=(int)(33*Math.random());
                            rand1=(int)(3*Math.random()+2);
                            File step = new File("music\\step"+rand+".wav");
                            playSound(step);
                        }
                        flagArea(area);
                        flagRem=mineNum-flagNum;
                        flagRemNum.setText(":"+flagRem);
                    }
                }
                checkWin();
            }
        };
    }

    public int getBound() {
        return bound;
    }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSize(int size) { this.size = size; }
    public void setMineNum(int mineNum) { this.mineNum = mineNum; }



    public void Start() {
        createWindow();
        newGame();
    }
    public static Image getImage (String name){
        ImageIcon icon = new ImageIcon("img/"+ name+".png");
        return icon.getImage();
    }

    private void createWindow() {
        window = new JFrame("Techieeees");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setIconImage(getImage("icon"));

        ImageIcon icon1 = new ImageIcon("img/smiley.png");
        reset.setIcon(icon1);
        reset.setBounds(x*(size+getBound())/2+getBound()-25, 10,50,50);
        reset.setContentAreaFilled(false);
        reset.setFocusPainted(false);
        back.add(reset);
        reset.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {            }
            @Override
            public void mousePressed(MouseEvent e) {
                ImageIcon icon= new ImageIcon("img/happy.png");
                reset.setIcon(icon);
            }
            @Override
            public void mouseReleased(MouseEvent e) { newGame();}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        window.setContentPane(back);


        Container button = window.getContentPane();
        button.setLayout(null);
        timer = new Timer();
        window.setContentPane(back);
        button.add(timer);
        timer.setBounds((x*(size+getBound())+getBound())*3/4-5, 0, 50, 60);
        timer.setFont(new Font("Serif", Font.BOLD, 20));

        flagRem = mineNum-flagNum;
        flagRemNum.setFont(new Font("Serif", Font.BOLD, 20));
        flagRemNum.setBounds((x*size+getBound()+getBound())/7+20,0,50,60);
        flagRemNum.setHorizontalAlignment(SwingConstants.LEFT);
        flagRemNum.setForeground(Color.BLACK);
        flagRemNum.setText(":"+flagRem);


        area = new Grid[x +1][y +1];

        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                area[i][j] = new Grid(i, j, size, getBound());
                button.add(area[i][j]);
                area[i][j].addMouseListener(mouseListener);
            }
        }

        button.setPreferredSize(new Dimension(x * (size +getBound())+getBound(),
                y * (size +getBound())+getBound()+60));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        window.add(flagRemNum);
    }


    private void newGame() {
        ImageIcon icon = new ImageIcon("img/smiley.png");
        reset.setIcon(icon);

        window.setContentPane(back);
        flagNum = 0;
        openNum = 0;
        ch_flagNum =0;
        timer.reset();

        rand=(int)(5*Math.random());
        File begin = new File("music\\begin"+rand+".wav");
        playSound(begin);
        resetZones();
        createMine(mineNum);
        createNums();
    }

    private void resetZones() {
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                area[i][j].reset();
                area[i][j].repaint();
            }
        }
    }



    private void createMine(int i) {
        int rX, rY;
        while (i > 0) {
            rX = (int)((x -1)*Math.random()+1);
            rY = (int)((y -1)*Math.random()+1);
            if (!area[rX][rY].mined) {
                area[rX][rY].mined = true;
//                System.out.println(rX+" "+ rY );
                i--;
            }
        }
    }

    private void createNums() {
        int mines;
        for (int x = 1; x <= this.x; x++) {
            for (int y = 1; y <= this.y; y++) {
                if (!area[x][y].mined) {
                    mines = 0;
                    ArrayList<Grid> neighbours = searchMinesAround(area[x][y]);

                    for (int i=0; i< neighbours.size(); i++) {
                        if (neighbours.get(i).mined) {
                            mines++;
                        }
                    }
                    area[x][y].minesAround = mines;
                }
            }
        }
    }

    private void flagArea(Grid area) {
        if (area.flagged) {
            flagNum--;
            area.flagged = !area.flagged;
            area.repaint();
            checkFlag(area,-1);
        }
        else {
            flagNum++;
            area.flagged = !area.flagged;
            area.repaint();
            checkFlag(area,1);
        }


    }
    private void checkFlag(Grid _zone, int n){
        if( _zone.mined){
            ch_flagNum +=n;
        }
        if(ch_flagNum ==mineNum && flagNum ==mineNum){
            gameOver(true);
        }
    }


    private void openArea(Grid area) {
        if(openNum==0){
            timer.start();
        }
        if (area.mined) {
            if(openNum ==0) {
                area.mined = false;
                createMine(1);
                createNums();
            }
            else {
                area.bombed=true;
                gameOver(false);
                return;
            }
        }
        if (!(area.flagged && area.opened)) {
            openNum++;
            area.opened = true;
            area.repaint();
            if (area.minesAround == 0) {
                clear(area);
            }
        }

    }


    private void clear(Grid area) {
        ArrayList<Grid> neighbours = searchMinesAround(area);

        for(int i=0; i<neighbours.size(); i++) {
            if (!neighbours.get(i).opened) {
                openArea(neighbours.get(i));
            }
        }
    }


    private ArrayList<Grid> searchMinesAround(Grid area) {
        ArrayList<Grid> areaAround = new ArrayList<Grid>();
        int X = area.gridX - 1;
        int Y = area.gridY - 1;

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (X+i > 0 && Y+j > 0 && X+i <= x && Y+j <= y) {
                    areaAround.add(this.area[X+i][Y+j]);
                }
            }
        }

        return areaAround;
    }


    private void checkWin() {
        if ((openNum) == (x * y -mineNum)) {
            gameOver(true);
        }
    }

    public static void playSound(File path){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(path));
            clip.start();
        }
        catch (Exception e){
            System.out.println("Error! No sound!");
        }

    }

    private void gameOver(boolean status) {
        timer.stop();
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                area[i][j].gameOver=true;
            }
        }
        if (status) {
            ImageIcon icon= new ImageIcon("img/brutal.png");
            reset.setIcon(icon);

            rand = (int)(10*Math.random());
            File win = new File("music\\win"+rand+".wav");
            playSound(win);
            window.setContentPane(back);
        }
        else {
            ImageIcon icon = new ImageIcon("img/smiley.gif");
            reset.setIcon(icon);
            rand=(int)(11*Math.random());
            File lose = new File("music\\lose"+rand+".wav");
            playSound(lose);
            window.setContentPane(back);
        }
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                if (area[i][j].mined) {
                    area[i][j].opened = true;
                    area[i][j].repaint();
                }
            }
        }
    }
        class Timer extends JLabel {
            private boolean go;
            private Runnable task;
            private int counter=0;

            public Timer() {
                super("00:00", SwingConstants.CENTER);
                task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        while (go) {
                            counter++;
                            window.setContentPane(back);
                            if((counter/60)/10>0){
                                if((counter%60)/10>0){
                                    setText(counter/60+":"+counter%60);
                                }
                                else{
                                    setText(counter/60+":0"+counter%60);
                                }
                            }
                            else {
                                if ((counter % 60) / 10 > 0) {
                                    setText("0"+counter / 60 + ":" + counter % 60);
                                } else {
                                    setText("0"+counter / 60 + ":0" + counter % 60);
                                }
                            }


                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {

                            }

                        }
                    }
                };
                go = false;
            }
            public void start() {
                setForeground(Color.BLACK);
                go = true;
                new Thread(task).start();
            }
            public void stop() {
                go = false;
                setForeground(Color.RED);
            }
            public void reset() {
                setForeground(Color.BLACK);
                go = false;
                counter=0;
                setText("00:00");
            }
        }



}