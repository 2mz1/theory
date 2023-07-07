package v1;

import java.util.Arrays;

public class pProfessor {
    private String name;
    private Lecture lecture;

    public Professor(String name, Lecture lecture) {
        this.name = name;
        this.lecture = lecture;
    }

    public String compileStatistics() {
        return String.format("[%s] %s - Avg: %.1f", name, lecture.evaluate(), lecture.average());
    }

    public static void main(String[] args) {
        Professor professor = new Professor("다익스트라",
                new Lecture(70,
                        "알고리즘",
                        Arrays.asList(81, 95, 75, 50, 45)));
        //결과 => "[다익스트라] Pass:3 Fail:2 - Avg: 69.2"
        String statistics = professor.compileStatistics();
        System.out.println(statistics);

        Professor professor2 = new Professor("다익스트라",
                new GradeLecture(70,
                        "알고리즘",
                        Arrays.asList(81, 95, 75, 50, 45),
                        Arrays.asList(new Grade("A",100, 95),
                                new Grade("B",94, 80),
                                new Grade("C",79, 70),
                                new Grade("D",69, 50),
                                new Grade("F",49, 0))
                       ));
//결과 => "[다익스트라] Pass:3 Fail:2, A:1 B:1 C:1 D:1 F:1 - Avg: 69.2"
        String statistics2 = professor.compileStatistics();
        System.out.println(statistics2);

    }

}
