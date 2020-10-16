package by.bsuir.spolks.command;

import by.bsuir.spolks.command.impl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {

    private static final CommandProvider instance =new CommandProvider();
    private final Map<CommandName, Command> repository = new HashMap<>();

    private final NoSuchCommand noSuchCommand=new NoSuchCommand();

    private CommandProvider(){
        repository.put(CommandName.ECHO, new EchoCommand());
        repository.put(CommandName.TIME, new TimeCommand());
        repository.put(CommandName.DOWNLOAD, new DownloadCommand());
        repository.put(CommandName.DOWNLOAD_CONTINUE, new DownloadContinueCommand());
        repository.put(CommandName.UPLOAD, new UploadCommand());
        repository.put(CommandName.UPLOAD_CONTINUE, new UploadContinueCommand());
    }

    public static CommandProvider getInstance(){
        return instance;
    }

    public Command getCommand(String name){
        CommandName commandName;
        Command command;

        try {
            commandName=CommandName.valueOf(name.toUpperCase());
            command=repository.get(commandName);
        }catch (IllegalArgumentException | NullPointerException e){
            command=noSuchCommand;
        }

        return command;
    }
}
