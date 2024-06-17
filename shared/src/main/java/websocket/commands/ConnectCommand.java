package websocket.commands;
import org.eclipse.jetty.websocket.api.Session;

public class ConnectCommand extends UserGameCommand{
    int id;
    Session session;
    String auth;

    ConnectCommand(Session session, int id, String auth) {
        super(CommandType.CONNECT);
        this.id = id;
        this.session = session;
        this.auth = auth;
    }
}
