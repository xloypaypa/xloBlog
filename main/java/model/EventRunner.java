package model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xlo on 15-8-23.
 * it's the event runner
 */
public class EventRunner {
    protected static EventRunner eventRunner = new EventRunner();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private EventRunner() {

    }

    public static EventRunner getEventRunner() {
        return eventRunner;
    }

    protected void submitAEvent(Event event) {
        executorService.execute(event::call);
    }
}
