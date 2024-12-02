package client.logic;

import client.network.NetworkClient;
import client.util.HalfMapGenerator;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;

public class GameLogic {
    private final NetworkClient networkClient;
    private final String gameId;
    private final String playerId;

    public GameLogic(String serverBaseUrl, String gameId, String playerId) {
        this.networkClient = new NetworkClient(serverBaseUrl);
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public void start() {
        try {
            // Step 1: Register Player
            PlayerRegistration registration = new PlayerRegistration("Roman", "Merkulov", "a11919434");
            UniquePlayerIdentifier uniquePlayerId = networkClient.registerPlayer(gameId, registration);
            System.out.println("Player registered with ID: " + uniquePlayerId.getUniquePlayerID());

            // Step 2: Submit Half-Map
            HalfMapGenerator generator = new HalfMapGenerator();
            networkClient.sendHalfMap(gameId, generator.generateHalfMap(uniquePlayerId.getUniquePlayerID()));
            System.out.println("Half-map submitted successfully!");

            // Step 3: Game Loop
            gameLoop(uniquePlayerId.getUniquePlayerID());
        } catch (Exception e) {
            System.err.println("An error occurred during game execution: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void gameLoop(String uniquePlayerId) {
        boolean gameActive = true;
        while (gameActive) {
            try {
                // Poll the game state
                GameState state = networkClient.getGameState(gameId, uniquePlayerId);

                // Check if the game is over
                if (state.getPlayers().stream().anyMatch(player -> player.getState().equals(EPlayerGameState.Won))) {
                    System.out.println("Game over!");
                    gameActive = false;
                    continue;
                }

                // Check if this player needs to act
                if (state.getPlayers().stream()
                        .anyMatch(player -> player.getUniquePlayerID().equals(uniquePlayerId) && player.getState().equals(EPlayerGameState.MustAct))) {
                    // Send a move (e.g., moving "Right")
                    PlayerMove move = PlayerMove.of(uniquePlayerId, EMove.Right);
                    networkClient.sendMove(gameId, move);
                    System.out.println("Move sent: Right");
                } else {
                    // If no action is required, wait briefly before checking again
                    System.out.println("Waiting for the other player...");
                }
            } catch (Exception e) {
                System.err.println("An error occurred during the game loop: " + e.getMessage());
                e.printStackTrace();
                gameActive = false;
            }
        }
    }
}
