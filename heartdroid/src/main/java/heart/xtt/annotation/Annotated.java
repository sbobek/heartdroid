package heart.xtt.annotation;

import java.util.List;

/**
 * Created by msl on 26/06/15.
 */
public interface Annotated {

    List<Annotation> annotations();
    List<Annotation> annotationsNamed(String name);
}

