package sample;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class Controller {
    public ProgressBar pbIter;
    public ProgressIndicator piIter;
    public TextField txtIter;
    public Button btnIterate;
    public Button btnCancel;
    public Label lblStatus;
    public TextField txtDelay;

    protected ProgressTask progressTask;

    public void RunSimulation(ActionEvent actionEvent) {
        int iter, delay;
        try {
            iter = Integer.parseInt(txtIter.getText());
            delay = Integer.parseInt(txtDelay.getText());
        } catch (NumberFormatException e){
            new Alert(Alert.AlertType.ERROR, "Invalid number!").showAndWait();
            return;
        }
        progressTask = new ProgressTask(iter, delay);

        pbIter.progressProperty().unbind();
        pbIter.progressProperty().bind(progressTask.progressProperty());

        piIter.progressProperty().unbind();
        piIter.progressProperty().bind(progressTask.progressProperty());

        lblStatus.textProperty().unbind();
        lblStatus.textProperty().bind(progressTask.messageProperty());

        btnIterate.setDisable(true);
        btnCancel.setDisable(false);

        progressTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            btnIterate.setDisable(false);
            btnCancel.setDisable(true);
        });

        progressTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, event -> {
            btnCancel.setDisable(true);
            btnIterate.setDisable(false);

            pbIter.progressProperty().unbind();
            pbIter.setProgress(0);
            piIter.progressProperty().unbind();
            piIter.setProgress(0);
            lblStatus.textProperty().unbind();
            lblStatus.setText("Failed");
        });
        new Thread(progressTask).start();
    }

    public void Cancel(ActionEvent actionEvent) {
        progressTask.cancel();

        btnCancel.setDisable(true);
        btnIterate.setDisable(false);

        pbIter.progressProperty().unbind();
        pbIter.setProgress(0);
        piIter.progressProperty().unbind();
        piIter.setProgress(0);
        lblStatus.textProperty().unbind();
        lblStatus.setText("Cancelled");
    }
}
