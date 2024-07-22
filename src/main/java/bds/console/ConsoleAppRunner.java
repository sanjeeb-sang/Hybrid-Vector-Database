package bds.console;

import bds.query.CalciteQueryProcessor;
import java.util.*;

/**
 * A console app that creates a Command Line interface that allows users to execute queries using Calcite
 * Connection without repeatedly having to manually change code.
 */
public class ConsoleAppRunner {

    private final static String SEPARATOR = "########################################################################"
            + "###################################################################";
    private final static int STATE_RUNNING = 11;
    private final static int STATE_QUIT = 12;
    private int currentState = STATE_RUNNING;
    private CalciteQueryProcessor queryProcessor;
    private ArrayList<String> history = new ArrayList<>();
    private HashMap<String, String> storageMap = new HashMap<>();

    /**
     * Method to start the ConsoleAppRunner instance.
     */
    public void start() {

        initStorageMap();

        println("");
        println(SEPARATOR);
        println("$ Welcome to Calcite Project.");
        println(SEPARATOR);
        println("");

        queryProcessor = new CalciteQueryProcessor();

        while (currentState == STATE_RUNNING) {
            try {
                String command = showPromptAndGetForCommand();
                command = command.trim();
                if (!command.equalsIgnoreCase(":history")) {
                    history.add(command);
                }

                if (command.startsWith(":")) {
                    handleUserCommand(command);
                } else {
                    handleQueryCommand(command);
                }

                println("");
                println(SEPARATOR);
                println("");
            } catch (Exception ex) {
                println("ERROR");
                println(ex.getMessage());
            }
        }
    }

    /**
     * Method to show User prompt and get user's input.
     * @return user's input string.
     */
    private String showPromptAndGetForCommand() {
        println("$ Enter Your Query or your commands. Enter :info-all to see view all commands. ");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Method to handle user Commands. User commands are user inputs that start with ':'
     * @param command the command to execute
     */
    private void handleUserCommand(String command) {
        println("> USER Command > " + command + "\n");
        String[] commandParts = command.toLowerCase().split(" ");
        String firstPart = command.toLowerCase();
        for (String part : commandParts) {
            if (!part.trim().isEmpty()) {
                firstPart = part.trim();
                break;
            }
        }
        switch (firstPart) {
            case ":quit", ":exit":
                println("Quitting Application!");
                currentState = STATE_QUIT;
                break;
            case ":show-history":
                printHistoryText();
                break;
            case ":show-tables":
                printTables();
                break;
            case ":show-functions":
                printAllFunctions();
                break;
            case ":clear-history":
                history.clear();
                System.out.println("History has been cleared successfully.");
                break;
            case ":info-all", ":all":
                printAllCommandsText();
                break;
            case ":help":
                println(getHelpText());
                break;
            case ":demo-queries":
                println(getDemoQueries());
                break;
            case ":set-storage":
                processStoreCommand(command, ":set-storage");
                break;
            case ":clear-storage":
                clearStorageMap();
                break;
            case ":show-storage":
                showStorageMap();
                break;
            default:
                println("  Unknown User Command: " + firstPart);
        }
    }

    /**
     * Method to handle user's query.
     * @param query the query to execute.
     */
    private void handleQueryCommand(String query) {
        if (query.contains("@")) {
            for (Map.Entry<String, String> entry : storageMap.entrySet()) {
                query = query.replaceAll(entry.getKey(), entry.getValue());
            }
        }

        println("> Executing SQL using Calcite > " + query + "\n");
        query = query.trim();
        try {
            if (query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update")) {
                println("INSERT/UPDATE");
                int result = queryProcessor.executeUpdate(query);
                println("INSERT/UPDATE Result: " + result);
            }
            queryProcessor.executeQuery(query);
        }
        catch (Exception ex) {
            println("ERROR");
            println(ex.getMessage());
        }
    }

    /**
     * Method to print all available tables.
     */
    private void printTables() {
        String allTables = """
                All Tables:
                1. hr.employees  ->  Employees table. Contains id, firstname, lastname, email, age, and vec.
                """;
        System.out.println(allTables);
    }

    /**
     * Method to process store command.
     * @param command the command to process.
     * @param shortcut the first part of the command.
     */
    private void processStoreCommand(String command, String shortcut) {
        String storeText = command.substring(shortcut.length()).trim();
        String[] storeParts = storeText.split("=");
        String name = storeParts[0].trim();
        String value = storeParts[1].trim().replace("\"", "'");
        if (name.startsWith("@")) {
            storageMap.put(name, value);
        }
    }

    /**
     * Method to clear the existing storage map.
     */
    private void clearStorageMap() {
        storageMap.clear();
        System.out.println("Storage Cleared.");
        initStorageMap();
    }

    /**
     * Method to show the current storage map.
     */
    private void showStorageMap() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Map.Entry<String, String> entry : storageMap.entrySet()) {
            sb.append("  ").append(index).append(". ")
                    .append(entry.getKey()).append(" : ")
                    .append(entry.getValue())
                    .append("\n");
        }

        System.out.println(sb);
    }

    /**
     * Method to print all the available Functions.
     */
    private void printAllFunctions() {
        String allTables = """
                All Functions:
                1. hr.SUM_ELEMENTS 
                2. hr.SIMILARITY
                3. hr.COSINE_SIMILARITY 
                """;
        System.out.println(allTables);
    }

    /**
     * Method to return all Demo Queries.
     * @return string containing demo queries that user can execute.
     */
    private String getDemoQueries() {
        return """
                Look at HybridDatabase1/Queries.txt
                """;
    }

    /**
     * Method to print all commands that user can execute.
     * Commands are user inputs that start with ':'.
     */
    private void printAllCommandsText() {
        String allCommands = """
                All available commands are listed below:
                
                Calcite Query Related:
                :show-tables --------------->  Print all available Tables.
                :show-functions ------------>  Print all available Tables.
                :set-storage ---------------> Storage a variable value pair for later use.
                :clear-storage -------------> Clear all variable values.
                :show-storage --------------> View all variable values currently stored.
                
                User Commands::
                :quit --------------> Quit or exit the application.
                :exit --------------> Exit the application.
                :help --------------> Show help prompt.
                :info-all
                :all
                :show-history ----------->  Show commands history.
                :clear-history ----->  Clear history.
                """;

        System.out.println(allCommands);
    }

    /**
     * Method to print user input history.
     */
    private void printHistoryText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            sb.append("    ").append("> ").append(history.get(i)).append("\n");
        }
        System.out.println(sb);
    }

    /**
     * Method to initialize Storage Map.
     */
    private void initStorageMap() {
        storageMap.put("@vecd1", "ARRAY[1.01, 2.002, 3.45, 4.67]");
        storageMap.put("@vecd2", "ARRAY[2.01, 4.002, 3.45, 6.67]");
        storageMap.put("@vecd3", "ARRAY[3.01, 5.002, 7.45, 9.67]");
    }

    /**
     * Method to return the help text that will be displayed to user.
     * @return help text string.
     */
    private String getHelpText() {
        String help = """
                Welcome to Help
                If you want to Execute a Query, then type that Query and Press Enter.
                Otherwise, you may want to use a User options, which are commands provided to users. They start with ':'.
                If you want to quit the application type ':quit'.
                If you want to see all available options, type ':show-options'.
                """;
        return help;
    }

    /**
     * Method to print a line to Console.
     * @param message the message to print.
     */
    private void println(String message) {
        System.out.println(message);
    }
}
