package heart.exceptions;

import heart.xtt.Attribute;

/**
 * Created by sbk on 19.03.15.
 */
public class UnknownValueException extends Exception {
    private Attribute att;

    public UnknownValueException(String message){
        super(message);
    }

    public UnknownValueException(Attribute att, String message){
        super(message);
        this.att = att;
    }

    public Attribute getAtt() {
        return att;
    }

    public void setAtt(Attribute att) {
        this.att = att;
    }
}
