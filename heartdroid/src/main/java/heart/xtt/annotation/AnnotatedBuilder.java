package heart.xtt.annotation;

import java.util.List;

/**
 * Created by msl on 26/06/15.
 */
public interface AnnotatedBuilder {
    AnnotatedBuilder addIncompleteAnnotation(Annotation.Builder incAnnotation);
    AnnotatedBuilder setIncompleteAnnotations(List<Annotation.Builder> annotations);
    List<Annotation.Builder> getIncompleteAnnotations();
    List<Annotation> buildAnnotations();
}
