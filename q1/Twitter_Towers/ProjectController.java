
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;

public class ProjectController {
    private boolean isRect;
    private TriangularTower train;
    private RectangularTower rect;

    @FXML
    private Button perimeter;

    @FXML
    private Button print;

    @FXML
    private Button input;

    @FXML
    private TextField lengthInput;

    @FXML
    private Label lengthLabel;

    @FXML
    private TextField widthInput;

    @FXML
    private Label widthLabel;

    @FXML
    public void initialize()
    {
        setVisibleInput();
        setVisibleInputTrain();
    }

    private void setVisibleInput()
    {
        lengthInput.setText("");
        widthInput.setText("");
        lengthInput.setVisible(!lengthInput.isVisible());
        widthInput.setVisible(!widthInput.isVisible());
        lengthLabel.setVisible(!lengthLabel.isVisible());
        widthLabel.setVisible(!widthLabel.isVisible());
        input.setVisible(!input.isVisible());
    }

    private void setVisibleInputTrain()
    {
        perimeter.setVisible(!perimeter.isVisible());
        print.setVisible(!print.isVisible());
    }

    @FXML
    void rectangleSelected(ActionEvent event)  {
        isRect = true;
        setVisibleInput();


    }

    @FXML
    void triangleSelected(ActionEvent event) {
        isRect = false;
        setVisibleInput();


    }

    @FXML
    void lenAndWidInput(ActionEvent event) {
        int len = Integer.parseInt(lengthInput.getText());
        int wid = Integer.parseInt(widthInput.getText());
        if(!isValidInput(len) || !isValidInput(wid))
        {
            JOptionPane.showMessageDialog(null, "Wrong input");
            setVisibleInput();
        }
        else if(isRect) {
            rect = new RectangularTower(len, wid);
            if (rect.isSquare() || rect.diffLenGreaterThan5()) {
                JOptionPane.showMessageDialog(null, "The area is: " + rect.calcArea());
            } else {
                JOptionPane.showMessageDialog(null, "The perimeter is: " + rect.calcPerimeter());
            }
            setVisibleInput();
        }
        else
        {
            train = new TriangularTower(len, wid);
            setVisibleInputTrain();
        }
    }

    @FXML
    void calcTrianglePerimeter(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "The perimeter is: " + train.calcPerimeter());
        setVisibleInputTrain();
        setVisibleInput();
    }

    @FXML
    void printTriangle(ActionEvent event) {
        if(train.isEvenWidth() || train.widGreaterThanTwiceLen()) {
            JOptionPane.showMessageDialog(null, "The triangle cannot be printed");
            setVisibleInput();
            setVisibleInputTrain();
        }
        else//print triangle
        {
            String output = train.print();
            JOptionPane.showMessageDialog(null, output);
            setVisibleInput();
            setVisibleInputTrain();
        }
    }


    @FXML
    void closeProgram(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean isValidInput(int num)
    {
        return num > 0;
    }


}
