package by.bsuir.spolks.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FileLoadMemory {

    private static final FileLoadMemory instance = new FileLoadMemory();

    private HashMap<String, CommandMemory> map = new HashMap<>();

    public static FileLoadMemory getInstance() {
        return instance;
    }
}
