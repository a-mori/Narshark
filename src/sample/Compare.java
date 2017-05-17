package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jnetpcap.util.PcapPacketArrayList;
import sample.Filereading.PcapFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
*
* Alex Lilley, Alex Mori, Ethan Balzer
*
* */

public class Compare {
    private static HashMap<CheckBox, File> boxFiles = new HashMap<>();

    public void Compare(ArrayList<File> files) throws IOException {
        VBox vbox = new VBox();
        int index = 0;
        boxFiles.clear();

        for(File file: files){
            CheckBox checkBox = new CheckBox();
            checkBox.setId(file.toString());
            checkBox.setText(file.getName());
            checkBox.setSelected(true);
            vbox.getChildren().add(index,checkBox);
            boxFiles.put(checkBox, file);
            index += 1;
        }

        Button ok = new Button();
        ok.setText("Ok");
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Long maxPackets = 100000000L;
                Long maxValue = 0L;
                ArrayList<XYChart.Series> lines = new ArrayList<>();

                for(HashMap.Entry<CheckBox,File> file: boxFiles.entrySet()) {
                    if (file.getKey().isSelected()) {
                        PcapFile myfile = new PcapFile(file.getValue().toString());
                        PcapPacketArrayList mypackets = null;
                        try {
                            mypackets = myfile.readOfflineFiles();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        int index = 0;
                        ArrayList<Long> timeDifs = new ArrayList<>(FindTimeDiff.findTimeDif(mypackets));
                        timeDifs.set(0, 0L);
                        if(timeDifs.size()<maxPackets){
                            maxPackets = (long) timeDifs.size();
                        }
                        XYChart.Series series = new XYChart.Series();
                        series.setName(file.getValue().getName());
                        for (Long entry : timeDifs) {
                            if(entry>maxValue){
                                maxValue = entry;
                            }
                            index += 1;
                            series.getData().add(new XYChart.Data<>(index, entry));
                        }
                        lines.add(series);
                    }
                }

                NumberAxis xAxis = new NumberAxis(0, maxPackets, 10);
                xAxis.setLabel("Packet Num");

                NumberAxis yAxis = new NumberAxis(0, maxValue*1.1, maxValue/25);
                yAxis.setLabel("Time(MicroSeconds)");

                LineChart linechart = new LineChart(xAxis, yAxis);
                linechart.setTitle("Times in microseconds");
                linechart.setCreateSymbols(false);

                for(XYChart.Series line: lines){
                    linechart.getData().add(line);
                }

                VBox vbox = new VBox();
                vbox.getChildren().add(0,linechart);

                Scene scene = new Scene(vbox, 1000, 300);
                makeWindow("Data comparison line graph", scene);
            }
        });

        vbox.getChildren().add(index,ok);
        Scene scene = new Scene(vbox, 300, 400);
        makeWindow("Choose files to compare", scene);
    }

    private void makeWindow(String title, Scene scene) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
