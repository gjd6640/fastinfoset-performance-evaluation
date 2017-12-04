import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import jaxb.RootWithListJaxbObject;
import utils.GCInfoCollector;
import utils.MemorySizingUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FastinfosetCodecTest {
    private final static double MEGABYTE = 1024*1024;
    private final static double KILOBYTE = 1024;
    private static JAXBContext context;
    static byte[] fiEncodedData; // We build this just once AND do it outside of the scope of the measurements around these tests
    
    private static void generateSampleXMLWithLotsOfTagNameVariety() throws FileNotFoundException {
        final int REPEAT_GROUP_COUNT = 220;
        final int GENERATED_UNIQUE_FIELD_NAMES = 75;
        try(PrintWriter out = new PrintWriter("src/test/resources/generatedSample.xml")) {
            out.println("<root xmlns=\"myNS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            
            for (int repeats = 1; repeats <= REPEAT_GROUP_COUNT; repeats++) {
                out.println("<list-item>");
                for (int i = 1; i <= GENERATED_UNIQUE_FIELD_NAMES; i++) {
                    out.print("<uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA");
                    out.print(i);
                    out.print(">");
                    
                    out.print("asdf</uniqueFieldWithASillyLongNameAAAAAAAAAAAAAAAAAA");
                    out.print(i);
                    out.print(">");
                }
                out.println("</list-item>");
            }
            out.println("</root>");
            out.flush();
        }
    }

    int ITERATIONS = 1000;
    
    @Test
    public void decodeMany1WithJaxbInterningSuppressedViaReflection() throws Exception {
        decodeMany(new ReflectionInterningSuppressionDecoder());
    }

    @Test
    public void decodeMany2WithDefaultInterningSettings() throws Exception {
        // Via a profiler observe many calls made to "com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor.intern". On my PC hooked to JProfiler in sampling mode this test took 11.4s to run & 4 seconds was spent in total in this intern method. 
        decodeMany(new ControlDecoder());
    }

    // Repeat both tests above to ensure that the first numbers weren't a fluke due to JVM warmup or one-time initialization costs
    @Test
    public void decodeMany3WithJaxbInterningSuppressedViaReflection() throws Exception {
        decodeMany(new ReflectionInterningSuppressionDecoder());
    }

    @Test
    public void decodeMany4WithDefaultInterningSettings() throws Exception {
        // Via a profiler observe many calls made to "com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor.intern". On my PC hooked to JProfiler in sampling mode this test took 11.4s to run & 4 seconds was spent in total in this intern method. 
        decodeMany(new ControlDecoder());
    }

    @Test // This fails today. It is here to demonstrate that it is not trivial to disable jaxb2's string interning behavior.
    public void decodeMany5WithInterningDisabledViaFactory() throws Exception {
        decodeMany(new InterningSuppressedViaFactoryPropertiesDecoder());
    }

    @Test // demonstrates a reflection-free but still ugly way of disabling String interning in jaxb 
    public void decodeMany6WithInterningDisabledViaCustomStaxManager() throws Exception {
        decodeMany(new InterningSuppressedViaExtendingTheStaxManagerDecoder());
    }
    
    
    // Setup primarily happens in method "classSetup()"
    public void decodeMany(Decoder decoder) throws Exception {
        
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Line of pound symbols was distracting...
        //String repeatedPoundSymbols = new String(new char[ITERATIONS]).replace('\0', '#');
        //System.out.println(repeatedPoundSymbols);
        for (int i = 1; i <= ITERATIONS; i++) {
            System.out.print(".");
            // Operate
            InputStream is = new ByteArrayInputStream(fiEncodedData);
            XMLStreamReader xsr = decoder.go(is);
            
            JAXBElement<RootWithListJaxbObject> jaxbElementValue = unmarshaller.unmarshal(xsr, RootWithListJaxbObject.class);      
            xsr.close();

            // Verify
            Assert.assertNotNull(jaxbElementValue.getValue().list.get(1).uniqueFieldsList);
            Assert.assertEquals("asdf", jaxbElementValue.getValue().list.get(1).uniqueFieldsList.get(1));
            int listSize = jaxbElementValue.getValue().list.size();
            Assert.assertTrue("List size was " + listSize, listSize  > 10); // This prevents the JVM from potentially optimizing away the entire workload. It also serves as a sanity check.
        }
        System.out.println();
    }

    @BeforeClass
    public static void classSetup() {
        createSampleFile();
        GCInfoCollector.installCollector();
    }
    
    private static void createSampleFile() {
        try {
            generateSampleXMLWithLotsOfTagNameVariety();
            context = JAXBContext.newInstance(RootWithListJaxbObject.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
       
        InputStream is;
        is = FastinfosetCodecTest.class.getClassLoader().getResourceAsStream("generatedSample.xml");
        
        FastinfosetCodec codec = new FastinfosetCodec();
        fiEncodedData = codec.encode(is).toByteArray();
        long bytesInFile = new File("src/test/resources/generatedSample.xml").length();
        System.out.println("Text XML size was " + Math.round(bytesInFile / MEGABYTE *10)/10.0 + " megabytes. Fastinfoset binary form was " + Math.round(fiEncodedData.length / KILOBYTE) + " kb \n");
    }
    
    long testStartTime; // Note that this is likely not thread-safe so don't run the tests in this suite in multiple threads. 
    
    @Before
    public void setup() {
        GCInfoCollector.resetCounters();
        MemorySizingUtil.init();
        testStartTime = System.currentTimeMillis();
    }
    
    @After
    public void tearDown() {
        long totalTimeInMillis = System.currentTimeMillis() - testStartTime;
        System.out.println("Test took " + totalTimeInMillis + "ms to run " + ITERATIONS + " iterations (" + (totalTimeInMillis/ITERATIONS) + " ms/iteration).");
        GCInfoCollector.printCollectedStats();
        MemorySizingUtil.checkTotalJVMMemorySizeForGrowth();
        System.out.println("\n**********\n");
    }
    
    @Rule
    public TestRule watcher = new TestWatcher() {
       @Override
    protected void starting(Description description) {
          System.out.println("Starting test: " + description.getMethodName());
       }
    };
    private static abstract class Decoder {
        FastinfosetCodec codec = new FastinfosetCodec();
        
        abstract XMLStreamReader go(InputStream is);
    }
    
    private static class ControlDecoder extends Decoder {
        @Override
        public XMLStreamReader go(InputStream is) {
            return codec.decodeWithDefaultInterningSettings(is);
        }
    }
    
    private static class ReflectionInterningSuppressionDecoder extends Decoder {
        @Override
        public XMLStreamReader go(InputStream is) {
            return codec.decodeInterningSuppressedViaReflection(is);
        }
    }

    private static class InterningSuppressedViaFactoryPropertiesDecoder extends Decoder {
        @Override
        public XMLStreamReader go(InputStream is) {
            return codec.decodeWithInterningDisabledThruFactoryProperties(is);
        }
    }

    // Ugly but works
    private static class InterningSuppressedViaExtendingTheStaxManagerDecoder extends Decoder {
        @Override
        public XMLStreamReader go(InputStream is) {
            return codec.decodeWithInterningDisabledUsingCustomFactoryExtension(is);
        }
    }
    
    
    
}
