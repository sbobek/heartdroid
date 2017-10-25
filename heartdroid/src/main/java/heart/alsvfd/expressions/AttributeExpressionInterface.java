package heart.alsvfd.expressions;

import heart.xtt.Attribute;

public interface AttributeExpressionInterface extends ExpressionInterface {
    String getAttributeName();
    Attribute getAttribute();
}
