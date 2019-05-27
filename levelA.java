import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Scanner;

public class levelA {
  public String[][] areaMined = new String[11][11];
  public String[][] areaOpened = new String[11][11];
  public Boolean lose = false;
  public Boolean win = false;
  public Boolean[][] checkZero = new Boolean[11][11];
  public int randCount=0;
  public int rand;
  public int rand1=1;

  private String hidden = "■";
  private String space = " ";
  private String mine = "¤";

  public levelA(){

    for(int i = 0; i < areaMined.length; i++){
      for(int j = 0; j < areaMined[0].length; j++){
        checkZero[i][j]=false;

        if((i==0&&j==0)||(i==10&&j==10)||(i==0&&j==10)||(i==10&&j==0)){
          areaMined[i][j] = space;
          areaOpened[i][j] = space;
        }

        else if(i == 0 || i == areaMined.length-1){
          areaMined[i][j] = Integer.toString(j);
          areaOpened[i][j] = Integer.toString(j);
        }
        else if(j == 0 || j == areaMined[0].length-1){
          areaMined[i][j] = Integer.toString(i);
          areaOpened[i][j] = Integer.toString(i);
        }
        else{
          areaMined[i][j] = hidden;
          areaOpened[i][j] = hidden;
        }
      }
    }
  }

  public void createMine(int num){
    int i=0;
    while(i<num){
      while(true){
        int x, y;
        x = (int)(8*Math.random()+1);
        y = (int)(8*Math.random()+1);

        if(!areaMined[x][y].equals(mine)){
          areaMined[x][y] = mine;
          i++;
//          System.out.println(x+" "+y);
          break;
        }
      }
    }
  }

  public void searchMinesAround(){
    for(int x = 1; x < areaOpened.length - 1; x++){
      for(int y = 1; y < areaOpened.length - 1; y++){
        if(areaMined[x][y].equals(space) == true){
          int num = 0;
          for(int i = (x - 1); i <= (x + 1); i++){
            for(int j = (y - 1); j <= (y + 1); j++){
              if(areaMined[i][j].equals(mine) == true)
                num++;
            }
          }
          areaOpened[x][y] =Integer.toString(num);
        }
      }
    }
  }

  public void clear(int x, int y){  
    for(int i = (x - 1); i <= (x + 1); i++){
      for(int j = (y - 1); j <= (y + 1); j++){
        if(areaMined[i][j].equals(hidden) == true){
          areaOpened[i][j] = space;
          areaMined[i][j] = space;
        }
      }
    }
  }

  public void clearAll(){
      boolean check = true;

      while(check){
        int counter=0;
        searchMinesAround();

        outerloop:
        for(int i=1;i<=checkZero.length-1;i++){
          for(int j=1;j<=checkZero[0].length-1;j++){
            counter++;
            if(areaOpened[i][j].equals("0")==true){
              if(checkZero[i][j]!=true){
                clear(i,j);
                checkZero[i][j]=true;
                searchMinesAround();
                counter=0;
                break outerloop;
              }
            }
          }
        }
        if(counter>=81) {check=false;}
        searchMinesAround();
      }
  }
  



  public void openArea(int x, int y){
    randCount++;
    if(areaMined[x][y].equals(space) == true){
      System.out.println("This area is already opened!");
    }
    else if(areaMined[x][y].equals(hidden) == true){
      if(randCount%rand1==0){
        rand=(int)(33*Math.random());
        rand1=(int)(3*Math.random()+2);
        File step = new File("music\\step"+rand+".wav");
        playSound(step);
      }
      areaOpened[x][y] = space;
      areaMined[x][y] = space;
    }
    else if(areaMined[x][y].equals(mine) == true){
      lose = true;
    }
  }

  public void replace(int x, int y){
    if(areaMined[x][y].equals(mine)==true){
      areaMined[x][y]=hidden;
      createMine(1);
    }
  }

  public void checkWin(){
    int space = 0;
    for(int i = 0; i < areaMined.length; i++){
      for(int j = 0; j < areaMined[0].length; j++){
        if(areaMined[i][j].equals(hidden) == true)
          space++;
      }
    }
    if(space != 0){
      win = false;
    }
    else{
      win = true;
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


  public static void printArea(String[][] map){
    for(int i = 0; i < map.length; i++){
      for(int j = 0; j < map[0].length ; j++){
        if(j > 0 && j < map[0].length) {
          System.out.print("|");
        }
        else {
          System.out.println();
        }
        System.out.print(map[i][j]);
      }
    }
  }

  public void printMines(){
    printArea(areaMined);
  }

  public void updateArea(){
    printArea(areaOpened);
    System.out.println();
  }

  public static void main(String[] args) {
    System.out.println("Welcome to the Techieeees! ");
    int rand;
    levelA game = new levelA();
    game.createMine(10);
    game.updateArea();
    Scanner scan = new Scanner(System.in);

    int x, y;
    rand=(int)(5*Math.random());
    File begin = new File("music\\begin"+rand+".wav");
    playSound(begin);

    System.out.print("Enter your next move (row[1-9] column[1-9]): ");
    y = scan.nextInt();
    x = scan.nextInt();
    game.replace(x,y);
    game.openArea(x,y);
    game.checkWin();
    game.clearAll();
    game.searchMinesAround();
    game.updateArea();

    while(true){
      if(game.win){
        System.out.println("Counter-Techies won!");
        game.printMines();

        rand = (int)(10*Math.random());
        File win = new File("music\\win"+rand+".wav");
        playSound(win);
        try
        {
          Thread.sleep(5000);
        }catch(InterruptedException e){}
        break;
      }

      else if(game.lose){
        System.out.println("KABOOOOOM! You are bombed!");
        game.printMines();
        rand=(int)(11*Math.random());
        File lose = new File("music\\lose"+rand+".wav");
        playSound(lose);
        try
        {
          Thread.sleep(5000);
        }catch(InterruptedException e){}
        break;
      }

      else if(!game.win && !game.lose){
        System.out.print("Enter your next move (row[1-9] column[1-9]): ");
        y = scan.nextInt();
        x = scan.nextInt();

        try{
          game.openArea(x,y);
          game.checkWin();
          game.clearAll();
          game.searchMinesAround();
          game.updateArea();
        }catch (ArrayIndexOutOfBoundsException e){
          System.out.println("Your input is wrong! Try again!");
          System.out.println();
        }
      }
    }
  }
}
