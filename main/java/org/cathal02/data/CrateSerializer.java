package org.cathal02.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.cathal02.crates.Crate;
import org.cathal02.crates.CrateReward;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateSerializer implements JsonSerializer<Crate> {

    @Override
    public JsonElement serialize(Crate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("crateName", src.getName());
        obj.addProperty("crateItem", BukkitSerializer.itemToBase64(src.getCrateItem()));
        obj.add("rewards", serializeRewards(src.getCrateRewards()));
        return obj;
    }

    private JsonArray serializeRewards(List<CrateReward> rewards){
        List<JsonObject> rewardObjects = new ArrayList<>();
        for(CrateReward reward : rewards ){
            JsonObject obj = new JsonObject();
            obj.addProperty("item", BukkitSerializer.itemToBase64(reward.getItemStack()));
            obj.addProperty("chance",reward.getChance());

            rewardObjects.add(obj);
        }

        return new Gson().toJsonTree(rewardObjects).getAsJsonArray();
    }
}
