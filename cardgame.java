
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // create a new deck of cards
        Deck deck = new Deck();

        // shuffle the deck
        deck.shuffle();

        // create the players
        Player[] players = new Player[4];
        for (int i = 0; i < 4; i++) {
            players[i] = new Player("Player " + (i + 1));
        }

        // deal 5 cards to each player
        for (int i = 0; i < 5; i++) {
            for (Player player : players) {
                Card card = deck.dealCard();
                player.addCardToHand(card);
            }
        }

        // create a new game with the players and the remaining cards in the deck
        Game game = new Game(players, deck);

        // play the game
        game.play();
    }
}

public class Card {
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }
}

public enum Rank {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public enum Suit {
    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES
}

public class Deck {
    private List<Card> cards;
    private List<Card> drawPile;
    private List<Card> discardPile;

    public Deck() {
        cards = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();

        String[] suits = { "hearts", "spades", "clubs", "diamonds" };
        String[] ranks = { "ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king" };

        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (drawPile.isEmpty()) {
            drawPile.addAll(discardPile);
            discardPile.clear();
            shuffle();
        }

        Card card = drawPile.remove(0);
        return card;
    }

    public void addToDiscard(Card card) {
        discardPile.add(card);
    }
}
import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;

    public Player(Deck deck) {
        hand = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Card card = deck.draw();
            hand.add(card);
        }
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public Card removeCard(int index) {
        Card card = hand.remove(index);
        return card;
    }

    public int getHandSize() {
        return hand.size();
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private Deck deck;
    private Card topCard;
    private boolean reverse;
    private int currentPlayerIndex;

    public Game() {
        players = new ArrayList<>();
        deck = new Deck();
        topCard = deck.draw();
        reverse = false;
        currentPlayerIndex = 0;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of players (2-4): ");
        int numPlayers = scanner.nextInt();

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(deck);
            players.add(player);
        }
    }

    public void play() {
        System.out.println("Starting game...");

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("Player " + (currentPlayerIndex + 1) + "'s turn");

            System.out.println("Top card: " + topCard.getSuit() + " " + topCard.getRank());
            System.out.println("Your hand:");
            for (int i = 0; i < currentPlayer.getHandSize(); i++) {
                Card card = currentPlayer.removeCard(i);
                System.out.println((i + 1) + ": " + card.getSuit() + " " + card.getRank());
            }

            System.out.println("Choose a card to play (or 0 to draw):");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if (choice == 0) {
                Card card = deck.draw();
                System.out.println("You drew: " + card.getSuit() + " " + card.getRank());

                if (card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank())) {
                    System.out.println("You can play this card!");
                    currentPlayer.addCard(card);
                } else {
                    System.out.println("You cannot play this card. Turn over.");
                }

            } else {
                Card card = currentPlayer.removeCard(choice - 1);

                if (card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank())) {
                    System.out.println("You played: " + card.getSuit() + " " + card.getRank());

                    if (card.getRank().equals("ace")) {
                        System.out.println("Skipping next player...");
                        if (reverse) {
                            currentPlayerIndex = (currentPlayerIndex - 2 + players.size()) % players.size();
                        } else {
                            currentPlayerIndex = (currentPlayerIndex + 2) % players.size();
                        }
                    } else if (card.getRank().equals("king")) {
                        System.out.println("Reversing order...");
                        reverse = !reverse;
                    } else if (card.getRank().equals("queen")) {
                        System.out.println("Drawing 2 cards for next player...");
                        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
                        Player nextPlayer = players.get(nextPlayerIndex);
                        nextPlayer.addCard(deck.draw());
                        nextPlayer.addCard(deck.draw());
                    } else if (card.getRank().equals("jack")) {
                        System.out.println("Drawing 4 cards for next player...");
                        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
                        Player nextPlayer = players.get(nextPlayerIndex);
                        nextPlayer.addCard(deck.draw());
                        nextPlayer.addCard(deck.draw());
                        nextPlayer.addCard(deck.draw());
                        nextPlayer.addCard(deck.draw());
                    }

                    topCard = card;
                } else {
                    System.out.println("You cannot play this card. Turn over.");
                    currentPlayer.addCard(card);
                }
            }

            if (currentPlayer.getHandSize() == 0) {
                System.out.println("Player " + (currentPlayerIndex + 1) + " wins!");
                break;
            }

            if (reverse) {
                currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
    }

}

public void start() {
    // Shuffle the deck and deal initial hands
    deck.shuffle();
    for (int i = 0; i < numPlayers; i++) {
        hands[i] = new Hand();
        for (int j = 0; j < INIT_HAND_SIZE; j++) {
            hands[i].addCard(deck.deal());
        }
    }
    discardPile.addCard(deck.deal());

    // Start the game
    int currentPlayer = 0;
    int direction = 1;
    boolean gameEnd = false;
    while (!gameEnd) {
        System.out.println("It is Player " + (currentPlayer + 1) + "'s turn.");
        System.out.println("Top card on discard pile: " + discardPile.topCard());

        // Check if player can play a card
        boolean canPlayCard = false;
        for (Card card : hands[currentPlayer].getCards()) {
            if (card.matches(discardPile.topCard())) {
                canPlayCard = true;
                break;
            }
        }

        if (canPlayCard) {
            // Player can play a card
            Card cardToPlay = null;

            while (cardToPlay == null) {
                // Ask player to choose a card to play
                System.out.println("Your hand: " + hands[currentPlayer]);
                int cardIndex = getValidInput("Enter the index of the card you want to play: ", hands[currentPlayer].getSize());
                cardToPlay = hands[currentPlayer].removeCard(cardIndex);

                if (!cardToPlay.matches(discardPile.topCard())) {
                    // Invalid card, put it back in the player's hand
                    hands[currentPlayer].addCard(cardToPlay);
                    cardToPlay = null;
                }
            }

            // Handle action cards
            switch (cardToPlay.getRank()) {
                case ACE:
                    System.out.println("Player " + (currentPlayer + 1) + " played an Ace! The next player will be skipped.");
                    currentPlayer = getNextPlayer(currentPlayer, direction);
                    break;
                case KING:
                    System.out.println("Player " + (currentPlayer + 1) + " played a King! The order of play is now reversed.");
                    direction *= -1;
                    currentPlayer = getNextPlayer(currentPlayer, direction);
                    break;
                case QUEEN:
                    System.out.println("Player " + (currentPlayer + 1) + " played a Queen! The next player will draw 2 cards.");
                    drawCards(currentPlayer, 2);
                    break;
                case JACK:
                    System.out.println("Player " + (currentPlayer + 1) + " played a Jack! The next player will draw 4 cards.");
                    drawCards(currentPlayer, 4);
                    break;
            }

            // Add card to discard pile
            discardPile.addCard(cardToPlay);
            System.out.println("Player " + (currentPlayer + 1) + " played " + cardToPlay);
        } else {
            // Player must draw a card
            Card drawnCard = deck.deal();
            if (drawnCard == null) {
                // Deck is empty, game ends in a draw
                System.out.println("The deck is empty! The game ends in a draw.");
                gameEnd = true;
            } else {
                System.out.println("Player " + (currentPlayer + 1) + " drew a card: " + drawnCard);
                hands[currentPlayer].addCard(drawnCard);
            }
        }

       // Check if player has won
for (Player player : players) {
    if (player.getHandSize() == 0) {
        System.out.println("Player " + player.getId() + " wins!");
        return;
    }
}

// If draw pile is empty, end the game in a draw
if (drawPile.isEmpty()) {
    System.out.println("The game ended in a draw.");
    return;
}

// If no player can play a card, draw a card for the next player
if (!playableCardExists) {
    System.out.println("No playable card exists. Drawing a card for player " + currentPlayer.getId() + "...");
    currentPlayer.drawCard(drawPile);
}

// Switch to the next player
currentPlayer = getNextPlayer(currentPlayer);
}