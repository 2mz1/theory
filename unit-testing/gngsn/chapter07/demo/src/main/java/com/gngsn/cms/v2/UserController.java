package com.gngsn.cms.v2;


public class UserController {
    private final Database database = new Database();       // ① 프로세스 외부 의존성(Database와 MessageBus)이 직접 인스턴스화
    private final MessageBus messageBus = new MessageBus();

    public void changeEmail(int userId, String newEmail) throws Exception {
        Object[] data = database.getUserById(userId);
        String email = (String)data[1];
        UserType type = (UserType)data[2];
        User user = new User(userId, email, type);            // ② 컨트롤러가 데이터베이스 데이터를 User 인스턴스화

        Object[] companyData = database.getCompany();
        String companyDomainName = (String) companyData[0];
        int numberOfEmployees = (int) companyData[1];

        int newNumberOfEmployees = user.changeEmail(newEmail, // ③ 회사 직원수는 특정 사용자와 관련이 없음
                companyDomainName, numberOfEmployees);

        // ④ 새로운 이메일이 전과 다른지 여부와 관계없이, 무조건 데이터를 수정해서 저장하고 메시지 버스에 알림을 보냄
        database.saveCompany(newNumberOfEmployees);
        database.saveUser(user);

        messageBus.sendEmailChangedMessage(userId, newEmail);
    }
}
