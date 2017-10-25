package heart.parser.hmr;

import heart.StateElement;
import heart.alsvfd.SimpleNumeric;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;
import java.util.List;

import heart.xtt.annotation.Annotation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by msl on 21/01/16.
 */
public class RuleCertaintyFactors {

    private XTTModel model;

    private final String MODEL_PATH = "src/test/resources/certainty-factors-test.pl";


    public RuleCertaintyFactors() {
    }

    @Before
    public void setUp() {

        SourceFile expression_test = new SourceFile(MODEL_PATH);
        HMRParser parser = new HMRParser();
        try {
            parser.parse(expression_test);
            model = parser.getModel();
        }
        catch (ModelBuildingException ex) {
            ex.printStackTrace();
        }
        catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testFILE() {
        SourceFile expression_test = new SourceFile(MODEL_PATH);
        assertNotNull(expression_test);
    }

    @Test
    public void testTable() {
        Table testTable = model.getTables().getFirst();
        String expectedName = "Test";
        assertEquals(String.format("The first should table should have name %s", expectedName), testTable.getName(),
                expectedName);
        Integer rulesAmount = testTable.getRules().size();
        Integer expectedAmount = 5;
        assertEquals(String.format("Test table should have %d rules, but have only %d", expectedAmount, rulesAmount),
                expectedAmount, rulesAmount);
    }

    @Test
    public void defaultCertaintyFactor() {
        genericTest(0, 1.0f);
    }

    @Test
    public void positiveCertaintyFactor() {
        genericTest(1, 0.5f);
    }

    @Test
    public void negativeCertaintyFactor() {
        genericTest(2, -0.5f);
    }

    @Test
    public void tooBigCertaintyFactor() {
        genericTest(3, 1.0f);
    }

    @Test
    public void tooSmallCertaintyFactor() {
        genericTest(4, -1.0f);
    }

    public void genericTest(Integer ruleIndex, float expectedValue) {
        Table testTable = model.getTables().getFirst();
        Rule testRule = testTable.getRules().get(ruleIndex);
        float existingValue = testRule.getCertaintyFactor();
        float accuracy = 0.0001f;
        assertEquals(String.format("The certainty factor of rule with index %d should be %f, but %f was found",
                ruleIndex, expectedValue, existingValue),
                expectedValue,
                existingValue,
                accuracy);
    }

}

