import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class RecurringSchedule {
    private String subject;
    private DayOfWeek dayOfWeek;
    private LocalTime from;
    private Duration duration;

    public RecurringSchedule(String subject, DayOfWeek dayOfWeek, LocalTime from, Duration duration) {
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.from = from;
        this.duration = duration;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getFrom() {
        return from;
    }

    public Duration getDuration() {
        return duration;
    }

    public static void main(String[] args) {
        RecurringSchedule recurringSchedule = new RecurringSchedule("회의", DayOfWeek.WEDNESDAY,
                LocalTime.of(10, 30), Duration.ofMillis(30));
    }
}
