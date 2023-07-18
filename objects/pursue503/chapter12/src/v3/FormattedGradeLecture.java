package v3;

import java.util.List;

public class FormattedGradeLecture extends GradeLecture {

    public FormattedGradeLecture(int pass, String title, List<Integer> scores, List<Grade> grades) {
        super(pass, title, scores, grades);
    }

    public String formatAverage() {
        return String.format("Avg: %1.1f", super.average());
    }
}
