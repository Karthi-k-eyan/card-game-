# card-game-

This is a simple implementation of a card game in Java. The game is played by multiple players, and each player starts with a hand of five cards. The game uses a standard 52-card deck, and the objective is to get rid of all cards in your hand by playing them onto a discard pile.

The game has several special cards that have different effects, such as skipping the next player's turn, reversing the order of play, or making the next player draw cards.

The game logic is implemented in the Game class, which keeps track of the players, the deck, the top card on the discard pile, the current player, and the direction of play (clockwise or counterclockwise). The play method is the main game loop, which continues until one player has emptied their hand.

Each player is represented by the Player class, which has a hand of cards and methods for adding and removing cards from the hand.

The Deck class represents the deck of cards, which is shuffled at the beginning of the game and used to draw new cards as needed. The deck is also used as a discard pile to hold cards that have been played.

The Card class represents a single playing card, with a suit and a rank.

Overall, this implementation provides a basic framework for a card game that could be expanded and customized as desired. 
