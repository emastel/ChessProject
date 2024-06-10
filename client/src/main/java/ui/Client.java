package ui;

import com.google.gson.Gson;
import model.GameData;
import net.ServerFacade;
import net.State;
import reqrep.BlankResponse;
import reqrep.ListGamesResponse;
import reqrep.RegisterLoginResponse;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    public  Gson gson = new Gson();
    private  ServerFacade server;
    private  State state = State.SIGNEDOUT;
    private  PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private  boolean running = true;
    private  String authToken;
    GameData[] list;


    public Client() {
        server = new ServerFacade(8080);
    }

    public static void main(String[] args) {
        Client client = new Client();
        while (client.running) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            client.eval(input);
        }
    }

    public  void eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> quit();
                default -> help();
            }
        } catch (Exception e) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(e.getMessage());
            out.println();
        }
    }

    public  void register(String...params) {
        if(params.length >= 1) {
            RegisterLoginResponse res = server.register(params[0], params[1], params[2]);
            if(res.message() == null) {
                authToken = res.authToken();
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(res.message());
            }
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Registered successfully");
            state = State.SIGNEDIN;
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void login(String...params) {
        if(params.length >= 1) {
            state = State.SIGNEDIN;
            RegisterLoginResponse res = server.login(params[0], params[1]);
            if(res.message() == null) {
                authToken = res.authToken();
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(res.message());
            }
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Logged in successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void logout(String...params) {
        BlankResponse res = server.logout(authToken);
        if(res == null) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Logged out successfully");
            state = State.SIGNEDOUT;
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print(res.message());
        }
        out.println();
    }

    public  void createGame(String...params) {
        if(params.length >= 1) {
            Object res = server.createGame(params[0], authToken);
            if(res instanceof BlankResponse) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(((BlankResponse)res).message());
            }
            else {
                out.print(SET_TEXT_COLOR_GREEN);
                out.print("Created game successfully");
            }
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void listGames() {
        Object allGames = server.listGames(authToken);
        if(allGames instanceof ListGamesResponse) {
            list = ((ListGamesResponse)allGames).games();
            int i = 1;
            for(GameData game : list) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(i +". Game Name: ");
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(game.getGameName());
                out.println();
                out.print(SET_TEXT_COLOR_BLUE);
                out.print("White Player: ");
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(game.getWhiteUsername());
                out.println();
                out.print(SET_TEXT_COLOR_BLUE);
                out.print("Black Player: ");
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(game.getBlackUsername());
                out.println();
                ++i;
            }
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print(((BlankResponse)allGames).message());
        }
        out.println();
    }

    public  void playGame(String...params) {
        if(params.length >= 1) {
            int gameId = list[Integer.parseInt(params[1])-1].getGameID();
            BlankResponse res = server.joinGame(authToken, params[0], gameId);
            if(res == null) {
                Gameplay.main();
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(res.message());
            }
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void observeGame(String...params) {
        if(params.length >= 1) {
            Gameplay.main();
        }
    }

    private  void printHelpQuit(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("Quit ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- playing chess");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("Help ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- with possible commands");
        out.println();
    }

    public  void help() {
        if(state == State.SIGNEDIN) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("Create <NAME> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("List ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- games");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("Join [WHITE|BLACK] <ID> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("Observe <ID> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("Logout ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- when you are done");
            out.println();
            printHelpQuit(out);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("Register <USERNAME> <PASSWORD> <EMAIL> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to create an account");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("Login <USERNAME> <PASSWORD> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to play chess");
            out.println();
            printHelpQuit(out);
        }
    }

    public  void quit() {
        running = false;
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("Quit successfully");
    }
}
