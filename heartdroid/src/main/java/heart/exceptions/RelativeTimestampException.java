package heart.exceptions;

import heart.State;

/**
 * Created by sbk on 29.03.16.
 */
public class RelativeTimestampException extends Throwable {
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public RelativeTimestampException(State bestStateFound) {

    }
}
