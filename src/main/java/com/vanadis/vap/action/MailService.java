package com.vanadis.vap.action;


import java.util.Map;

public interface MailService {
    /**
     * 发送邮件
     *
     * @return
     */
    public void sendSimpleMail(String receiver, String subject, Map<String, String> content);
    /**
     * 发送邮件
     * @param receiver 邮件接受者
     * @param subject 邮件主题
     * @param content 邮件内容
     */
}
