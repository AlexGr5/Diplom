package org.example.Recognition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Класс дирриктория (каталог)
// Поля - путь до каталога
// Методы - создать каталог, указать каталог в поле "путь"
public class Directory {

    // Путь до каталога
    private String Path;

    public Directory()
    {
        Path = new String();
    }
    public Directory(String path)
    {
        Path = new String(path);
    }

    public boolean CreateDir()
    {
        boolean Res = false;

        Path pathScene = Paths.get(Path);

        if (!Files.exists(pathScene)) {
            try {
                Files.createDirectory(pathScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Res = true;
            System.out.println("New Directory created !   " + Path);


        } else {
            Res = false;
            System.out.println("Directory already exists");
        }

        return Res;
    }

}
