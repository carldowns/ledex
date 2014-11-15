package util;

import product.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URI;

/**
 * In here for historical.  We started with this but Jackson is simply faster and it is what DropWizard uses.
 * So we have this hear for grins.
 *
 * Created by carl_downs on 10/11/14.
 */
public class GsonUtil {

    /**
     *
     */
    public static void toJsonFile(Product obj, URI filepath) {

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
    public static Product fromJson(URI filepath) {

        Gson gson = new Gson();

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));

            // convert the json string back to object
            Product obj = gson.fromJson(br, Product.class);

            //System.out.println(obj);
            return obj;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
