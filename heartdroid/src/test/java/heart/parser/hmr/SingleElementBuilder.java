package heart.parser.hmr;

import heart.StateElement;
import heart.alsvfd.SimpleNumeric;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.parser.hmr.runtime.SourceString;
import heart.xtt.*;

import java.io.*;
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
public class SingleElementBuilder {

    private XTTModel model;

    private final String TYPE_PATH = "src/test/resources/single-type-builder-test.pl";
    private final String ATTRIBUTE_PATH = "src/test/resources/single-attribute-builder-test.pl";
    private final String TABLE_PATH = "src/test/resources/single-table-builder-test.pl";
    private final String RULE_PATH = "src/test/resources/single-rule-builder-test.pl";

    public SingleElementBuilder() {
    }

    @Test
    public void testTypeFILE() {
        SourceFile singleTypeFile = new SourceFile(TYPE_PATH);

        HMRParser parser = new HMRParser();
        try {
            parser.parse(singleTypeFile);
            Type.Builder singleType = parser.getTypeBuilder();
            assertNotNull(singleType);
        }catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTypeString() {
        try {
            File fin = new File(TYPE_PATH);
            FileInputStream fis = new FileInputStream(fin);

            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            String allFileString = "";
            while ((line = br.readLine()) != null) {
                allFileString += line;
            }

            br.close();
            SourceString singleTypeString = new SourceString(allFileString);
            HMRParser parser = new HMRParser();

            parser.parse(singleTypeString);
            Type.Builder singleType = parser.getTypeBuilder();
            assertNotNull(singleType);
        } catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAttributeFILE() {
        SourceFile singleAttributeFile = new SourceFile(ATTRIBUTE_PATH);

        HMRParser parser = new HMRParser();
        try {
            parser.parse(singleAttributeFile);
            Attribute.Builder singleAttribute = parser.getAttributeBuilder();
            assertNotNull(singleAttribute);
        }catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testAttributeString() {
        try {
            File fin = new File(ATTRIBUTE_PATH);
            FileInputStream fis = new FileInputStream(fin);

            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            String allFileString = "";
            while ((line = br.readLine()) != null) {
                allFileString += line;
            }

            br.close();
            SourceString singleAttributeString = new SourceString(allFileString);
            HMRParser parser = new HMRParser();

            parser.parse(singleAttributeString);
            Attribute.Builder singleAttribute = parser.getAttributeBuilder();
            assertNotNull(singleAttribute);
        } catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTableFILE() {
        SourceFile singleTableFile = new SourceFile(TABLE_PATH);

        HMRParser parser = new HMRParser();
        try {
            parser.parse(singleTableFile);
            Table.Builder singleTable = parser.getTableBuilder();
            assertNotNull(singleTable);
        }catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testTableString() {
        try {
            File fin = new File(TABLE_PATH);
            FileInputStream fis = new FileInputStream(fin);

            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            String allFileString = "";
            while ((line = br.readLine()) != null) {
                allFileString += line;
            }

            br.close();
            SourceString singleTableString = new SourceString(allFileString);
            HMRParser parser = new HMRParser();

            parser.parse(singleTableString);
            Table.Builder singleTable = parser.getTableBuilder();
            assertNotNull(singleTable);
        } catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRuleFILE() {
        SourceFile singleRuleFile = new SourceFile(RULE_PATH);

        HMRParser parser = new HMRParser();
        try {
            parser.parse(singleRuleFile);
            Rule.Builder singleRule = parser.getRuleBuilder();
            assertNotNull(singleRule);
        }catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testRuleString() {
        try {
            File fin = new File(RULE_PATH);
            FileInputStream fis = new FileInputStream(fin);

            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            String allFileString = "";
            while ((line = br.readLine()) != null) {
                allFileString += line;
            }

            br.close();
            SourceString singleRuleString = new SourceString(allFileString);
            HMRParser parser = new HMRParser();

            parser.parse(singleRuleString);
            Rule.Builder singleRule = parser.getRuleBuilder();
            assertNotNull(singleRule);
        }catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

