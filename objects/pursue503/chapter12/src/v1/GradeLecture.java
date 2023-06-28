package v1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GradeLecture extends Lecture {

    private List<Grade> grades;

    public GradeLecture(int pass, String title, List<Integer> scores, List<Grade> grades) {
        super(pass, title, scores);
        this.grades = grades;
    }

    @Override
    public String evaluate() {
        return super.evaluate() + "," + gradesStatistics();
    }

    public double average(String gradeName) {
        return grades.stream()
                .filter(each -> each.isName(gradeName))
                .findFirst()
                .map(this::gradeAverage)
                .orElse(0d);
    }

    private double gradeAverage(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    private String gradesStatistics() {
        return grades.stream()
                .map(grade -> format(grade))
                .collect(Collectors.joining(" "));
    }

    private String format(Grade grade) {
        return String.format("%s:%d", grade.getName(), gradeCount(grade));
    }

    private long gradeCount(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .count();
    }

    public static void main(String[] args) {
        Lecture lecture = new GradeLecture(70,
                "객체지향 프로그래밍",
                Arrays.asList(81, 95, 75, 50, 45),
                Arrays.asList(new Grade("A",100, 95),
                        new Grade("B",94, 80),
                        new Grade("C",79, 70),
                        new Grade("D",69, 50),
                        new Grade("F",49, 0))
        );
        System.out.println(lecture.evaluate());
    }

}
