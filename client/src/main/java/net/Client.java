package net;

import ui.Gameplay;
import ui.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Client {

    private ServerFacade server;
    private State state = State.SIGNEDOUT;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


    public Client() {
        server = new ServerFacade(8080);
    }

    public void eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
                case "createGame" -> createGame(params);
                case "listGames" -> listGames(params);
                case "playGame" -> playGame(params);
                case "observeGame" -> observeGame(params);
                case "quit" -> quit();
                default -> help();
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }

    public void register(String...params) {
        if(params.length >= 1) {
            server.register(params[0], params[1], params[2]);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Registered successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public void login(String...params) {
        if(params.length >= 1) {
            state = State.SIGNEDIN;
            server.login(params[1], params[2]);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Logged in successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public void logout(String...params) {
        if(params.length >= 1) {
            state = State.SIGNEDOUT;
            server.logout(params[1]);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Logged out successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public void createGame(String...params) {
        if(params.length >= 1) {
            server.createGame(params[1],params[2]);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Created game successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public void listGames(String...params) {
        if(params.length >= 1) {
            server.listGames(params[1]);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Listed games successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public void playGame(String...params) {
        if(params.length >= 1) {
            server.joinGame(params[1]);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Joined game successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public void observeGame(String...params) {
        if(params.length >= 1) {
            Gameplay.main();
        }
    }

    private void printHelpQuit(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("quit ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- playing chess");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("help ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- with possible commands");
        out.println();
    }

    public void help() {
        if(state == State.SIGNEDIN) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("create <NAME> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("list ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- games");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("join <ID> [WHITE|BLACK] ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("observe <ID> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("logout ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- when you are done");
            out.println();
            printHelpQuit(out);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("register <USERNAME> <PASSWORD> <EMAIL> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to create an account");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("login <USERNAME> <PASSWORD> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to play chess");
            out.println();
            printHelpQuit(out);
        }
    }

    public void quit() {
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("Quit successfully");
    }
}
