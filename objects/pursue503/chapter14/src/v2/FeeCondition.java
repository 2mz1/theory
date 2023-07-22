package v2;

import java.util.List;

public interface FeeCondition {
    List<DateTimeInterval> findTimeIntervals(Call call);
}
