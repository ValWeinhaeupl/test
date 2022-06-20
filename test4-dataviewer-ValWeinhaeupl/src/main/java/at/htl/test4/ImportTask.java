package at.htl.test4;

import javafx.concurrent.Task;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class ImportTask extends Task {
    String path;
    ChoiceBox cb;

    public ImportTask(String path, ChoiceBox cb) {
        this.path = path;
        this.cb = cb;
    }

    @Override
    protected Object call() throws Exception {
        AtomicInteger count = new AtomicInteger();
        long amount = Files.lines(Path.of(path)).skip(1)
                .map(s -> s.split(";")[2])
                .distinct()
                .count();

        Files.lines(Path.of(path))
                .skip(1)
                .map(s -> s.split(";")[2])
                .distinct()
                .sorted()
                .forEach(s -> {

                    cb.getItems().add(s);
                    updateProgress(count.getAndIncrement() , amount);
                });;

        return "sieg";
    }
}
