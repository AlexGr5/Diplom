package org.example.Recognition;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Класс для работы с файлами в каталогах
// Поля - путь до каталога,
public class FilesAndDirs {

    // Путь до каталога
    protected String DirPath;

    // Тип (расширение) нужных файлов
    protected String TypeOfNeedFiles;

    // Список всех файлов в выбранном каталоге
    protected ArrayList<String> ListOfAllFiles;

    // Список нужных файлов в каталоге
    protected ArrayList<String> ListOfNeededFiles;

    // Список "кадров"
    volatile protected List<frame> ListOfFrames = new ArrayList<frame>();

    // Параметры распознования
    protected RecognitionParameters paramsRecogn;

    // Параметры распознования цвета
    protected ColorParams colorParams;

    // Ссылка на графическое окно
    volatile protected MyTask myTask;

    // Установка ссылки на графическое окно
    public void SetMyTask(MyTask myTask1)
    {
        myTask = myTask1;
    }

    // Получение размера списка "кадров"
    public int GetSizeFramesList()
    {
        if(ListOfFrames.size() > 0)
            return ListOfFrames.size();

        else return 0;
    }

    // Получить кадр из списка кадров по индексу
    public frame GetFrameInListIndex(int index)
    {
        if(ListOfFrames.size() > 0 && index > 0 && index < ListOfFrames.size())
            return ListOfFrames.get(index);

        else return null;
    }

    // Установить параметры распознования
    public void SetParamsRecognized(RecognitionParameters parameters)
    {
        paramsRecogn = new RecognitionParameters(parameters.getDp(), parameters.getDenominatorOfMinDist(),
                parameters.getParam1(), parameters.getParam2(),parameters.getMinRadius(),parameters.getMaxRadius());
    }

    // Установить параметры цвета
    public void SetColorParams(ColorParams NewColorParams)
    {
        colorParams = new ColorParams(NewColorParams.isISAutomaticColor(), NewColorParams.getRed(),
                NewColorParams.getGreen(), NewColorParams.getBlue(), NewColorParams.getDelta());
    }

    // Установить путь до каталога
    public void SetDirPath(String path)
    {
        DirPath = new String(path);
    }

    // Получить путь до каталога
    public String GetDirPath()
    {
        return new String(DirPath);
    }

    // Получение списка всех файлов в каталоге
    public ArrayList<String> FilesInDirToList(String path) {

        ArrayList<String> ArrListOfFiles = new ArrayList<String>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                ArrListOfFiles.add(file.getName());
            }
        }

        ListOfAllFiles = (ArrayList<String>)ArrListOfFiles.clone();

        return ArrListOfFiles;
    }

    // Получение списка нужных файлов из списка со всеми файлами
    // Парметры - список с файлами, тип нужных файлов
    public ArrayList<String> SelectTypeFilesOfListFiles (ArrayList<String> ArrList, String typeOfFile)
    {
        ArrayList<String> ArrType = new ArrayList<String>();

        TypeOfNeedFiles = new String(typeOfFile);

        for (int i = 0; i < ArrList.size(); i++)
        {
            if (ArrList.get(i).length() >= typeOfFile.length()) {
                if (ArrList.get(i).regionMatches(ArrList.get(i).length() - typeOfFile.length(), typeOfFile, 0, typeOfFile.length())) {
                    ArrType.add(ArrList.get(i));
                }
            }
        }

        ListOfNeededFiles = (ArrayList<String>)ArrType.clone();

        return ArrType;
    }

    // Создание рабочих каталогов в выбранном каталоге
    // Параметры - выбранный каталог
    public boolean CreateWorkingDirs(String MainPath)
    {
        boolean Res = false;

        String SuccessfulName = "/Successful";
        String UnsuccessfulName = "/Unsuccessful";
        String TempName = "/Temp";
        String SceneName = "/Scene";

        Directory dirSuccessful = new Directory(MainPath + SuccessfulName);
        Directory dirUnsuccessful = new Directory(MainPath + UnsuccessfulName);
        Directory dirTemp = new Directory(MainPath + TempName);
        
        if(dirSuccessful.CreateDir())
            if(dirUnsuccessful.CreateDir())
                if(dirTemp.CreateDir())
                    Res = true;

        return Res;
    }


    // Сохраниение "кадра" (всех стадий распознования) по рабочим каталогам
    // Параметры - индекс "кадра" в списке кадров
    public void SaveFrame(int index)
    {
        StringBuffer SBName = new StringBuffer(ListOfNeededFiles.get(index));

        StringBuffer NameTempImg = new StringBuffer(SBName);
        NameTempImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_temp");

        StringBuffer NameMainColorImg = new StringBuffer(SBName);
        NameMainColorImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_MC");

        StringBuffer NameWightBlackImg = new StringBuffer(SBName);
        NameWightBlackImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_WB");

        StringBuffer NameRecognImg = new StringBuffer(SBName);
        NameRecognImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_Recogn");

        ListOfFrames.get(index).WriteTemp(DirPath + "/Temp/" + NameTempImg);
        ListOfFrames.get(index).WriteMainColor(DirPath + "/Temp/" + NameMainColorImg);
        ListOfFrames.get(index).WriteWhiteBlack(DirPath + "/Temp/" + NameWightBlackImg);

        if (ListOfFrames.get(index).GetIsRecognized()) {
            ListOfFrames.get(index).WriteRecognised(DirPath + "/Successful/" + NameRecognImg);
        }
        else {
            ListOfFrames.get(index).WriteOriginal(DirPath + "/Unsuccessful/" + ListOfNeededFiles.get(index));
        }
    }

    // Запуск распознования
    public boolean ImageListProcessing()
    {
        boolean Res = false;

        if (DirPath.length() > 0)
        {
            if (ListOfNeededFiles.size() > 0)
            {
                Res = true;

                LoadProcessingRecognizeSendToDisplaySave();
            }
        }

        return Res;
    }

    // Распознование всех нужных изображений, вывод стадий в графическое окно и сохранение в рабочие каталоги
    private void LoadProcessingRecognizeSendToDisplaySave() {
        for (int i = 0; i < ListOfNeededFiles.size(); i++)
        {
            ListOfFrames.add(new frame(DirPath + "/" + ListOfNeededFiles.get(i)));

            // = new frame(DirPath, ListOfNeededFiles.get(i));

            ListOfFrames.get(i).WorkWithFrame(paramsRecogn, colorParams);

            if (ListOfFrames.get(i).GetIsRecognized())
            {
                System.out.println("Img " + DirPath + "/" + ListOfNeededFiles.get(i) + " is recognized!");
            }
            else {

                System.out.println("Img " + DirPath + "/" + ListOfNeededFiles.get(i) + " is not recognized!");
            }

            //myTask.updateProgress((long)i,(long)ListOfNeededFiles.size());

            SaveFrame(i);
        }
    }


    // Метод работы с классом (объединение работы основных методов)
    // Параметры - путь до каталога с файлами, тип файлов, параметры распознования, параметры цвета
    public void MainProcessVariable2(String Path, String TypeOfFiles, RecognitionParameters parameters, ColorParams NewColorParams)
    {
        SetParamsRecognized(parameters);
        SetColorParams(NewColorParams);
        SetDirPath(Path);
        SelectTypeFilesOfListFiles(FilesInDirToList(Path), ".jpg");
        CreateWorkingDirs(Path);

        ImageListProcessing();

        System.out.println("\n\nRecognizing end");

    }



}
