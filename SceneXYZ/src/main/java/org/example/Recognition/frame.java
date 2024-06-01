package org.example.Recognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

// Класс для работы с "кадрами" (стадиями обработки изображения)
// Поля - оригинальное, временное (промежуточное), выделение основного цвета, черо-белое, распознанное изображения,
// список распознанных окружностей, дельта - разница в цвете (только для автоматического), результат распознования
public class frame {
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    // Оригинальное
    private ImgInFormatMat imgOriginal;

    // Промежуточное (повышенная насыщенность)
    private ImgInFormatMat imgTemp;

    // Выдделение основного цвета
    private ImgInFormatMat imgMainColor;

    // Черно-белое изображение (основной цвет -> черно-белое)
    private ImgInFormatMat imgWhiteBlack;

    // Распознонное (если распознались окружности)
    private ImgInFormatMat imgRecognized;

    // Список распознанных окружностей
    private Circles circles = new Circles();

    private int mainColor = 50;    // разница между красным каналом и остальными.
    //private int mainColor = 75;    // разница между красным каналом и остальными.
    // уменьшая его можно больше красных и близких к ним точек выделять
    // увеличивая его - всё более красные точки выделяются

    // Результат распознования
    private volatile boolean isRecognised;

    public boolean isRecognised() {
        return isRecognised;
    }

    // Получение информации о распознавании
    public boolean GetIsRecognized()
    {
        return isRecognised;
    }

    public frame()
    {
        imgOriginal = new ImgInFormatMat();
        imgTemp = new ImgInFormatMat();
        imgWhiteBlack = new ImgInFormatMat();
        imgMainColor = new ImgInFormatMat();
        imgRecognized = new ImgInFormatMat();
    }

    // Конструктор с параметром - путь до оригинального изображения
    public frame(String pathForOriginImg)
    {
        imgOriginal = new ImgInFormatMat();
        imgTemp = new ImgInFormatMat();
        imgWhiteBlack = new ImgInFormatMat();
        imgMainColor = new ImgInFormatMat();
        imgRecognized = new ImgInFormatMat();

        ReadImgOriginal(pathForOriginImg);
    }

    // Конструктор с параметром - изображение
    public frame(Mat img)
    {
        imgOriginal = new ImgInFormatMat(img);
        imgTemp = new ImgInFormatMat();
        imgWhiteBlack = new ImgInFormatMat();
        imgMainColor = new ImgInFormatMat();
        imgRecognized = new ImgInFormatMat();
    }


    // Получить каждое изображение отдельно
    public Mat GetOriginal()
    {
        return imgOriginal.GetImg();
    }
    public Mat GetTemp()
    {
        return imgTemp.GetImg();
    }
    public Mat GetMainColor()
    {
        return imgMainColor.GetImg();
    }
    public Mat GetWhiteBlack()
    {
        return imgWhiteBlack.GetImg();
    }
    public Mat GetRecognized()
    {
        return imgRecognized.GetImg();
    }

    public Circles getCircles() {
        return circles;
    }

    // Чтение оригинального изображения
    public boolean ReadImgOriginal(String path)
    {
        if(imgOriginal.ReadImg(path, "original"))
            return true;
        else return false;
    }

    // Запись оригинального изображения
    public void WriteOriginal(String newFullPath) {

        imgOriginal.WriteImg(newFullPath, "original");
    }

    // Запись изображения
    public void WriteMainColor(String newFullPath) {

        imgMainColor.WriteImg(newFullPath, "MainColor");
    }

    // Запись изображения
    public void WriteTemp(String newFullPath) {

        imgTemp.WriteImg(newFullPath, "temp");

    }

    // Запись изображения
    public void WriteWhiteBlack(String newFullPath) {

        imgWhiteBlack.WriteImg(newFullPath, "White and Black");

    }

    // Запись изображения
    public void WriteRecognised(String newFullPath) {

        imgRecognized.WriteImg(newFullPath, "original");

    }

    public void createTempImg() {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(imgOriginal.GetImg(), imgHSV, Imgproc.COLOR_BGR2HSV);
        Mat imgBGR = new Mat();
        Imgproc.cvtColor(imgHSV, imgBGR, Imgproc.COLOR_HSV2BGR);
        imgTemp.SetImg(imgBGR);
    }

    // Повышение насыщенности цветов изображения
    public void IncreasingSaturation() {

        if (imgOriginal.GetImg().empty())
        {
            ;
        }
        else {
            Mat imgHSV = new Mat();
            Imgproc.cvtColor(imgOriginal.GetImg(), imgHSV, Imgproc.COLOR_BGR2HSV);
            // Увеличение насыщенности
            Core.add(imgHSV, new Scalar(0, 40, 0), imgHSV);
            Mat imgBGR = new Mat();
            Imgproc.cvtColor(imgHSV, imgBGR, Imgproc.COLOR_HSV2BGR);
            imgTemp.SetImg(imgBGR);
        }
    }

    // Установка основного цвета
    public void SetMainColor(int color)
    {
        mainColor = color;
    }

    // Выделение основного цвета из оригинального (основной цвет по умолчанию)
    public void OriginalToMainColor()
    {

        Mat WorkImg = imgTemp.GetImg();

        if (WorkImg.empty())
        {
            ;
        }
        else {
            // Создаём новое пустое изображение, такого же размера
            imgMainColor.SetImg(new Mat(WorkImg.rows(), WorkImg.cols(), WorkImg.type()));

            int channels = WorkImg.channels();// Получить количество каналов изображения
            double[] pixel = new double[3];

            // Обработаем каждый пиксель исходного изображения
            for (int x = 0; x < WorkImg.rows(); x++) {          // цикл по ширине
                for (int y = 0; y < WorkImg.cols(); y++) {       // цикл по высоте

                    // Получаем цвет текущего пикселя
                    pixel = WorkImg.get(x, y).clone();

                    // Получаем красную, зелёную и синюю составляющую цвета
                    double blue = pixel[0];
                    double green = pixel[1];
                    double red = pixel[2];


                    // Если красного много - сделаем полносью красный
                    if ((red > (blue + mainColor)) && (red > (green + mainColor))) {
                        red = 0;
                        green = 0;
                        blue = 0;
                    }
                    // иначе пусть будет белый цвет (255,255,255) (черный - RGB=(0,0,0))
                    else {
                        red = 255;
                        green = 255;
                        blue = 255;
                    }


                    pixel[0] = blue;
                    pixel[1] = green;
                    pixel[2] = red;

                    // Установим этот цвет в пиксель нового изображения
                    imgMainColor.GetImg().put(x, y, pixel);
                }
            }
        }
    }


    // Выделение основного цвета из оригинального (основной цвет задан в графическом интерфейсе)
    // Параметры - параметры цвета распознавания
    public void OriginalToMainColorAlternative(ColorParams colorParams)
    {
        Mat WorkImg = imgTemp.GetImg();

        if (WorkImg.empty())
        {
            ;
        }
        else {
            // Создаём новое пустое изображение, такого же размера
            imgMainColor.SetImg(new Mat(WorkImg.rows(), WorkImg.cols(), WorkImg.type()));

            int channels = WorkImg.channels();// Получить количество каналов изображения
            double[] pixel = new double[3];

            // Обработаем каждый пиксель исходного изображения
            for (int x = 0; x < WorkImg.rows(); x++) {          // цикл по ширине
                for (int y = 0; y < WorkImg.cols(); y++) {       // цикл по высоте

                    // Получаем цвет текущего пикселя
                    pixel = WorkImg.get(x, y).clone();

                    // Получаем красную, зелёную и синюю составляющую цвета
                    double blue = pixel[0];
                    double green = pixel[1];
                    double red = pixel[2];


                    double maxValueColor;

                    maxValueColor = Math.max(Math.max(colorParams.getRed(), colorParams.getGreen()), colorParams.getBlue());

                    double mainChoiceColor = 0;
                    double addColor1 = 0;
                    double addColor2 = 0;

                    if(maxValueColor == colorParams.getRed()) {
                        mainChoiceColor = red;
                        addColor1 = green;
                        addColor2 = blue;
                    }
                    if(maxValueColor == colorParams.getGreen()) {
                        mainChoiceColor = green;
                        addColor1 = red;
                        addColor2 = blue;
                    }
                    if(maxValueColor == colorParams.getBlue()) {
                        mainChoiceColor = blue;
                        addColor1 = green;
                        addColor2 = red;
                    }

                    if ((red <= colorParams.getRed() + colorParams.getDelta()) && (red >= colorParams.getRed() - colorParams.getDelta()) &&
                        (green <= colorParams.getGreen() + colorParams.getDelta()) && (green >= colorParams.getGreen() - colorParams.getDelta()) &&
                        (blue <= colorParams.getBlue() + colorParams.getDelta()) && (blue >= colorParams.getBlue() - colorParams.getDelta())) {

                    // Если основного много - сделаем полносью красный
//                    if ((mainChoiceColor > (addColor1 + colorParams.getDelta())) &&
//                            (mainChoiceColor > (addColor2 + colorParams.getDelta()))){
                        red = 0;
                        green = 0;
                        blue = 0;
                    }
                    // иначе пусть будет белый цвет (255,255,255) (черный - RGB=(0,0,0))
                    else {
                        red = 255;
                        green = 255;
                        blue = 255;
                    }


                    pixel[0] = blue;
                    pixel[1] = green;
                    pixel[2] = red;


                    // Установим этот цвет в пиксель нового изображения
                    imgMainColor.GetImg().put(x, y, pixel);
                }
            }
        }
    }

    // Выделение основного цвета из оригинального (основной цвет задан в графическом интерфейсе)
    // Параметры - параметры цвета распознавания
    public void highlightСolor(ColorParams colorParams)
    {
        Mat WorkImg = imgTemp.GetImg();

        if (WorkImg.empty())
        {
            ;
        }
        else {
            // Создаём новое пустое изображение, такого же размера
            imgMainColor.SetImg(new Mat(WorkImg.rows(), WorkImg.cols(), WorkImg.type()));

            int channels = WorkImg.channels();// Получить количество каналов изображения
            double[] pixel = new double[3];

            // Обработаем каждый пиксель исходного изображения
            for (int x = 0; x < WorkImg.rows(); x++) {          // цикл по ширине
                for (int y = 0; y < WorkImg.cols(); y++) {       // цикл по высоте

                    // Получаем цвет текущего пикселя
                    pixel = WorkImg.get(x, y).clone();

                    // Получаем красную, зелёную и синюю составляющую цвета
                    double blue = pixel[0];
                    double green = pixel[1];
                    double red = pixel[2];


                    double maxValueColor;

                    maxValueColor = Math.max(Math.max(colorParams.getRed(), colorParams.getGreen()), colorParams.getBlue());


                    if ((red <= colorParams.getRed() + colorParams.getDelta()) && (red >= colorParams.getRed() - colorParams.getDelta()) &&
                            (green <= colorParams.getGreen() + colorParams.getDelta()) && (green >= colorParams.getGreen() - colorParams.getDelta()) &&
                            (blue <= colorParams.getBlue() + colorParams.getDelta()) && (blue >= colorParams.getBlue() - colorParams.getDelta()))
                    {
                        red = 255;
                        green = 255;
                        blue = 255;

                    }
                    // иначе пусть будет белый цвет (255,255,255) (черный - RGB=(0,0,0))
                    else {
                        red = 0;
                        green = 0;
                        blue = 0;
                    }


                    pixel[0] = blue;
                    pixel[1] = green;
                    pixel[2] = red;


                    // Установим этот цвет в пиксель нового изображения
                    imgMainColor.GetImg().put(x, y, pixel);
                }
            }
        }
    }

    /**
     * Фильтр основного цвета
     */
    public void filterMainColorImg()
    {
        int H1, W1, x1, x2, y1, y2, i, j, cW1, i1, j1;
        double[] pixel = new double[3];

        // определить прямоугольник, где белые точки
        // размеры окрестности точки, где будем считать кол-во белых точек
        H1 = 5;
        W1 = 5;
        // координаты прямоугольника, ограничивающего найденный круг
        x1 = imgMainColor.GetImg().cols() + 1;
        x2 = -1;
        y1 = imgMainColor.GetImg().rows() + 1;
        y2 = -1;
        // удаляем мусор (одиночные белые точки)
        for (i = 0; i < imgMainColor.GetImg().cols(); i+=H1)
            for (j = 0; j < imgMainColor.GetImg().rows(); j+=W1)
            {
                cW1 = 0;
                // посчитать в окрестности кол-во белых точек
                for (i1 = Math.max(0,i-H1); i1 < Math.min(imgMainColor.GetImg().cols(), i+H1); i1++)
                    for (j1 = Math.max(0,j-W1); j1 < Math.min(imgMainColor.GetImg().rows(), j+W1); j1++)
                    {
                        pixel = imgMainColor.GetImg().get(j1, i1);
                        if (pixel[2] == 255) cW1++;
                    }
                // если < 10% - то мусор
                if (cW1 < (H1*W1*4/10))
                {   // иначе удаляем мусор
                    for (i1 = Math.max(0, i - H1); i1 < Math.min(imgMainColor.GetImg().cols(), i + H1); i1++)
                        for (j1 = Math.max(0, j - W1); j1 < Math.min(imgMainColor.GetImg().rows(), j + W1); j1++)
                        {
                            pixel[0] = 0;
                            pixel[1] = 0;
                            pixel[2] = 0;
                            // Установим этот цвет в пиксель нового изображения
                            imgMainColor.GetImg().put(j1, i1, pixel);
                        }


                }
            }
    }


    // Основной цвет в черно-белый
    public void MainColorToGray()
    {
        if (imgMainColor.GetImg().empty()) {
            System.out.println("MainColor is empty");
        }
        else {
            Mat img2 = new Mat();
            Imgproc.cvtColor(imgMainColor.GetImg(), img2, Imgproc.COLOR_BGR2GRAY);
            Mat img3 = new Mat();
            double thresh = Imgproc.threshold(img2, img3, 100, 255,
                    Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            //System.out.println(thresh);
            imgWhiteBlack.SetImg(img3);
        }
    }


    // Отправка кадра на распознование окружностей и рисование квадратных контуров вокруг распознанных окружностей
    public boolean RecognizeAndDrawCircles(RecognitionParameters parameters)
    {
        boolean Res = false;

        if (imgWhiteBlack.GetImg().empty() || imgOriginal.GetImg().empty())
            return false;

        imgRecognized.SetImg(imgOriginal.GetImg().clone());
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (circles.FindAndDrawCircles(imgWhiteBlack.GetImg(), imgRecognized.GetImg(), parameters))
        //if (circles.FindAndDrawCircles(imgWhiteBlack.GetImg(), imgMainColor.GetImg(), parameters))
        {
            Res = true;
            isRecognised = true;
        }

        return Res;
    }

    // Работа с кадром (объединение основных методов класса)
    // Повышение насыщенности
    // Блюр изображения
    // Выделение основного цвета
    // Основной цвет -> черно-белый
    // Отпарвка на распозннование и рисование
    // Параметры - параметры распохнования, параметры основного цвета
    public synchronized void WorkWithFrame(RecognitionParameters parameters, ColorParams NewColorParams)
    {
        createTempImg();
        //IncreasingSaturation();
        //BlurRichImg();
        if(NewColorParams.isISAutomaticColor()) {
            OriginalToMainColor();
        }
        else {
            //OriginalToMainColorAlternative(NewColorParams);
            highlightСolor(NewColorParams);
        }
        filterMainColorImg();
        MainColorToGray();
        //BlurWightBlackImg();
        RecognizeAndDrawCircles(parameters);
    }

    // Блюр временного (повышенная насыщенность) изображения
    public void BlurRichImg()
    {
        Imgproc.blur(imgTemp.GetImg(), imgTemp.GetImg(), new Size(3, 3));
    }

    public void BlurWightBlackImg()
    {
        Imgproc.blur(imgWhiteBlack.GetImg(), imgWhiteBlack.GetImg(), new Size(3, 3));
    }

}
