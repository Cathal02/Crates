package org.cathal02.data;


import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.cathal02.Crates;
import org.cathal02.crates.Crate;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CrateDataManager {

    Crates plugin;

    File file;

    public CrateDataManager(Crates plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder().getPath() + File.separator + "crates.json");
        if (!file.exists()) plugin.saveResource(file.getName(), false);
    }

    public void saveCrates(Collection<Crate> values) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Crate.class, new CrateSerializer()).setPrettyPrinting().create();
        List<Crate> data = new ArrayList<>(values);
        data.toArray();

        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.flush();
            gson.toJson(data, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<Crate> getCrateData() {
        try {
            JsonArray arr = new JsonArray();

            JsonReader reader = new JsonReader(new FileReader(file));
            JsonParser parser = new JsonParser();
            JsonElement parsedElement = parser.parse(reader);
            if (parsedElement.isJsonArray()) {
                arr = parsedElement.getAsJsonArray();
            }

            Gson gson = new GsonBuilder().registerTypeAdapter(Crate.class, new CrateDeserializer()).setPrettyPrinting().create();
            reader.close();

            return new ArrayList<Crate>(Arrays.asList(gson.fromJson(arr, Crate[].class)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
