package client.network;

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
            ResponseEnvelope<UniquePlayerIdentifier>
        }

    }
}
