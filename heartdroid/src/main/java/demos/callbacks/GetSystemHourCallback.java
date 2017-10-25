package demos.callbacks;

import heart.Callback;
import heart.Debug;
import heart.HeaRT;
import heart.WorkingMemory;
import heart.alsvfd.SimpleNumeric;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

import java.util.Calendar;

/**
 * Created by sbk on 26.10.16.
 */
public class GetSystemHourCallback implements Callback{
    public void execute(Attribute subject, WorkingMemory wmm) {
        System.out.println("Executing GetSystemHourCallback for "+subject.getName());
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        try {
            wmm.setAttributeValue(subject,new SimpleNumeric((double)hour),false);
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
