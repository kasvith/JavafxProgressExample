package sample;

import javafx.concurrent.Task;

public class ProgressTask extends Task<Integer> {
    int maxIterations, delay;
    public ProgressTask(int maxIterations, int delay){
        this.maxIterations = maxIterations;
        this.delay = delay;
    }

    @Override
    protected Integer call() throws Exception {
        int currentIterations = 0;
        for (int i = 0; i <= maxIterations; i++) {
            if (isCancelled()){
                updateMessage("Cancelled");
                break;
            }

            currentIterations = i;
            updateMessage("Iterations: " + i);
            updateProgress(currentIterations, maxIterations);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                if (isCancelled()){
                    updateMessage("Cancelled");
                    break;
                }
            }
        }

        return currentIterations;
    }
}
