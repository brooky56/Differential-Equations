package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static ArrayList<Double> arrayListX = new ArrayList<>();
    public static ArrayList<Double> arrayListY = new ArrayList<>();

    public static XYChart.Series EM = new XYChart.Series();
    public static XYChart.Series IEM = new XYChart.Series();
    public static XYChart.Series RKM = new XYChart.Series();

    public static int amountOut = 11;
    //public static int amountOut = 21;

    public static FileWriter writer;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Differential equations");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<>(x, y);
        numberLineChart.setTitle("Integrable curve");

        double a = 0.0, b = 1.0; //boards of interval
        double h = 0.1; //step
        int m = (int) ((b - a) / h); //separation times

        writer = new FileWriter("output.txt");

        EM(h, m, 0.0, 2.0);
        numberLineChart.getData().add(EM);
        arrayListX.clear();
        arrayListY.clear();
        writer.write("---------------------------------------------------------------------------------------------------\n");
        IEM(h, m, 0.0, 2.0);
        numberLineChart.getData().add(IEM);
        arrayListX.clear();
        arrayListY.clear();
        writer.write("---------------------------------------------------------------------------------------------------\n");
        RKM(h, m, 0.0, 2.0);
        numberLineChart.getData().add(RKM);


        writer.flush();
        writer.close();

        Scene scene = new Scene(numberLineChart, 1200, 720);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void EM(double h, int m, double initialX, double initialY) throws IOException {


        EM.setName("Euler Method");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();


        arrayListX.add(initialX);
        arrayListY.add(initialY);

        for (int i = 1; i < m + 1; i++) {
            arrayListX.add(myEulerX(h, arrayListX.get(i - 1)));
        }

        for (int i = 1; i < m + 1; i++) {
            arrayListY.add(myEulerY(h, arrayListX.get(i - 1), arrayListY.get(i - 1)));
        }


        writer.write("EM_X: " + arrayListX.toString() + "\n");
        writer.write("EM_Y: " + arrayListY.toString() + "\n");

        for (int i = 0; i < amountOut; i++) {
            datas.add(new XYChart.Data(arrayListX.get(i), arrayListY.get(i)));
        }

        EM.setData(datas);

    }

    public static void IEM(double h, int m, double initialX, double initialY) throws IOException {


        IEM.setName("Improved Euler Method");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();


        arrayListX.add(initialX);
        arrayListY.add(initialY);

        for (int i = 1; i < m + 1; i++) {
            arrayListX.add(myIEulerX(h, arrayListX.get(i - 1)));
        }

        for (int i = 1; i < m + 1; i++) {
            arrayListY.add(myIEulerY(h, arrayListX.get(i - 1), arrayListY.get(i - 1)));
        }

        writer.write("IEM_X: " + arrayListX.toString() + "\n");
        writer.write("IEM_Y: " + arrayListY.toString() + "\n");

        for (int i = 0; i < amountOut; i++) {
            datas.add(new XYChart.Data(arrayListX.get(i), arrayListY.get(i)));
        }

        IEM.setData(datas);

    }

    public static void RKM(double h, int m, double initialX, double initialY) throws IOException {


        RKM.setName("Runge-Kutta Method");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();


        arrayListX.add(initialX);
        arrayListY.add(initialY);

        for (int i = 1; i < m + 1; i++) {
            arrayListX.add(myRKM_X(h, arrayListX.get(i - 1)));
        }

        for (int i = 1; i < m + 1; i++) {
            arrayListY.add(myRKM_Y(h, arrayListX.get(i - 1), arrayListY.get(i - 1)));
        }


        writer.write("RKM_X: " + arrayListX.toString() + "\n");
        writer.write("RKM_Y: " + arrayListY.toString() + "\n");

        for (int i = 0; i < amountOut; i++) {
            datas.add(new XYChart.Data(arrayListX.get(i), arrayListY.get(i)));
        }

        RKM.setData(datas);

    }

    public static double myEulerX(double h, double xPre) {

        return xPre + h;
    }

    public static double myEulerY(double h, double xPre, double yPre) {

        return yPre + h * (myFunction(xPre, yPre));
    }

    public static double myIEulerX(double h, double xPre) {

        return xPre + h;
    }

    public static double myIEulerY(double h, double xPre, double yPre) {

        return yPre + h * (myFunction(xPre + h / 2, yPre + h / 2 * myFunction(xPre, yPre)));
    }

    public static double myRKM_X(double h, double xPre) {

        return xPre + h;
    }

    public static double myRKM_Y(double h, double xPre, double yPre) {
        double k1 = myFunction(xPre, yPre);
        double k2 = myFunction(xPre + h / 2, yPre + h * k1 / 2);
        double k3 = myFunction(xPre + h / 2, yPre + h * k2 / 2);
        double k4 = myFunction(xPre + h, yPre + h * k3);

        return yPre + h / 6 * (k1 + 2 * k2 + 2 * k3 + k4);
    }

    //change this function to others
    public static double myFunction(double x, double y) {

        return x * x - 3 * y - 3 * x * y + y * y;
    }


}
