package tally.transfer.common.domain.vo;

import java.time.LocalTime;
import java.util.Objects;

public class TimeWindow {

    private final LocalTime start;
    private final LocalTime end;

    public TimeWindow(
            final LocalTime start,
            final LocalTime end
    ) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("시작 및 종료 시간은 null일 수 없습니다. start = " + start + ", end = " + end);
        }
        this.start = start;
        this.end = end;
    }

    public static TimeWindow between(LocalTime start, LocalTime end) {
        return new TimeWindow(start, end);
    }

    public boolean isWithin(LocalTime time) {
        if (end.isBefore(start)) {
            return time.isAfter(start) || !time.isAfter(end);
        }
        return !time.isBefore(start) && time.isBefore(end);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[" + start + " ~ " + end + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final TimeWindow that = (TimeWindow) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
