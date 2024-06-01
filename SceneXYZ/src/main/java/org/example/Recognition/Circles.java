package org.example.Recognition;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

// Класс для поиска окружностей на изображении
// Поля - список окружностей
// Методы - найти окружности на изображении, нарисовать квадратный контур на изображении
public class Circles {

    /**
     * Список окружностей
     */
    private List<Circle> ListCircles = new ArrayList<Circle>();

    /**
     * Получить список окружностей
     * @return - список
     */
    public List<Circle> getListCircles() {
        return ListCircles;
    }

    // Поиск всех окружностей на изображении
    // Параметры - черно-белое изображение, параметры распознования из графического интерфейса
    public boolean FindCircles(Mat ImgGray, RecognitionParameters parameters)
    {
        boolean Res = false;

        Mat workImg = ImgGray;

        if (workImg.empty())
            return false;

        Mat circles = new Mat();

        //==============================================================================================================
        // Находит круги на изображении в градациях серого с помощью преобразования Хафа. Функция находит круги на
        // изображении в градациях серого, используя модификацию преобразования Хафа. Пример: :
        // INCLUDE: snippets/imgproc_HoughLinesCircles.cpp
        // Примечание: Обычно функция хорошо определяет центры кругов. Однако он может не найти правильные радиусы.
        // Вы можете помочь функции, указав диапазон радиуса ( minRadius и maxRadius ), если вы его знаете.
        // Или, в случае метода #HOUGH_GRADIENT, вы можете установить maxRadius в отрицательное число, чтобы возвращать
        // центры только без поиска радиуса и найти правильный радиус с помощью дополнительной процедуры. Это также
        // помогает немного сгладить изображение, если оно уже не мягкое. Например, может помочь GaussianBlur() с ядром
        // 7x7 и сигмой 1,5x1,5 или подобным размытием.
        //
        // Параметры:
        //     image — 8-битное одноканальное входное изображение в градациях серого.
        //     круги - Выходной вектор найденных кругов. Каждый вектор кодируется как 3-х или 4-х элементный
        //     вектор с плавающей запятой (x, y, радиус).
        //
        // или (x,y,радиус,голоса)
        //     .
        //     method — метод обнаружения, см. #HoughModes. Доступны следующие методы:
        //     #HOUGH_GRADIENT и #HOUGH_GRADIENT_ALT.
        //
        //     dp - Обратное отношение разрешения аккумулятора к разрешению изображения.
        //     Например, если dp=1, аккумулятор имеет то же разрешение, что и входное изображение. Если dp=2,
        //     аккумулятор имеет половину ширины и высоты. Для #HOUGH_GRADIENT_ALT рекомендуемое значение dp=1,5,
        //     если только не нужно обнаруживать очень маленькие круги.
        //
        //     minDist - Минимальное расстояние между центрами обнаруженных кругов. Если параметр слишком
        //     мал, в дополнение к истинному может быть ложно обнаружено несколько соседних кругов.
        //     Если он слишком большой, некоторые круги могут быть пропущены.
        //
        //     param1 - Первый параметр, специфичный для метода. В случае #HOUGH_GRADIENT и #HOUGH_GRADIENT_ALT
        //     это более высокий порог из двух, переданных детектору ребер Канни (нижний в два раза меньше).
        //     Обратите внимание, что #HOUGH_GRADIENT_ALT использует алгоритм #Scharr для вычисления производных
        //     изображений, поэтому пороговое значение обычно должно быть выше, например 300 или нормально
        //     экспонированные и контрастные изображения.
        //
        //     param2 - Второй параметр, специфичный для метода. В случае #HOUGH_GRADIENT это порог накопителя для
        //     центров кругов на этапе обнаружения. Чем он меньше, тем больше ложных кругов может быть обнаружено.
        //     Кружки, соответствующие большим значениям аккумулятора, будут возвращены первыми. В случае алгоритма
        //     #HOUGH_GRADIENT_ALT это мера "совершенства" круга. Чем он ближе к 1, тем лучше выбирает алгоритм кругов.
        //     В большинстве случаев 0,9 должно быть достаточно. Если вы хотите улучшить обнаружение маленьких кругов,
        //     вы можете уменьшить его до 0,85, 0,8 или даже меньше. Но также постарайтесь ограничить диапазон поиска
        //     [minRadius, maxRadius], чтобы избежать множества ложных кругов.
        //
        //     minRadius - Минимальный радиус круга.
        //
        //     maxRadius - Максимальный радиус окружности. Если <= 0, используется максимальный размер изображения.
        //     Если < 0, #HOUGH_GRADIENT возвращает центры без нахождения радиуса. #HOUGH_GRADIENT_ALT всегда вычисляет
        //     радиусы кругов. ПОСМОТРЕТЬ: fitEllipse, minEnclosingCircle
        //
        Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT, parameters.getDp(), workImg.rows() / parameters.getDenominatorOfMinDist(),
                parameters.getParam1(), parameters.getParam2(), parameters.getMinRadius(), parameters.getMaxRadius());
        //==============================================================================================================


        for (int i = 0, r = circles.rows(); i < r; i++) {
            for (int j = 0, c = circles.cols(); j < c; j++) {
                double[] circle = circles.get(i, j);

                ListCircles.add(new Circle (circle[0], circle[1], circle[2]));

            }
        }

        if (ListCircles.size() > 0)
            Res = true;

        return Res;
    }

    // Рисование квадратов вокруг найденных окружностей
    // Параметры - оригинальное изображение
    public boolean DrawCircles(Mat photo)
    {
        boolean Res = false;

        Mat workImg = photo;

        if (workImg.empty())
            return false;

        Scalar purple = new Scalar(255, 0, 0);

        if (ListCircles.size() > 0) {
            for (int i = 0; i < ListCircles.size(); i++) {

                Circle temp = new Circle(ListCircles.get(i));

                double[] rect = {temp.getX() - temp.getRadius(), temp.getY() - temp.getRadius(), temp.getX() + temp.getRadius(), temp.getY() + temp.getRadius()};

                Imgproc.rectangle(workImg, new Point(rect[0], rect[1]),
                        new Point(rect[2], rect[3]), purple, 5);
            }
            Res = true;
        }

        return Res;
    }

    // Поиск окружностей по крайним пикселям нужного цвета
    public boolean findCirclesForMaxMinPixels(Mat im2)
    {
        boolean res = false;
        int i, j, x1, x2, y1, y2, x, y, RadiusX, RadiusY;
        double[] c1 = new double[3];
        // координаты прямоугольника, ограничивающего найденный круг
        x1 = im2.cols() + 1;
        x2 = -1;
        y1 = im2.rows() + 1;
        y2 = -1;
        // ищем мин и макс координаты белых точек
        for (i = 0; i < im2.cols(); i++)
            for (j = 0; j < im2.rows(); j++)
            {
                c1 = im2.get(j, i);
                if (c1[0] == 255) // белая точка
                {
                    if (i < x1) { x1 = i; }
                    if (i > x2) { x2 = i; }
                    if (j < y1) { y1 = j; }
                    if (j > y2) { y2 = j; }
                }
            }

        // если нашли
        if ((x1 < im2.cols()) && (x2 > 0) && (y1 < im2.rows()) && (y2 > 0)) {
            x = (x1 + x2) / 2;
            y = (y1 + y2) / 2;
            RadiusX = (x2 - x1) / 2;
            RadiusY = (y2 - y1) / 2;
            ListCircles.add(new Circle (x, y, RadiusX));
            res = true;
        }

        return res;
    }

    // Метод для работы с классом из вне (объединяет методы, написанные выше)
    // Параметры - (черно-белое изображение), (изображение, на котором рисуются контуры квадрата), (параметры распознования окружностей)
    public boolean FindAndDrawCircles(Mat WightBlackImg, Mat DrawImg, RecognitionParameters parameters)
    {
        boolean Res = false;

        if (WightBlackImg.empty() || DrawImg.empty())
            return false;

        //if (FindCircles(WightBlackImg, parameters))
        if (findCirclesForMaxMinPixels(WightBlackImg))
        {

//            bubbleSort();
//            deleteAloneCircles(120);
//            filterCircles(120);

            //countAverageRadiusCircles(150);
            //selectMaxRadius();

            if (DrawCircles(DrawImg))
            {
                Res = true;
            }
        }

        return Res;
    }

    /**
     * Фильтр окружностей
     * @param radius - радиус фильтруемый
     */
    public void filterCircles(int radius)
    {
        if (ListCircles.size() > 0)
        {
            List<Circle> newListCircles = new ArrayList<Circle>();

            for (int i = 0; i < ListCircles.size(); i++)
            {
                if (!newListCircles.contains(ListCircles.get(i)))
                {
                    if (newListCircles.size() == 0)
                        newListCircles.add(ListCircles.get(i));
                    else
                    {
                        boolean flagNear = false;

                        for (int j = 0; j < newListCircles.size(); j++)
                        {
                            if (ListCircles.get(i).isNear(newListCircles.get(j), radius))
                            {
                                flagNear = true;
                            }
                        }

                        if (flagNear == false)
                        {
                            newListCircles.add(ListCircles.get(i));
                        }
                    }
                }
            }
            ListCircles.clear();
            ListCircles = newListCircles;
        }
    }

    /**
     * Сортировка пузырьком
     */
    public void bubbleSort(){
        if (ListCircles.size() > 0) {
            for (int i = 0; i < ListCircles.size() - 1; i++) {
                for (int j = 0; j < ListCircles.size() - i - 1; j++) {
                    if (ListCircles.get(j + 1).getRadius() > ListCircles.get(j).getRadius()) {
                        Circle swap = ListCircles.get(j);
                        ListCircles.set(j, ListCircles.get(j + 1));
                        ListCircles.set(j + 1, swap);
                    }
                }
            }
        }
    }

    /**
     * Выбрать максимальный размер
     */
    public void selectMaxRadius()
    {
        if (ListCircles.size() > 0) {
            Circle max = ListCircles.get(0);
            for (int i = 0; i < ListCircles.size(); i++)
            {
                if (max.getRadius() < ListCircles.get(i).getRadius())
                    max = ListCircles.get(i);
            }
            ListCircles.clear();
            ListCircles.add(max);
        }
    }

    /**
     * Посчитать и выбрать средний радиус
     * @param radius - радиус
     */
    public void countAverageRadiusCircles(int radius)
    {
        if (ListCircles.size() > 0)
        {
            List<Circle> newListCircles = new ArrayList<Circle>();

            for (int i = 0; i < ListCircles.size(); i++)
            {
                if (!newListCircles.contains(ListCircles.get(i)))
                {
                    if (newListCircles.size() == 0)
                        newListCircles.add(ListCircles.get(i));
                    else
                    {
                        boolean flagNear = false;

                        for (int j = 0; j < newListCircles.size(); j++)
                        {
                            if (ListCircles.get(i).isNear(newListCircles.get(j), radius))
                            {
                                Circle average = new Circle();
                                flagNear = true;
                                average.setX((ListCircles.get(i).getX() + newListCircles.get(j).getX()) / 2);
                                average.setY((ListCircles.get(i).getY() + newListCircles.get(j).getY()) / 2);
                                average.setRadius((ListCircles.get(i).getRadius() + newListCircles.get(j).getRadius()) / 2);

                                newListCircles.get(j).SetCircle(average);
                            }
                        }

                        if (flagNear == false)
                        {
                            newListCircles.add(ListCircles.get(i));
                        }
                    }
                }
            }

            ListCircles = newListCircles;
        }
    }

    /**
     * Удалить одинокие окружности
     * @param radius - радиус
     */
    public void deleteAloneCircles(int radius)
    {
        if (ListCircles.size() > 0)
        {
            for (int i = 0; i < ListCircles.size(); i++)
            {
                boolean flagAlone = false;
                for (int j = i; j < ListCircles.size(); j++)
                {
                    if (ListCircles.get(i).isNear(ListCircles.get(j), radius) && !ListCircles.get(i).isZero())
                    {
                        flagAlone = true;
                    }
                }
                if (flagAlone == false)
                {
                    ListCircles.set(i, new Circle(0,0,0));
                    //ListCircles.remove(i);
                }
            }

            bubbleSort();
            for (int i = ListCircles.size() - 1; i >= 0; i--)
            {
                if (ListCircles.get(i).isZero())
                    ListCircles.remove(i);
            }
        }
    }
}
