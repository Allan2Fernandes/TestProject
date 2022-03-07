
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application {
    Number currentMemory;
    Number currentCpUsage;
    int counter;

    @Override
    public void start(Stage stage) throws Exception {
        counter = 0;


        Group root = new Group();
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Usage");
        stage.show();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Memory usage");
        
        /*
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Memory usage");
        series.getData().add(new XYChart.Data<>(1, 2));
        chart.getData().add(series);

         */

        ObservableList list = FXCollections.observableArrayList();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        list.add(series);


        series.getData().add(new XYChart.Data<>(1, 10));


        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Memory usage");
        chart.setData(list);

        root.getChildren().add(chart);



        list.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change change) {
                chart.setData(list);
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                accessResources();

                series.getData().add(new XYChart.Data<>(++counter, currentMemory));
            }
        }, 1, 1000);

    }

    private void accessResources()
    {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Freemem: " + runtime.freeMemory());
        System.out.println("Avail Processors: " + runtime.availableProcessors());

        System.out.println("memory: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
        System.out.println("cpu: " + ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());

        //links
        //https://stackoverflow.com/questions/47177/how-do-i-monitor-the-computers-cpu-memory-and-disk-usage-in-java
        //https://stackoverflow.com/questions/74674/how-to-do-i-check-cpu-and-memory-usage-in-java
        //http://knight76.blogspot.dk/2009/05/how-to-get-java-cpu-usage-jvm-instance.html
        currentMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        currentCpUsage = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }
}
