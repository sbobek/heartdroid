package demos.callbacks;

import heart.Callback;
import heart.Debug;
import heart.WorkingMemory;
import heart.alsvfd.SimpleNumeric;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sbk on 26.10.16.
 */
public class GetDayOfAWeekCallback implements Callback{
    public void execute(Attribute subject, WorkingMemory wmm) {
        System.out.println("Executing GetDayOfAWeekCallback for "+subject.getName());
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1;
        day = (day == 0 ? 7 : day);
        try {
            wmm.setAttributeValue(subject,new SimpleNumeric((double)day),false);
        } catch (AttributeNotRegisteredException e) {
            Debug.debug("CALLBACK",
                    Debug.Level.WARNING,
                    "Callback failed to set value of"+subject.getName()+", as the attribute is not registered in the Working Memory.");
        } catch (NotInTheDomainException e) {
            Debug.debug("CALLBACK",
                    Debug.Level.WARNING,
                    "Callback failed to set value of"+subject.getName()+", as the obtained value was not in the domain of attribute.");
        }
    }
}
