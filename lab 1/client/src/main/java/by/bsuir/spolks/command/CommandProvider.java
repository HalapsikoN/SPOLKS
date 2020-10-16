package by.bsuir.spolks.command;

import by.bsuir.spolks.command.impl.DownloadCommand;
import by.bsuir.spolks.command.impl.EchoCommand;
import by.bsuir.spolks.command.impl.NoSuchCommand;
import by.bsuir.spolks.command.impl.TimeCommand;
import by.bsuir.spolks.command.impl.UploadCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {

    private static final CommandProvider instance = new CommandProvider();
    private final Map<CommandName, Command> repository = new HashMap<>();

    private final NoSuchCommand noSuchCommand = new NoSuchCommand();

    private CommandProvider() {
        repository.put(CommandName.ECHO, new EchoCommand());
        repository.put(CommandName.TIME, new TimeCommand());
        repository.put(CommandName.DOWNLOAD, new DownloadCommand());
        repository.put(CommandName.UPLOAD, new UploadCommand());
    }

    public static CommandProvider getInstance() {
        return instance;
    }

    public Command getCommand(String name) {
        CommandName commandName;
        Command command;

        try {
            commandName = CommandName.valueOf(name.toUpperCase());
            command = repository.get(commandName);
        } catch (IllegalArgumentException | NullPointerException e) {
            command = noSuchCommand;
        }

        return command;
    }
}