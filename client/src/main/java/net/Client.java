package net;

import ui.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Client {

    private ServerFacade server;
    private String serverURL;
    private State state = State.SIGNEDOUT;


    public Client(String serverURL) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
                case "createGame" -> createGame(params);
                case "listGames" -> listGames(params);
                case "playGame" -> playGame(params);
                case "observeGame" -> observeGame(params);
                case "quit" -> quit(PrintStream out);
                default -> help(PrintStream out);
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String register(String...params) {

    }

    public String login(String...params) {
        if(params.length >= 1) {
            state = State.SIGNEDIN;

        }
    }

    public String logout(String...params) {

    }

    public String createGame(String...params) {


    }

    public String listGames(String...params) {

    }

    public String playGame(String...params) {


    }

    public String observeGame(String...params) {

    }

    private void printHelpQuit(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("quit ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- playing chess");
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("help ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- with possible commands");
    }

    public void help(PrintStream out) {
        if(state == State.SIGNEDIN) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("create <NAME> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("list ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- games");
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("join <ID> [WHITE|BLACK] ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("observe <ID> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("logout ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- when you are done");
            printHelpQuit(out);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("register <USERNAME> <PASSWORD> <EMAIL> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to create an account");
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("login <USERNAME> <PASSWORD> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to play chess");
            printHelpQuit(out);
        }

    }

    public String quit() {

    }
}
