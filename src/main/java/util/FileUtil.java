package util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Assembly;
import model.Part;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reading and writing Json encoded files to/from the local file system for the purpose
 * of uploading them into the database, downloading them, etc.
 *
 * Created by carl_downs on 10/9/14.
 */
public class FileUtil {

    static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    /**
     *
     * @param fileURI
     * @return
     */
    public Assembly importAssembly (URI fileURI) {


        try {
            Assembly asm = mapper.readValue(new File(fileURI), Assembly.class);
            return asm;
        }
        catch (Throwable t) {
            throw new RuntimeException ("unable to parse assembly file : " + t.toString());
        }
    }

    /**
     *
     * @param fileURI
     * @return
     */
    public List<Assembly> importAssemblies (URI fileURI) {


        try {
            List<Assembly> list = new ArrayList<>();
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createParser(new File(fileURI));

            // advance stream to START_ARRAY first:
            jp.nextToken();

            // and then each time, advance to opening START_OBJECT
            while (jp.nextToken() == JsonToken.START_OBJECT) {
                Assembly assembly = mapper.readValue(jp, Assembly.class);
                list.add(assembly);
            }
            return list;
        }
        catch (Throwable t) {
            throw new RuntimeException ("unable to parse assembly file : " + t.toString());
        }
    }

    /**
     * Import a parts file.
     * Expects to find a Json Array of parts
     * @param fileURI
     * @return
     */
    public List<Part> importParts(URI fileURI) {

        try {
            List<Part> list = new ArrayList<>();
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createParser(new File(fileURI));

            // advance stream to START_ARRAY first:
            jp.nextToken();

            // and then each time, advance to opening START_OBJECT
            while (jp.nextToken() == JsonToken.START_OBJECT) {
                Part part = mapper.readValue(jp, Part.class);
                list.add(part);
            }
            return list;
        }
        catch (Throwable t) {
            throw new RuntimeException ("unable to parse parts file : " + t.toString());
        }
    }

    /**
     * Import a single part from a file.
     * Expects to find single Json Part
     * @param fileURI
     * @return
     */
    public Part importPart (URI fileURI) {
        try {
            Part part = mapper.readValue(new File(fileURI), Part.class);
            return part;
        }
        catch (Throwable t) {
            throw new RuntimeException ("unable to parse part file : " + t.toString());
        }
    }
}
