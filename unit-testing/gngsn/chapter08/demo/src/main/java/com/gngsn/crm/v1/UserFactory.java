package com.gngsn.crm.v1;

/**
 * chapter08 - v1
 */
public class UserFactory {
    public static User create(Object[] data) {
        assert data.length >= 3;

        int id = (int)data[0];
        String email = (String)data[1];
        UserType type = (UserType)data[2];

        return new User(id, email, type);
    }
}
