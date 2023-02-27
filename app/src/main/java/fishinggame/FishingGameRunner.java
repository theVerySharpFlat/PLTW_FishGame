package fishinggame;

/********************************************
 * Sample solution for Activity 4.9.3 
 ********************************************/
public class FishingGameRunner
{
  public static void main(String[] args)
  {
    Player player = new Player();
    GameGUI gameGUI = new GameGUI(player);
   
    // setup all the components of the game
    gameGUI.playGame();
  }
}
