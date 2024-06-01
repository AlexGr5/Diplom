package org.example.Recognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class Mark {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Version OpenCV: " + Core.VERSION);
    }

    public static void main(String[] args) {

        ColorParams orange = new ColorParams(false, 225, 99, 59, 50);
        ColorParams yellow = new ColorParams(false, 206, 226, 50, 51);
        ColorParams yellow1 = new ColorParams(false, 255, 255, 140, 41);
        ColorParams yellow2 = new ColorParams(false, 165, 165, 70, 31);

        // Режим для просмотра видео и отладки
        //***********
        useVideo(orange, 0);
        //***********


        // Режим для работы
        //ColorParams colorParams = new ColorParams(false, 255, 100, 85, 50);
        //System.err.println("XYZ = " + Arrays.toString(getXYZ(5, orange, 0)));
        //System.err.println("XYZ = " + Arrays.toString(getXYZ(5, yellow2, 0)));
    }

    public static double[] getXYZ(int countFrames, ColorParams colorParams, int cameraIndex) {

//        JFrame window = new JFrame();
//        JLabel screen = new JLabel();
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.setVisible(true);

        VideoCapture videoCapture = new VideoCapture(cameraIndex);

        // Установка разрешения камеры на 1080p
        videoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1920);
        videoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 1080);

        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        ImageIcon ic;

        MatImgAndXYZRadius currentFrame = new MatImgAndXYZRadius();
        int i = 0;
        double averageRadius = 0;
        double averageX = 0;
        double averageY = 0;
        double height = 0;
        double width = 0;

        while (videoCapture.grab() && i < countFrames + 3)
        {
            videoCapture.read(frame);

            if (i == 0)
            {
                height = frame.height();
                width = frame.width();
            }

            // На первых трех кадрах камера выставляет баланс белого
            //if (i > 2) {
                currentFrame.setImg(frame);

                currentFrame = recognImg(currentFrame, colorParams);

                if (i > 2 && currentFrame.isRecogn == true) {
                    averageRadius += currentFrame.getAverageRadius();
                    averageX += currentFrame.getAverageX();
                    averageY += currentFrame.getAverageY();
                    //System.out.println(currentFrame.getAverageX() + " " + currentFrame.getAverageY() + " " + currentFrame.getAverageRadius());
                }
//                Imgcodecs.imencode(".png", currentFrame.getImg(), buf);

//                ic = new ImageIcon(buf.toArray());
//            screen.setIcon(ic);
//            window.setContentPane(screen);
//            window.pack();
                currentFrame.clearAll();
                //System.out.println(i);
            //}
            i++;
        }

        //System.err.println("Average Radius = " + averageRadius / countFrames);

        videoCapture.release();
//        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

        //System.out.println(averageX + " " + averageY + " " + averageRadius);
        if (averageRadius == 0)
            return new double[] {0, 0, 0};
        return countXYZ(new double[] {averageX / countFrames, averageY / countFrames, averageRadius / countFrames}, height, width);
    }

    public static MatImgAndXYZRadius recognImg(MatImgAndXYZRadius img, ColorParams colorParams) {
        Mat result = img.getImg();

        //Mat img = Imgcodecs.imread("D:/altstu/robot/projects/TESTS/RecognXYZ_v1/src/main/resources/testImg.jpg");

        frame frame1 = new frame(result);

        RecognitionParameters recognitionParameters = new RecognitionParameters(1, 10, 100, 15, 10, 150);

        // 255, 156, 136
        //ColorParams colorParams = new ColorParams(false, 255, 100, 85, 50);

        frame1.WorkWithFrame(recognitionParameters, colorParams);
        if (frame1.isRecognised())
        {
            result = frame1.GetRecognized();
            //frame1.WriteRecognised("D:\\temp\\recogn.jpg");
            img.isRecogn = true;
        }
        else {
            img.isRecogn = false;
        }

        img.setImg(result);

        for (int i = 0; i < frame1.getCircles().getListCircles().size(); i++) {
            System.out.print(frame1.getCircles().getListCircles().get(i));
            double Z = countZWithRadius(frame1.getCircles().getListCircles().get(i).getRadius(), img.getImg().width());
            System.out.println(" Z = " + Z);
            img.setX(frame1.getCircles().getListCircles().get(i).getX());
            img.setY(frame1.getCircles().getListCircles().get(i).getY());
            img.setZ(Z);
            img.setRadius(frame1.getCircles().getListCircles().get(i).getRadius());
        }

        return img;
    }

    public static double[] countXYZ(double[] XYRaduis, double heightOrig, double widthOrig)
    {
        double[] XYZ = new double[3];

        double width = widthOrig, height = heightOrig;

        double middleWidth = width / 2, middleHeight = height / 2;

        double newX = XYRaduis[0] - middleWidth, newY = middleHeight - XYRaduis[1];

        // ОПРЕДЕЛЕНИЕ РАССТОЯНИЯ ДО ОБЪЕКТА ПО ОТНОШЕНИЮ
        // номера чисел в формуле
        //  1 - фиксированное значение размера матрицы по ширине в мм
        //  2 - диаметр шара в реальности в мм
        //  3 - ПОДОБРАЛ значение, чтобы расстояние до объекта совпадало с реальным расстоянием до шара
        //  4 - разрешение камеры по ширине в пикселях
        //  5 - диаметр распознанного шара в пикселях
        double newZ = countZWithRadius(XYRaduis[2], widthOrig);

        newZ = adjustingZForViewingAngle(newX, newY, newZ);

        XYZ[0] = (int) newX;
        XYZ[1] = (int) newY;
        XYZ[2] = (int) newZ;

        return XYZ;
    }

    // Рассчитать расстояние до объекта по формуле
    public static double countZWithRadius(double radius, double width)
    {
        // ОПРЕДЕЛЕНИЕ РАССТОЯНИЯ ДО ОБЪЕКТА ПО ОТНОШЕНИЮ
        // номера чисел в формуле
        //  1 - фиксированное значение размера матрицы по ширине в мм
        //  2 - диаметр шара в реальности в мм
        //  3 - ПОДОБРАЛ значение, чтобы расстояние до объекта совпадало с реальным расстоянием до шара
        //  4 - разрешение камеры по ширине в пикселях
        //  5 - диаметр распознанного шара в пикселях
        //return (5.1 * 40) / (5.0 / 640 * radius * 2);
        //return (5.3 * 40) / (3.15 / width * radius * 2);

        return /*(int)*/ ((6.17 * 40) / (11.55 / width * radius * 2));
        //return (6.17 * 40) / (12.7 / width * radius * 2);
    }

    // Поправка Z с учетом угла обзора конкретной моей камеры HIKVISION
    public static double adjustingZForViewingAngle(double x, double y, double z)
    {
        double resultZ = z;

        int oneSmInPixel = 0;

        int otklonenieVSantimetrahOtCentraPoX = 0;

        int delta = 0;

        if (z >= 200 && z < 400)
        {
            oneSmInPixel = 32;
        }
        else
        {
            if (z >= 400 && z < 600)
            {
                oneSmInPixel = 20;
            }
            else
            {
                if (z >= 600 && z < 800)
                {
                    oneSmInPixel = 15;
                }
                else
                {
                    if (z >= 800)
                    {
                        oneSmInPixel = 11;
                    }
                }
            }
        }

        if (oneSmInPixel > 0)
        {
            otklonenieVSantimetrahOtCentraPoX = Math.abs((int)x) / oneSmInPixel;

            if (otklonenieVSantimetrahOtCentraPoX >= 5 && otklonenieVSantimetrahOtCentraPoX < 15)
            {
                delta = 10;
            }
            else
            {
                if (otklonenieVSantimetrahOtCentraPoX >= 15 && otklonenieVSantimetrahOtCentraPoX < 25)
                {
                    delta = 50;
                }
                else
                {
                    if (otklonenieVSantimetrahOtCentraPoX >= 25 && otklonenieVSantimetrahOtCentraPoX < 35)
                    {
                        delta = 100;
                    }
                    else
                    {
                        if (otklonenieVSantimetrahOtCentraPoX >= 35 && otklonenieVSantimetrahOtCentraPoX < 45)
                        {
                            delta = 120;
                        }
                        else
                        {
                            if (otklonenieVSantimetrahOtCentraPoX >= 45)
                            {
                                delta = 140;
                            }
                        }
                    }
                }
            }

            resultZ += delta;
        }

        return resultZ;
    }







    //***************************************************************************
    public static void useVideo(ColorParams colorParams, int cameraIndex) {
        JFrame window = new JFrame();
        JLabel screen = new JLabel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        VideoCapture videoCapture = new VideoCapture(cameraIndex);

        // Установка разрешения камеры на 1080p
        videoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1920);
        videoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 1080);

        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        ImageIcon ic;

        while (videoCapture.grab())
        {
            videoCapture.read(frame);

            //System.out.println(frame.height() + " " + frame.width());

            frame = recognImgVideo(frame, colorParams);

            Imgcodecs.imencode(".png", frame, buf);

            ic = new ImageIcon(buf.toArray());
            screen.setIcon(ic);
            window.setContentPane(screen);
            window.pack();
        }

        videoCapture.release();
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    public static Mat recognImgVideo(Mat img, ColorParams colorParams) {
        Mat result = img;

        //Mat img = Imgcodecs.imread("D:/altstu/robot/projects/TESTS/RecognXYZ_v1/src/main/resources/testImg.jpg");

        frame frame1 = new frame(img);

        RecognitionParameters recognitionParameters = new RecognitionParameters(1, 10, 100, 15, 10, 150);

        // 255, 156, 136
        //ColorParams colorParams = new ColorParams(false, 255, 100, 85, 50);

        frame1.WorkWithFrame(recognitionParameters, colorParams);
        if (frame1.isRecognised())
        {
            result = frame1.GetRecognized();
            frame1.WriteWhiteBlack("D:\\temp\\white.jpg");
        }

        for (int i = 0; i < frame1.getCircles().getListCircles().size(); i++) {
            System.err.println("XYZ = " + Arrays.toString(countXYZ(new double[] {frame1.getCircles().getListCircles().get(i).getX(),
                    frame1.getCircles().getListCircles().get(i).getY(),
                    frame1.getCircles().getListCircles().get(i).getRadius()}, img.height(), img.width())));
            //System.out.print(frame1.getCircles().getListCircles().get(i));

            // ОПРЕДЕЛЕНИЕ РАССТОЯНИЯ ДО ОБЪЕКТА ПО ОТНОШЕНИЮ
            // номера чисел в формуле
            //  1 - фиксированное значение размера матрицы по ширине в мм
            //  2 - диаметр шара в реальности в мм
            //  3 - ПОДОБРАЛ значение, чтобы расстояние до объекта совпадало с реальным расстоянием до шара
            //  4 - разрешение камеры по ширине в пикселях
            //  5 - диаметр распознанного шара в пикселях
            //System.out.println(" Z = " + (5.1 * 32) / (5.2 / 640 * frame1.getCircles().getListCircles().get(i).getRadius() * 2));
        }

        return result;
    }
    //***************************************************************************
}