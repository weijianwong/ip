package bobby;

import bobby.command.Command;
import bobby.exceptions.BobbyException;
import bobby.parser.Parser;
import bobby.storage.Storage;
import bobby.tasklist.TaskList;
import bobby.ui.Ui;

/**
 * The {@code Bobby} class represents the main application for a task management program.
 * This class initializes the core components of the application, including the user interface (UI),
 * storage system, parser, and task list. It provides methods to run the application and handle user commands.
 * <p>
 * The main application loop continuously reads user input, interprets commands,
 * and executes the corresponding actions such as adding tasks, marking tasks as done, deleting tasks,
 * and saving tasks to a file. The program runs until the user issues an exit command.
 * </p>
 * <p>
 * This class serves as the entry point to the application, encapsulating the primary functionality
 * and workflow of the task manager.
 * </p>
 */
public class Bobby {

    private static final String FILE_PATH = "./src/main/data/Bobby.txt";
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Constructs a new {@code Bobby} object, initializing the user interface,
     * storage, parser, and task list. It also loads any previously saved tasks
     * from the specified storage file.
     */
    public Bobby() {
        this.ui = new Ui();
        this.storage = new Storage(FILE_PATH);
        this.parser = new Parser();
        this.tasks = new TaskList();
        tasks = storage.loadTasks();
    }

    /**
     * Runs the main application loop. It displays a greeting to the user,
     * continuously reads user input, parses it into commands, and executes
     * the corresponding actions until the exit command is issued.
     */
    public void run() {
        ui.showGreeting();
        boolean isRunning = true;
        while (isRunning) {
            String userInput = ui.readCommand();
            String response = processCommand(userInput);
            ui.showResponse(response);
            if (response.equals(ui.getExitMessage())) {
                isRunning = false;
            }
        }
    }

    /**
     * Processes a single command and returns the appropriate response.
     *
     * @param userInput The user's input command.
     * @return The response to be displayed to the user.
     */
    public String processCommand(String userInput) {
        try {
            Command userCommand = parser.parseUserCommand(userInput);
            return userCommand.execute(this.tasks, this.ui, this.storage);
        } catch (BobbyException e) {
            return ui.getErrorMessage(e.getMessage());
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return processCommand(input);
    }

    /**
     * Serves as the entry point of the application.
     * It creates a new instance of {@code Bobby} and runs the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Bobby().run();
    }

}
