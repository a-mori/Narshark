package sample;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    private static File file;
    private static ArrayList<File> files = new ArrayList<>();
    private String content = "t";

    public void fileChoose() throws Exception {
        FileChooser chooser = new FileChooser();
        try {
            chooser.setTitle("Open File");
            file = chooser.showOpenDialog(new Stage());

            Tab tab = new Tab();
            tab.setText(file.getName());
            tab.setId(file.toString());
            changeFile(tab.getId());

            if (content.equals("t")) {
                tab.setContent(GetTable.gettable(file));
            } else if (content.equals("l")) {
                tab.setContent(LineGraph.LineChart(file));
            } else if (content.equals("p")) {
                tab.setContent(PieChart.pieChart(file));
            }

            files.add(file);
            Main.createTab(tab);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void quit() {
        System.exit(1);
    }

    public static void changeFile(String newFile) {
        for (File f : files) {
            if (f.toString().equals(newFile)) {
                file = f;
            }
        }
    }

    public void compare() throws IOException {
        Compare compare = new Compare();
        compare.Compare(files);
    }

    public void createLineGraph() throws Exception {
        try {
            TabPane tabPane = Main.getTabPane();

            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                changeFile(tab.getId());
                tab.setContent(LineGraph.LineChart(file));
            }

            content = "l";
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createTable() throws Exception {
        try {
            TabPane tabPane = Main.getTabPane();

            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                changeFile(tab.getId());
                tab.setContent(GetTable.gettable(file));
            }

            content = "t";
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createPieChart() throws Exception {
        try {
            TabPane tabPane = Main.getTabPane();

            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                changeFile(tab.getId());
                tab.setContent(PieChart.pieChart(file));
            }

            content = "p";
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
