/**
 *
 *     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
 *
 *
 *     This file is part of HeaRTDroid.
 *     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
 *     XTT2 representation and other concepts developed within the HeKatE project .
 *
 *     HeaRTDroid is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     HeaRTDroid is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package heart.parser.hmr;

import heart.exceptions.ModelBuildingException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.Table;
import heart.xtt.XTTModel;
import java.util.List;

import heart.xtt.annotation.Annotation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotatedExpression {

    private XTTModel model;

    private final String MODEL_PATH = "src/test/resources/annotated-expression-test.pl";
    private final String RELATION = "relation";
    private final String SUBJECT = "subject";

    private final String DEPRECATED_FLAG = "Deprecated";
    private final String RECOMMENDED_FLAG = "Recommended";

    private final String FLAG_TABLE = "FlagAnnotation";
    private final String FLAG_RELATION = "describes";
    private final String FLAG_SUBJECT = "user";

    private final String DOUBLE_TABLE = "DoubleAnnotation";
    private final String DOUBLE_RELATION = "interests";
    private final String DOUBLE_SUBJECT = "system";
    private final String DOUBLE_RELATION2 = "realizes";
    private final String DOUBLE_SUBJECT2 = "failure";


    public AnnotatedExpression() {
    }

    @Before
    public void setUp() {
        SourceFile expression_test = new SourceFile(MODEL_PATH);
        assertNotNull(expression_test);
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

    @After
    public void tearDown() {
    }

    @Test
    public void testFILE() {
        SourceFile expression_test = new SourceFile(MODEL_PATH);
        assertNotNull(expression_test);
    }

    @Test
    public void testBUILD() {
        assertNotNull("XTT Model hasn't been built succesfully. Check logs.", model);
    }

    @Test
    public void testFLAG() {
        Table table = checkTableExistence(FLAG_TABLE);
        assertHasFlag(table, DEPRECATED_FLAG);
        assertHasFlag(table, RECOMMENDED_FLAG);
        assertHasSemanticAnnotation(table, FLAG_RELATION, FLAG_SUBJECT);
    }

    @Test
    public void testDOUBLE() {
        Table table = checkTableExistence(DOUBLE_TABLE);
        assertHasSemanticAnnotation(table, DOUBLE_RELATION, DOUBLE_SUBJECT);
        assertHasSemanticAnnotation(table, DOUBLE_RELATION2, DOUBLE_SUBJECT2);
    }

    Table checkTableExistence(String tableName) {
        Table table = null;

        for (Table t : model.getTables()) {
            if (t.getName().equals(tableName)) {
                table = t;
                break;
            }
        }
        assertNotNull(table);
        return table;
    }

    void assertHasFlag(Table table, String flag) {
        List<Annotation> annotations = table.annotationsNamed(flag);
        assertTrue(String.format("%s should have defined flag %s", table.getName(), flag), !annotations.isEmpty());
    }

    void assertHasSemanticAnnotation(Table table, String relation, String subject) {
        boolean hasAnnotation = false;
        List<Annotation> annotations = table.annotations();

        for (Annotation annotation : annotations) {

            String rel = annotation.getName();
            String sub = annotation.getRelationSubject();

            if (annotation.definesRelation() && subject.equals(sub) && relation.equals(rel)) {
                hasAnnotation = true;
            }
        }
        assertTrue(String.format("%s should have relation %s with subject %s", table.getName(),
                        relation, subject), hasAnnotation);
    }

}
