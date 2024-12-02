//    package client.util;
//
//
//
//    import messagesbase.UniquePlayerIdentifier;
//    import messagesbase.messagesfromclient.ETerrain;
//    import messagesbase.messagesfromclient.PlayerHalfMap;
//    import messagesbase.messagesfromclient.PlayerHalfMapNode;
//
//    import java.util.ArrayList;
//    import java.util.List;
//    import java.util.Random;
//
//
//    public class HalfMapGenerator {
//        public PlayerHalfMap generateHalfMap(String playerId) {
//            List<PlayerHalfMapNode> halfMapNodes = new ArrayList<PlayerHalfMapNode>();
//
//            for (int i = 0; i < 50; i++){
//                int x = i % 10;
//                int y = i / 10;
//
//                ETerrain terrain = getRandomTerrain();
//
//                halfMapNodes.add(new PlayerHalfMapNode(x,y,false,terrain));
//            }
//            placeFort(halfMapNodes);
//            validateMap(halfMapNodes);
//
//            return new PlayerHalfMap(playerId,halfMapNodes);
//        }
//
//
//        private void placeFort(List<PlayerHalfMapNode> halfMapNodes) {
//            Random random = new Random();
//
//            while (true){
//                int index = random.nextInt(halfMapNodes.size());
//                PlayerHalfMapNode node = halfMapNodes.get(index);
//                if(node.getTerrain().equals(ETerrain.Grass) && !node.isFortPresent()){
//                    PlayerHalfMapNode temp = new PlayerHalfMapNode(
//                            node.getX(),
//                            node.getY(),
//                            true,
//                            node.getTerrain()
//                    );
//                    halfMapNodes.set(index, temp);
//                    break;
//                }
//            }
//        }
//
//        private ETerrain getRandomTerrain(){
//            double random = Math.random();
//
//            if (random < 0.7){
//                return ETerrain.Grass;
//            } else if (random < 0.9) {
//                return ETerrain.Water;
//            }
//            else {
//                return ETerrain.Mountain;
//            }
//        }
//        private void validateMap(List<PlayerHalfMapNode> nodes) {
//
//            if (nodes.size() != 50) {
//                throw new IllegalStateException("Half-map must contain exactly 50 fields.");
//            }
//
//
//            long fortCount = nodes.stream().filter(PlayerHalfMapNode::isFortPresent).count();
//            if (fortCount != 1) {
//                throw new IllegalStateException("Half-map must contain exactly one fort.");
//            }
//
//
//            long grassCount = nodes.stream().filter(node -> node.getTerrain().equals(ETerrain.Grass)).count();
//            if (grassCount < 35) {
//                throw new IllegalStateException("Half-map must have at least 70% Grass fields.");
//            }
//        }
//    }

//package client.util;
//
//import messagesbase.UniquePlayerIdentifier;
//import messagesbase.messagesfromclient.ETerrain;
//import messagesbase.messagesfromclient.PlayerHalfMap;
//import messagesbase.messagesfromclient.PlayerHalfMapNode;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class HalfMapGenerator {
//    private static final int WIDTH = 10;
//    private static final int HEIGHT = 5;
//
//    public PlayerHalfMap generateHalfMap(String playerId) {
//        List<PlayerHalfMapNode> halfMapNodes = new ArrayList<>();
//
//        // Generate terrain
//        for (int i = 0; i < 50; i++) {
//            int x = i % WIDTH;
//            int y = i / WIDTH;
//            ETerrain terrain = getRandomTerrain();
//            halfMapNodes.add(new PlayerHalfMapNode(x, y, false, terrain));
//        }
//
//        // Place the fort
//        placeFort(halfMapNodes);
//
//        // Ensure connectivity
//        ensureConnectivity(halfMapNodes);
//
//        // Validate the map
//        validateMap(halfMapNodes);
//
//        return new PlayerHalfMap(playerId, halfMapNodes);
//    }
//
//    private void placeFort(List<PlayerHalfMapNode> halfMapNodes) {
//        Random random = new Random();
//        while (true) {
//            int index = random.nextInt(halfMapNodes.size());
//            PlayerHalfMapNode node = halfMapNodes.get(index);
//            if (node.getTerrain() == ETerrain.Grass && !node.isFortPresent()) {
//                PlayerHalfMapNode fortNode = new PlayerHalfMapNode(
//                        node.getX(), node.getY(), true, node.getTerrain()
//                );
//                halfMapNodes.set(index, fortNode);
//                break;
//            }
//        }
//    }
//
//    private ETerrain getRandomTerrain() {
//        double random = Math.random();
//        if (random < 0.7) {
//            return ETerrain.Grass;
//        } else if (random < 0.9) {
//            return ETerrain.Water;
//        } else {
//            return ETerrain.Mountain;
//        }
//    }
//
//    private void ensureConnectivity(List<PlayerHalfMapNode> nodes) {
//        boolean[][] visited = new boolean[WIDTH][HEIGHT];
//        floodFill(nodes, 0, 0, visited);
//
//        for (PlayerHalfMapNode node : nodes) {
//            if (node.getTerrain() == ETerrain.Grass && !visited[node.getX()][node.getY()]) {
//                throw new IllegalStateException("Map contains disconnected Grass fields.");
//            }
//        }
//    }
//
//    private void floodFill(List<PlayerHalfMapNode> nodes, int x, int y, boolean[][] visited) {
//        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT || visited[x][y]) return;
//
//        PlayerHalfMapNode node = nodes.stream()
//                .filter(n -> n.getX() == x && n.getY() == y)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("Node not found"));
//
//        if (node.getTerrain() != ETerrain.Grass) return;
//
//        visited[x][y] = true;
//
//        floodFill(nodes, x + 1, y, visited);
//        floodFill(nodes, x - 1, y, visited);
//        floodFill(nodes, x, y + 1, visited);
//        floodFill(nodes, x, y - 1, visited);
//    }
//
//    private void validateMap(List<PlayerHalfMapNode> nodes) {
//        if (nodes.size() != 50) {
//            throw new IllegalStateException("Half-map must contain exactly 50 fields.");
//        }
//
//        long fortCount = nodes.stream().filter(PlayerHalfMapNode::isFortPresent).count();
//        if (fortCount != 1) {
//            throw new IllegalStateException("Half-map must contain exactly one fort.");
//        }
//
//        long grassCount = nodes.stream().filter(node -> node.getTerrain() == ETerrain.Grass).count();
//        if (grassCount < 35) {
//            throw new IllegalStateException("Half-map must have at least 70% Grass fields.");
//        }
//    }
//}


package client.util;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HalfMapGenerator {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 5;

    public PlayerHalfMap generateHalfMap(String playerId) {
        List<PlayerHalfMapNode> halfMapNodes = new ArrayList<>();

        for(int i = 0; i < 50; i++) {
            int x = i % WIDTH;
            int y = i / WIDTH;
            ETerrain terrain = getRandomTerrain();
            halfMapNodes.add(new PlayerHalfMapNode(x, y,false, terrain));
        }
        placeFort(halfMapNodes);

    }

    private ETerrain getRandomTerrain() {
        double random = Math.random();

        if (random < 0.7) {
            return ETerrain.Grass;
        } else if (random < 0.9) {
            return ETerrain.Water;
        }
        else{
            return ETerrain.Mountain;
        }

    }

    private void placeFort(List<PlayerHalfMapNode> halfMapNodes) {
        Random random = new Random();
        while (true) {
            int index = random.nextInt(halfMapNodes.size());
            PlayerHalfMapNode node = halfMapNodes.get(index);

            if (node.getTerrain() == ETerrain.Grass && !node.isFortPresent()) {
                PlayerHalfMapNode fortNode = new PlayerHalfMapNode(
                        node.getX(), node.getY(), true, node.getTerrain()
                );
                halfMapNodes.set(index, fortNode); // Replace with fort node
                break;
            }
        }
    }

    private void ensureConnectivity(List<PlayerHalfMapNode> halfMapNodes) {
        boolean [][] visited = new boolean[halfMapNodes.size()][halfMapNodes.size()];
        floodFill(nodes,0,0,visited);

        for(PlayerHalfMapNode node : halfMapNodes) {
            if(node.getTerrain().equals(ETerrain.Grass) && !visited[node.getX()][node.getY()]) {
                throw new IllegalAccessException("Map contains disconnected Grass fields.");
            }
        }
    }

    private void floodFill(List<PlayerHalfMapNode> nodes, int x, int y, boolean[][] visited) {
        if (x < 0 || x >= )
    }






}
