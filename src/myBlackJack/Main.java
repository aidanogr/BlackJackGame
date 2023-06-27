package myBlackJack;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class Main {

    /* Demo BlackJack game with responsive design (Using ImageDrawer and ScaledImageLabel class
     * All card data stored in Card class
     * All game logic and functions are stored in this Main class
     * Read code bottom -> top for order of events
     * 
     */
    
    
    public final static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public final static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public final static int DEFAULT_WIDTH = (int) (SCREEN_WIDTH*0.7);
    public final static int DEFAULT_HEIGHT = (int) (SCREEN_HEIGHT*0.9);
    
    
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
    private static final String[] SUITS = { "Hearts", "Clubs", "Diamonds", "Spades" };
    private static final int[] POINT_VALUES = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11 };
    
    //frame variables
    private static JFrame frame;
    private static JPanel infoPanel;
    private static JLabel myBet;
    private static JButton betButton;
    private static JLabel dealerBet;
    private static JPanel gamePanel;
    private static JPanel playPanel;
    private static JButton newGame;
    private static JLabel bg;
    private static JPanel dealerPanel;
    private static JPanel playerPanel;
    private static JPanel deckPanel;
    private static GridBagConstraints gpGBC;
    private static GridBagConstraints gbc;
    private static FlowLayout fLayout;
    private static JButton standButton;
    private static JButton hitButton;
    private static JLabel dc;
    private static JButton dealNew;
    private static ImageIcon bgIm;
    private static JLabel dealerCurrencyLabel;
    private static JLabel playerCurrencyLabel;
    //game variables
    private static int myBetAmount;
    private static int dealerBetAmount;
    private static int dealerCurrency;
    private static int playerCurrency;
    private static ArrayList<Card> shoe;
    private static ArrayList<Card> dealerCards;
    private static ArrayList<Card> playerCards;
    private static Boolean showDealerCard;
    private static Boolean gameActive;
   
    public static void revalidateGame() {
	frame.setSize(new Dimension(frame.getSize().width +1, frame.getSize().height));
	frame.setSize(new Dimension(frame.getSize().width -1, frame.getSize().height));
    }
   
    public static void resetGame() throws Exception {
	
	dealerBetAmount = 0;
	myBetAmount = 0;
	dealerCards = new ArrayList<Card>();
	playerCards = new ArrayList<Card>();
	
	
	frame.getContentPane().removeAll();
	frame.repaint();
	playerCurrencyLabel.setText("My Currency: " + playerCurrency);
	dealerCurrencyLabel.setText("Dealer's Currency: " + dealerCurrency);
	showDealerCard = false;
	betButton.setEnabled(true);
	dealNew.setEnabled(true);
	frame.setEnabled(true);
	startGame();
	frame.revalidate();
    }
    
    public static void winner(int winner) throws Exception {
	String w = "";
	if(winner == 0) {
	    w = "dealer";
	    dealerCurrency+=myBetAmount;
	    playerCurrency-=myBetAmount;
	}
	else if(winner == 1) {
	    w = "Player";
	    playerCurrency+=dealerBetAmount;
	    dealerCurrency-=dealerBetAmount;
	    
	}
	else {
	    w = "tie, Player";
	    playerCurrency+=dealerBetAmount;
	    dealerCurrency-=dealerBetAmount;
	    System.out.println("Tie");
	    revalidateGame();
	}
	JButton button = new JButton();
	button.setText(w + " wins... Reset Game");
	frame.setEnabled(false);
	JFrame winnerFrame = new JFrame();
	System.out.println("test");
	winnerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	winnerFrame.add(button);
	winnerFrame.setVisible(true);
	winnerFrame.setLocation(SCREEN_WIDTH/2 - 200/2, SCREEN_HEIGHT/2 - 160/2);
	winnerFrame.setSize(new Dimension(200, 160));
	//frame.revalidate();
	//revalidateGame();
	button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    button.setVisible(false);
		    winnerFrame.dispose();
		    
		    resetGame();
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    
		    
		    e1.printStackTrace();
		}
	    }
	});
	
    }
    
    
    public static int getHandValue(ArrayList<Card> hand) {
	int count = 0;
	for(int i = 0; i < hand.size(); i++) {
	    count += hand.get(i).pointValue();
	}
	return count;
    }
    
    public static int checkDealerBust() throws Exception {
	int count = getHandValue(dealerCards);
	if(count > 21) {
	    for(Card c : dealerCards) {
		if(c.isAce() && c.pointValue()!=1) {
		    c.changeValue();
		    return checkDealerBust();
		}
		
	    }
	}
	return count;
    }
    
    public static int dealerHit() throws Exception {
	dealerCards.add(getRandomCard());
	displayCards(showDealerCard);
	int c = checkDealerBust();
	return c;
    }
    
    public static void dealerPlays() throws Exception {
	int dealerCount = 0;
	showDealerCard = true;
	dealerCount = getHandValue(dealerCards);
	
	while(dealerCount < 16) {
	    dealerCount = dealerHit();
	}
	displayCards(showDealerCard);
	if(dealerCount <= 21 && dealerCount > getHandValue(playerCards)) {
	    winner(0);
	}
	else if(dealerCount <= 21 && dealerCount == getHandValue(playerCards)) {
	    winner(3);
	}
	else{winner(1);
	};
	
    }
    
    public static void stand() throws Exception {
	dealerPlays();
    }
    
    public static void checkPlayerBust() throws Exception {
	int count = getHandValue(playerCards);
	
	if(count > 21) {
	    for(Card c : playerCards) {
		if(c.isAce() && c.pointValue() != 1) {
		    c.changeValue();
		    checkPlayerBust();
		    return;
		}
		
	    }
	    winner(0);
	    displayCards(true);
	}
	
    }  
    
    public static void hitPlayer() throws Exception {
	playerCards.add(getRandomCard());
	displayCards(showDealerCard);
	checkPlayerBust();
    }
    
    public static void bet() {
	myBet.setText("My Bet: " + myBetAmount);
	dealerBetAmount = (int) (myBetAmount*3/2);
	dealerBet.setText("Dealer's Bet: " + dealerBetAmount);

	revalidateGame();
    }
    
    public static void displayCards(Boolean d) {
	
	dealerPanel.removeAll();
	playerPanel.removeAll();
	frame.repaint();
	
	if(d) {
	    for(int i = 0; i<dealerCards.size(); i++) {
	    	dealerPanel.add(Card.getCardFace(dealerCards.get(i)));
		}
	}
	else {
	    dealerPanel.add(Card.getBlankCard());
	    dealerPanel.add(Card.getCardFace(dealerCards.get(1)));
	}
	
	for(int j = 0; j<playerCards.size(); j++) {
	    playerPanel.add(Card.getCardFace(playerCards.get(j)));
	}
	revalidateGame();
	frame.revalidate();
    }
    
    public static Card getRandomCard() {
	if (shoe.size() < 12) {
	    System.out.println("New Shoe");
	    createNewShoe();
	}
	Card random = shoe.get((int) (Math.random()*shoe.size()));
	shoe.remove(random);
	return random;
    }
    

    
    public static void createNewShoe() {
	shoe = new ArrayList<Card>();
	for(int x = 0; x<3; x++) {
	    for(int i = 0; i<RANKS.length; i++) {
	    	for(int j = 0; j<SUITS.length; j++) {
			shoe.add(new Card(RANKS[i], SUITS[j], POINT_VALUES[i]));
	    	}
	    }
	}
    }
    
    public static void newGame() throws Exception {
	
	playerCurrency = 5000;
	dealerCurrency = 5000;
	
	playerCurrencyLabel.setText("My Currency: " + playerCurrency);
	dealerCurrencyLabel.setText("Dealer's Currency: " + dealerCurrency);
	
	dealerCards = new ArrayList<Card>();
	playerCards = new ArrayList<Card>();
	gameActive = true;
	showDealerCard = false;
	dealNew.setEnabled(gameActive);
	
	newGame.setEnabled(false);
	
	/*A new game will create a new shoe of cards that will be played until the player clicks ('New Game') again, similar to how
	a shoe is used in real life. This makes the odds of winning more realistic*/
	createNewShoe();
	
	revalidateGame();
    }
    
    public static void dealNewHand() throws Exception {

	betButton.setEnabled(false);;	
	hitButton.setEnabled(true);	//these buttons will continue the game, -> 'hitPlayer()'
	standButton.setEnabled(true);	// ->'stand()'. Buttons initialized in startGame
	dealNew.setEnabled(false);
	
	//gets card from shoe
	for(int i = 0; i<2; i++) {
	    dealerCards.add(getRandomCard());
	}
	
	for(int j = 0; j<2; j++) {
	    playerCards.add(getRandomCard());
	}
	
	displayCards(showDealerCard);
	revalidateGame();
	frame.revalidate();
    }
    
    public static void startGame() throws Exception {
	//PAINTS JFRAME WITH GAMEPANEL;INFOPANEL;PLAYPANEL; 
	

	//SETUP LAYOUT AND ADD PANELS TO LAYOUT
	frame.setLayout(new GridBagLayout());
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	frame.requestFocus();
	
	
	
	
	
	
	//SETUP GAME PANEL (MIDDLE PANEL)
	gamePanel = new JPanel();
	gamePanel.setLayout(new GridBagLayout());		//Layout within gamePanel
	gamePanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
	gpGBC = new GridBagConstraints(); 
	deckPanel = new JPanel(new FlowLayout());		//panel in gamePanel that displays deck
	dealerPanel = new JPanel(new FlowLayout());		//panel in gamePanel that displays dealer's cards
	playerPanel = new JPanel(new FlowLayout());		//panel in gamePanel that displays player cards
	fLayout = new FlowLayout();				//Layout for dealer's cards
	
	ImageIcon deck = new ImageIcon("src\\cards\\back1.gif");
	dc = new JLabel(deck);
	
	deckPanel.add(dc);
	deckPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
	deckPanel.setLayout(fLayout);
	gpGBC.gridy = 0;
	gpGBC.fill = GridBagConstraints.BOTH;
	gpGBC.weighty = 1;
	gamePanel.add(deckPanel, gpGBC);
	dealerPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
	gpGBC.gridy = 1;
	gpGBC.fill = GridBagConstraints.BOTH;
	gpGBC.weighty = 1;
	gamePanel.add(dealerPanel, gpGBC);
	playerPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
	gpGBC.gridy = 2;
	gpGBC.fill = GridBagConstraints.BOTH;
	gpGBC.weighty = 1;
	gamePanel.add(playerPanel, gpGBC);
	gbc.gridy = 1;
	gbc.weighty = 1;
	frame.add(gamePanel, gbc);
	
	//resizable background board
	File backg = new File("src\\ff.jpg");
	Image image = ImageIO.read(backg);
	bgIm = new ImageIcon(image);
	bg = new ScaledImageLabel();
	bg.setIcon(bgIm);
	gbc.gridy = 1;
	gbc.gridx = 0;
	gbc.weighty = 1;
	frame.add(bg, gbc);
	revalidateGame();
	
	
	
	
	//SETUP INFO PANEL (TOP PANEL)
	infoPanel = new JPanel();
	infoPanel.setLayout(new GridLayout());
	infoPanel.setBackground(Color.BLACK);
	gbc.gridy = 0;  
	gbc.weighty = 0;
	gbc.ipady = 15;
	gbc.weightx = 1;
	frame.add(infoPanel, gbc);
	myBet = new JLabel("My Bet: ");
	myBet.setForeground(Color.WHITE);

	newGame = new JButton();	//This button starts the game (see dealNewHand func.)
	newGame.setText("New Game");
	newGame.addActionListener( new ActionListener() {public void actionPerformed(ActionEvent e){
	    try {
		newGame();
	    } catch (Exception e1) {
		e1.printStackTrace();
	    }
	    newGame.setEnabled(false);
	    revalidateGame();}} );
	
	dealNew = new JButton();
	dealNew.setText("Deal New Hand...");
	dealNew.setEnabled(gameActive);
	dealNew.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    dealNewHand();
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	});

	infoPanel.add(newGame);
	infoPanel.add(myBet);
	infoPanel.add(playerCurrencyLabel);
	infoPanel.add(dealerBet);
	infoPanel.add(dealerCurrencyLabel);
	infoPanel.add(dealNew);
	
	

	
	//SETUP PLAY/BET PANEL (BOTTOM PANEL)
	playPanel = new JPanel();
	playPanel.setLayout(new FlowLayout());
	playPanel.setBackground(Color.BLACK);
	betButton = new JButton();
	betButton.setEnabled(true);
	betButton.setText("Bet $1");
	betButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e){
	    myBetAmount += 1;
	    bet()
	    ;}});
	standButton = new JButton();
	standButton.setEnabled(false);
	standButton.setText("Stand");
	standButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    stand();
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }});
	    
	hitButton = new JButton();
	hitButton.setEnabled(false);
	hitButton.setText("Hit");
	hitButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    hitPlayer();
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	});
	playPanel.add(hitButton);
	playPanel.add(standButton);
	playPanel.add(betButton);

	gbc.gridy = 2;
	gbc.weighty = 0;
	gbc.ipady =15;
	frame.add(playPanel, gbc);
	revalidateGame();
	
	
	

    }
    
    public static void displayGameScreen() throws Exception {
	//INITIALIZE FRAME AND SET LOCATION/SIZE
	frame = new JFrame();
	frame.setVisible(true);
	frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	frame.setLocation(SCREEN_WIDTH/2 - DEFAULT_WIDTH/2, SCREEN_HEIGHT/2 - DEFAULT_HEIGHT/2); //Math to center screen
	frame.setMinimumSize(new Dimension(500, 500));
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	
	//game info initialization
	dealerBet = new JLabel("Dealer's Bet: ");
	dealerBet.setForeground(Color.WHITE);
	playerCurrencyLabel = new JLabel("My Currency: ");;
	playerCurrencyLabel.setForeground(Color.WHITE);
	dealerCurrencyLabel = new JLabel("Dealer's Currency: ");
	dealerCurrencyLabel.setForeground(Color.WHITE);
	gameActive = false;
	
	//call start to game
	startGame();
	
	
    }
    
    public static void main(String[] args) throws Exception {

	//UTILIZES CLASSES:
	//BlackJackDealer;BlackJackGame;BlackJackPlayer;Card;ImageDrawer;Main(this);ScaledImageLabel;
	
	//START
	displayGameScreen();

    }

}
