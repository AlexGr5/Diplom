package org.example.Recognition;

// Класс параметры распознавания изображения
// Поля - разрешение сумматора, минимальное расстояние между центрами окружностей, параметр1, параметр2,
// минимальный и максимальный радиусы.
// Методы - задание и получение полей
public class RecognitionParameters {

    private double dp;

    private double denominatorOfMinDist;

    private double param1;

    private double param2;

    private int minRadius;

    private int maxRadius;

    public RecognitionParameters()
    {
        ;
    }

    /**
     *
     * @param DP    dp - Обратное отношение разрешения аккумулятора к разрешению изображения.
     *              Например, если dp=1, аккумулятор имеет то же разрешение, что и входное изображение. Если dp=2,
     *              аккумулятор имеет половину ширины и высоты. Для #HOUGH_GRADIENT_ALT рекомендуемое значение dp=1,5,
     *              если только не нужно обнаруживать очень маленькие круги.
     * @param DenominatorOfMinDist      minDist - Минимальное расстояние между центрами обнаруженных кругов. Если параметр слишком
     *                                  мал, в дополнение к истинному может быть ложно обнаружено несколько соседних кругов.
     *                                  Если он слишком большой, некоторые круги могут быть пропущены.
     * @param Param1    param1 - Первый параметр, специфичный для метода. В случае #HOUGH_GRADIENT и #HOUGH_GRADIENT_ALT
     *                  это более высокий порог из двух, переданных детектору ребер Канни (нижний в два раза меньше).
     *                  Обратите внимание, что #HOUGH_GRADIENT_ALT использует алгоритм #Scharr для вычисления производных
     *                  изображений, поэтому пороговое значение обычно должно быть выше, например 300 или нормально
     *                  экспонированные и контрастные изображения.
     * @param Param2    param2 - Второй параметр, специфичный для метода. В случае #HOUGH_GRADIENT это порог накопителя для
     *                  центров кругов на этапе обнаружения. Чем он меньше, тем больше ложных кругов может быть обнаружено.
     *                  Кружки, соответствующие большим значениям аккумулятора, будут возвращены первыми. В случае алгоритма
     *                  #HOUGH_GRADIENT_ALT это мера "совершенства" круга. Чем он ближе к 1, тем лучше выбирает алгоритм кругов.
     *                  В большинстве случаев 0,9 должно быть достаточно. Если вы хотите улучшить обнаружение маленьких кругов,
     *                  вы можете уменьшить его до 0,85, 0,8 или даже меньше. Но также постарайтесь ограничить диапазон поиска
     *                  [minRadius, maxRadius], чтобы избежать множества ложных кругов.
     * @param MinRadius     minRadius - Минимальный радиус круга.
     * @param MaxRadius     maxRadius - Максимальный радиус окружности. Если меньше либо равно 0, используется максимальный размер изображения.
     *                      Если меньше 0, #HOUGH_GRADIENT возвращает центры без нахождения радиуса. #HOUGH_GRADIENT_ALT всегда вычисляет
     *                      радиусы кругов. ПОСМОТРЕТЬ: fitEllipse, minEnclosingCircle
     */
    public RecognitionParameters(double DP, double DenominatorOfMinDist, double Param1, double Param2, int MinRadius, int MaxRadius)
    {
        setDp(DP);
        setDenominatorOfMinDist(DenominatorOfMinDist);
        setParam1(Param1);
        setParam2(Param2);
        setMinRadius(MinRadius);
        setMaxRadius(MaxRadius);
    }

    public void SetRecognitionParameters(double DP, double DenominatorOfMinDist, double Param1, double Param2, int MinRadius, int MaxRadius)
    {
        setDp(DP);
        setDenominatorOfMinDist(DenominatorOfMinDist);
        setParam1(Param1);
        setParam2(Param2);
        setMinRadius(MinRadius);
        setMaxRadius(MaxRadius);
    }


    public double getDp() {
        return dp;
    }

    public void setDp(double dp) {
        this.dp = dp;
    }

    public double getDenominatorOfMinDist() {
        return denominatorOfMinDist;
    }

    public void setDenominatorOfMinDist(double denominatorOfMinDist) {
        this.denominatorOfMinDist = denominatorOfMinDist;
    }

    public double getParam1() {
        return param1;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return param2;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
}
