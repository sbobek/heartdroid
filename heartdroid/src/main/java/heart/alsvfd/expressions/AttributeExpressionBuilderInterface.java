package heart.alsvfd.expressions;

import heart.exceptions.BuilderException;
import heart.xtt.Attribute;

import java.util.Map;

public interface AttributeExpressionBuilderInterface extends ExpressionBuilderInterface {
    abstract String getAttributeName();
    abstract AttributeExpressionInterface build(Map<String,Attribute> atts) throws BuilderException;
}
