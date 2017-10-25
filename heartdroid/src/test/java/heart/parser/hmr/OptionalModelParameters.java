package heart.parser.hmr;

import heart.HeaRT;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.XTTModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sbk on 24.01.17.
 */
public class OptionalModelParameters {

    private final String MODEL_PATH = "src/test/resources/optional-parameters-abbrevs-missing-test.pl";
    SourceFile abbrevs_test;
    private XTTModel model;

    @Before
    public void setUp() {
        abbrevs_test = new SourceFile(MODEL_PATH);
        assertNotNull(abbrevs_test);


    }

    @After
    public void tearDown() {
        HeaRT.getWm().unregisterAll(model);
    }

    @Test
    public void optionalAbbrevsMissing(){
        HMRParser parser = new HMRParser();
        Exception e = null;
        try {
            parser.parse(abbrevs_test);
            model = parser.getModel();
            assertNotNull(model);
        }
        catch (ModelBuildingException ex) {
            e=ex;
        }
        catch (ParsingSyntaxException ex) {
            e=ex;
        }
        assertEquals(null,e);
    }
}
