package com.gngsn;

public class Message {
    public String Header;
    public String Body;
    public String Footer;

    public String getHeader() {
        return Header;
    }

    public void setHeader(final String header) {
        Header = header;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(final String body) {
        Body = body;
    }

    public String getFooter() {
        return Footer;
    }

    public void setFooter(final String footer) {
        Footer = footer;
    }
}