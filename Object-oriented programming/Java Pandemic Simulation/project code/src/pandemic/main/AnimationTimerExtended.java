package pandemic.main;
import javafx.animation.AnimationTimer;

/**
 * Creates an animation timer with specific delay in seconds.
 */
public abstract class AnimationTimerExtended extends AnimationTimer {

    private long sleepNs = 0;
    long prevTime = 0;

    public AnimationTimerExtended( long sleepS) {
        this.sleepNs = sleepS * 1_000_000_000;
    }

    @Override
    public void handle(long now) {
        if ((now - prevTime) < sleepNs) {
            return;
        }

        prevTime = now;
        handle();
    }

    public abstract void handle();

}