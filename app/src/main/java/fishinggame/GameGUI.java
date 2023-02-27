package fishinggame;

/*
* Activity 4.9.3 and 4.9.4 
* A Fishing Game
* 
* V2.0
* 11/2/2022 Resized UI and added scalability 
*
* Copyright(c) 2019 PLTW to present. All rights reserved
*/
import java.lang.Math;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.text.DefaultCaret;

import java.io.File;
import javax.imageio.ImageIO;

/**
 * A fishing game board to show all items in a fisherman/woman's inventory.
 */
public class GameGUI extends JComponent implements ActionListener {
  private static final long serialVersionUID = 26L;

  // game setup info
  private Player player;

  // inventory items
  private Image itemImage;
  private ArrayList<LakeObject> inventoryItems;

  // game frame
  private JFrame frame;
  private JTextField textInput;

  // output panel
  private JTextArea outputArea;
  private JPanel topPanel;

  private JPanel midPanel;

  // command panel
  private JScrollPane outputScrollPane;
  private JLabel label;
  private JButton button;
  private JPanel bottomPanel;

  /**
   * Constructor for the GameGUI class.
   * Creates a frame with a background image and command textbox.
   */
  public GameGUI(Player player) {

    // set player and their inventory items
    this.player = player;
    inventoryItems = player.getInventory();

    // create game frame
    frame = new JFrame();
    frame.setTitle("FishingGame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // inventory panel
    topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(2,8));
    createInventoryItems();

    // text area for instructions, results
    midPanel = new JPanel();
    outputArea = new JTextArea(12,30); 
    // autoscroll to text at bottom
    DefaultCaret caret = (DefaultCaret)outputArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    outputArea.setEditable(false);
    outputScrollPane = new JScrollPane(outputArea);  
    midPanel.add(outputScrollPane);

    // command panel
    bottomPanel = new JPanel();

    textInput = new JTextField(5);
    textInput.requestFocus();
    label = new JLabel("Enter command:");
    button = new JButton("Execute");
    button.addActionListener(this);
    // enable button to execute command
    frame.getRootPane().setDefaultButton(button);
    bottomPanel.add(label);
    bottomPanel.add(textInput);
    bottomPanel.add(button);

    // render game frame
    frame.add(BorderLayout.PAGE_START, topPanel);
    frame.add(BorderLayout.CENTER, midPanel);
    frame.add(BorderLayout.SOUTH, bottomPanel);
    frame.pack();
    frame.setVisible(true);
  }
 
  /*
   * Add items in the player's inventory to the top panel.
   * 
   * @param s a String to append to the output
   */
  public void createInventoryItems()
  {
    String str = "";
    for (int i = 0; i < player.getMaxInventory(); i++)
    {
      if (i < player.getInventorySize())
      {
        LakeObject item = inventoryItems.get(i);
        if (item.getObjectName().equals("Hook"))             
          str = "Pow:" + ((Hook)item).getStrength();
        else 
          str ="$" + item.getCost();
      }
      else
      {
        str = "";
      }

      setItemImage(i);
      ImageIcon icon = new ImageIcon(itemImage);
      JLabel label = new JLabel(str, icon, JLabel.CENTER);
      label.setHorizontalTextPosition(JLabel.CENTER);
      label.setVerticalTextPosition(JLabel.BOTTOM);

      topPanel.add(label);
    }
  
  }

  /*
   * Append to the output area
   * 
   * @param s a String to append to the output
   */
  public void setOutput(String s) {
    outputArea.append(s + "\n");
  }

  /*
   * Add a player to the game and set the inventory and wallet values.
   * 
   * @param p player to be added
   */
  public void playGame() {
    goToForest();
  }

  /*---------- public methods not to be called directly ----------*/

  /*
   * Action listener for the execute button, invokes the
   * execCommand method.
   * Note: This method is used by the textbox and Execute
   * button and should not be called directly.
   */
  public void actionPerformed(ActionEvent event) {
    String input = textInput.getText();

    // a try is like an if statement, "throwing" an error if the body of the try fails
    try {
      int inputInt = Integer.parseInt(input);
      execCommand(inputInt);
    } catch (Exception e) {
      // continue with string input
      execCommand(input);
    }
    textInput.setText("");
    frame.revalidate(); 
    frame.repaint();
  }

  /*---------- private/convenience methods ----------*/
  /*
   * Set the image for an inventory item
   */
  private void setItemImage(int index) {
    if (index == 0)
    {
        try {
          itemImage = ImageIO.read(new File("Wallet.png"));
        } catch (Exception e) {
          System.err.println("Warning: Could not open file Wallet.png");
        }
    }
    else
    {
      String imageName = "Empty";
      if (index < player.getInventorySize())
        imageName = player.getInventoryName(index);
      
        // a try is like an if statement, "throwing" an error if the body of the try fails
      try {
        itemImage = ImageIO.read(new File(imageName + ".png"));
      } catch (Exception e) {
        System.err.println("Warning: Could not open file " + imageName + ".png");
      }
    }
  
  }

  /*
   * Update the displayed inventory to player's inventory.
   */
  private void updateInventoryImages() {
    inventoryItems = player.getInventory();

    // cannot easily update labels with images and text so recreate
    for (Component c : topPanel.getComponents()) {
      if (c instanceof JLabel) {
          topPanel.remove(c);
      }
    }
    createInventoryItems();
   
  }

  /**
   * Execute the command entered by the player if the command is an integer
   *
   * @param input int command entered by the player
   */
  private void execCommand(int input) {
    if (player.getCurrentLocation().equals("shop")) {
      sellFish(input);
    }
    
    updateInventoryImages();
  }

  /**
   * Execute the command entered by the player if it's a String
   * 
   * @param input String command entered by the player
   */
  private void execCommand(String input) {
    if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
      setOutput("Thank you, bye!");
      frame.dispose();
    } else if (input.equalsIgnoreCase("shop") || input.equals("S")) {
      goToShop();
    } else if (input.equalsIgnoreCase("lake") || input.equals("L")) {
      goToLake();
    } else if ((input.equalsIgnoreCase("yes") || input.equals("y"))
        && player.getCurrentLocation().equalsIgnoreCase("lake")) {
      goFish();
      goToLake();
    } else if ((input.equalsIgnoreCase("no") || input.equals("n"))
        && player.getCurrentLocation().equalsIgnoreCase("lake")) {
      goToForest();
    } else if ((input.equalsIgnoreCase("no") || input.equals("n"))
        && player.getCurrentLocation().equalsIgnoreCase("shop")) {
      goToForest();
    } else if ((input.equalsIgnoreCase("bait") || input.equals("b"))
        && player.getCurrentLocation().equalsIgnoreCase("shop")) {
      setOutput("Buy Bait");
      buyBait();
      goToShop();
    } else if ((input.equalsIgnoreCase("hook") || input.equals("h"))
        && player.getCurrentLocation().equalsIgnoreCase("shop")) {
      setOutput("Buy Hook");
      buyHook();
      goToShop();
    } else if (input.equalsIgnoreCase("leave") || input.equals("l")) {
      setOutput("Thank you, return anytime!");
      goToForest();
    } else if ((input.equalsIgnoreCase("sell") || input.equals("s"))
        && player.getCurrentLocation().equalsIgnoreCase("shop")) {
      setOutput("Sell Fish");
      setOutput("What is the location of the fish you want to sell?");
    } else {
      setOutput("Not a valid command.");
      // reset output and input
      if (player.getCurrentLocation().equalsIgnoreCase("forest"))
        goToForest();
      else if (player.getCurrentLocation().equalsIgnoreCase("lake"))
        goToLake();
      else if (player.getCurrentLocation().equalsIgnoreCase("shop"))
        goToShop();
    }

    updateInventoryImages();
  }

  /*---------- Private Game Area methods ----------*/

  /* Game Forest Area */
  private void goToForest() {
    outputArea.setText("");
    player.setCurrentLocation("forest");
    setOutput("\nYou are in a forest. Where would you like to go?");
    setOutput("\nEnter a valid choice: Lake(L), Shop(S), Quit(Q)");
  }

  /* Game Shop Area */
  private void goToShop() {
    player.setCurrentLocation("shop");
    setOutput(
        "\nWelcome to the shop. What would you like to do?\n buy bait (b)\n buy a hook (h)\n sell fish (s)\n or leave (l)");
  }

  private void buyBait() {
    Bait b = new Bait();

    if (player.roomInInventory()) {
      if (player.getWallet() >= b.getCost()) {
        player.updateWallet(-1 * b.getCost());
        player.updateInventory(b, true);
        setOutput(b.say());
      } else
        setOutput("You cannot afford a bait.");
    } else
      setOutput("You have no room in your inventory.");

  }

  private void buyHook() {
    Hook h = new Hook();

    if (player.roomInInventory()) {
      if (player.getWallet() >= h.getCost()) {
        player.updateWallet(-1 * h.getCost());
        player.updateInventory(h, true);
        setOutput(h.say());
      } else
        setOutput("You cannot afford a hook.");
    } else
      setOutput("You have no room in your inventory.");

  }

  private void sellFish(int location) {
    boolean sold = player.loseItem("Fish", location, true);

    if (sold)
      setOutput("Thank you. Your wallet has been updated.");
    else
      setOutput("You did not have a fish at that location.\nDon't try to trick me!");

    goToShop();
  }

  /* Game Lake Area */
  public void goToLake() {
    player.setCurrentLocation("lake");
    setOutput("\nWelcome to the lake. Would you like to fish?\n yes(y)\n no(n)\n or leave(l)");
  }

  public void goFish() {
    // generate a Lake full of new LakeObjects
    LakeObject[] randomObjects = new LakeObject[4];
    randomObjects[0] = new Fish();
    randomObjects[1] = new Wallet();
    randomObjects[2] = new Fish();
    randomObjects[3] = new Boot();
    System.out.println("goFish");

    if (player.hasHook() && player.hasBait()) {
      int i = (int) (Math.random() * randomObjects.length) + 1;
      Hook strongestHook = player.getStrongestHook();

      if (i >= randomObjects.length) {
        setOutput("\nYou lost your hook  :(");
        player.updateInventory(new Hook(), false);
      } else if (randomObjects[i].wasCaught(strongestHook)) {
        if (randomObjects[i].getObjectName().equals("Wallet")) {
          player.updateWallet(randomObjects[i].getCost());
          setOutput("You caught a Wallet!");
          setOutput(randomObjects[i].say());
          setOutput("Money from the wallet was added to your inventory");
        } else {
          if (player.roomInInventory()) {
            player.updateInventory(randomObjects[i], true);
            setOutput(randomObjects[i].say());
          } else
            setOutput("You have no room in your inventory.");
        }
      } else {
        setOutput("\nYour hook had a strength of " + strongestHook.getStrength() +
            "\nand needed at least a strength of " + randomObjects[i].getWeight());
      }
    } else {
      setOutput("You need a hook and bait to go fishing. You can buy them at the shop.");
    }
  }

}
