package com.gngsn.crm.v1;

public interface IMessageBus {
    void sendEmailChangedMessage(int userId, String mail);
}
