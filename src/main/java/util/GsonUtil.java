package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Assembly;

import java.io.*;
import java.net.URI;

/**
 * Created by carl_downs on 10/11/14.
 */
public class GsonUtil {

    /**
     *
     */
    public static void toJsonFile(Assembly obj, URI filepath) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(obj);

        try {
            // write converted json data to a file named "file.json"
            File file = new File(filepath);
            if (file.exists())
                file.delete();

            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(json);

    }

    /**
     *
     */
    public static Assembly fromJson(URI filepath) {

        Gson gson = new Gson();

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));

            // convert the json string back to object
            Assembly obj = gson.fromJson(br, Assembly.class);

            //System.out.println(obj);
            return obj;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
