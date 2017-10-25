package heart.parser.hmr;

import heart.HeaRT;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.runtime.SourceFile;
import heart.parser.hmr.runtime.SourceStream;
import heart.xtt.XTTModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sbk on 24.01.17.
 */
public class StreamReader {
    private final String MODEL_PATH = "src/test/resources/expression-test.pl";
    SourceStream abbrevs_test;
    private XTTModel model;

    @Before
    public void setUp() {
        InputStream is = null;
        Exception ex = null;
        try {
            is = new FileInputStream(MODEL_PATH);
            abbrevs_test = new SourceStream(is);
            HMRParser parser = new HMRParser();
            parser.parse(abbrevs_test);
            model = parser.getModel();
            assertNotNull(model);
        } catch (FileNotFoundException e) {
            ex = e;
        } catch (ModelBuildingException e) {
            ex = e;
        } catch (ParsingSyntaxException e) {
            ex = e;
        }
        assertEquals(null, ex);
        assertNotNull(abbrevs_test);

    }

    @After
    public void tearDown() {
        HeaRT.getWm().unregisterAll(model);
    }


}
