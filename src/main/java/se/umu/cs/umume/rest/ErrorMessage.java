/*
 * @(#)ErrorMessage.java
 * Time-stamp: "2010-01-18 19:28:02 anton"
 */

package se.umu.cs.umume.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="error")
public class ErrorMessage { 
    private String msg;
    
    public ErrorMessage() {}
    public ErrorMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}


