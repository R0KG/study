package client.network;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import messagesbase.*;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.*;

public class NetworkClient {
    private final  WebClient webClient;

    public NetworkClient(String serverUrl) {
        this.webClient = WebClient.builder().baseUrl(serverUrl)
                .defaultHeader("Content-Type", "application/xml")
                .defaultHeader("Accept", "application/xml").build();

    }

    public UniquePlayerIdentifier registerPlayer(String gameID, PlayerRegistration registration) {
        try {
            ResponseEnvelope<UniquePlayerIdentifier> response = webClient.post()
                    .uri("/" + gameID + "/players")
                    .bodyValue(registration)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<UniquePlayerIdentifier>>() {})
                    .block();

            if (response != null && response.getState() == ERequestState.Okay){
                return response.getData().orElseThrow(() -> new RuntimeException("No player ID returned!"));
            }
            else {
                throw new RuntimeException("Error registering player " + response.getExceptionMessage());
            }
        }catch (WebClientResponseException e){
            throw new RuntimeException("Error registering player " + e.getMessage());
        }

    }

    public void sendHalfMap (String gameId, PlayerHalfMap halfMap) {
        try{
            ResponseEnvelope<Void> response = webClient.post()
                    .uri("/" + gameId + "/halfmaps")
                    .bodyValue(halfMap)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<Void>>() {})
                    .block();

            if (response != null && response.getState() == ERequestState.Okay){
                throw new RuntimeException("Error sending half map " + response.getExceptionMessage());
            }
            System.out.println("Half-map sent successfully!");
        }catch (WebClientResponseException e){
            throw new RuntimeException("Error sending half map " + e.getMessage());
        }
    }

    public GameState getGameState (String gameId, String playerId) {
        try{
            ResponseEnvelope<GameState> response = webClient.get()
                    .uri("/" + gameId + "/states/" + playerId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<GameState>>() {
                    })
                    .block();

            if (response != null && response.getState() == ERequestState.Okay) {
                return response.getData().orElseThrow(() -> new RuntimeException("No game state returned!"));
            } else {
                throw new RuntimeException("Error retrieving game state: " + response.getExceptionMessage());
            }
        } catch (WebClientResponseException e) {
            throw new RuntimeException("HTTP error during game state polling: " + e.getMessage());
        }
    }

    public void sendMove(String gameId, PlayerMove playerMove) {
        try {
            ResponseEnvelope<Void> response = webClient.post()
                    .uri("/" + gameId + "/moves")
                    .bodyValue(playerMove)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<Void>> () {})
                    .block();

            if (response != null && response.getState() != ERequestState.Okay) {
                throw new RuntimeException("Error sending move: " + response.getExceptionMessage());
            }

            System.out.println("Move sent successfully!");
        } catch (WebClientResponseException e) {
            throw new RuntimeException("HTTP error during move submission: " + e.getMessage());
        }
    }
}
