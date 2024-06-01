package org.example.Recognition;


// Класс графического окна
// Поля - ссылка на контрллер графичкского окна, "кадры", путь до каталога, праметры рспознавания и цвета
// Методы - задание контроллера окна, установка кадров, установка каталога,
public class MyTask {


    // Кадры
    volatile private FilesAndDirs Frames;

    // Путь до каталога
    private String Path;

    // Параметры распознования
    private RecognitionParameters parameters;

    // Параметры цвета
    private ColorParams colorParams;


    public void SetFrames(FilesAndDirs Frames)
    {
        if (Frames != null)
            this.Frames = Frames;
    }

    public void SetPath(String path)
    {
        Path = path;
    }





    // Возвращение параметров рапознования
    public RecognitionParameters getParameters() {
        return parameters;
    }

    // Установка параметров распознования
    public void setParameters(RecognitionParameters parameters) {
        this.parameters = parameters;
    }

    // Установка параметров цвета
    public void SetColorParams(ColorParams newColorParams){colorParams = newColorParams;}
}
