package server;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String user, Session session) {
        var connection = new Connection(user, session);
        connections.put(user, connection);
    }

    public void remove(String user) {
        connections.remove(user);
    }

    public void broadcast(String user, ServerMessage mes, String message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var connection : connections.values()) {
            if(connection.session.isOpen()) {
                if(!connection.username.equals(user)) {
                    connection.send(message);
                }
            }
            else {
                removeList.add(connection);
            }
        }
        for (var connection : removeList) {
            connections.remove(connection.username);
        }
    }
}
