package net;

import ui.ServerFacade;

public class Client {

    private ServerFacade server;
    private String serverURL;
    private State state = State.SIGNEDOUT;


//    public Client(String serverURL) {
//        server = new ServerFacade(serverURL);
//        this.serverURL = serverURL;
//    }

//    public String eval(String input) {
//        try {
//            var tokens = input.toLowerCase().split(" ");
//            var cmd = (tokens.length > 0) ? tokens[0] : "help";
//            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
//            return switch (cmd) {
//                case "register" -> register(params);
//                case "login" -> login(params);
//                case "logout" -> logout(params);
//                case "createGame" -> createGame(params);
//                case "listGames" -> listGames(params);
//                case "playGame" -> playGame(params);
//                case "observeGame" -> observeGame(params);
//                case "quit" -> quit();
//                default -> help();
//            };
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//    }
//
//    public String register(String...params) {
//
//    }
//
//    public String login(String...params) {
//        if(params.length >= 1) {
//            state = State.SIGNEDIN;
//
//        }
//    }
//
//    public String logout(String...params) {
//
//    }
//
//    public String createGame(String...params) {
//
//
//    }
//
//    public String listGames(String...params) {
//
//    }
//
//    public String playGame(String...params) {
//
//
//    }
//
//    public String observeGame(String...params) {
//
//    }
//
//    public String help() {
//        if(state == State.SIGNEDIN) {
//            return """
//                   create <NAME> - a game
//                   list - games
//                   join <ID> [WHITE|BLACK] - a game
//                   observe <ID> - a game
//                   logout - when you are done
//                   quit - playing chess
//                   help - with possible commands
//                   """;
//        } else {
//            return """
//                   register <USERNAME> <PASSWORD> <EMAIL> - to create an account
//                   login <USERNAME> <PASSWORD> - to play chess
//                   quit - playing chess
//                   help - with possible commands
//                   """;
//        }
//
//    }
//
//    public String quit() {
//
//    }
}
