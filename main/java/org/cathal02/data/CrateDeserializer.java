package org.cathal02.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;
import org.cathal02.crates.Crate;
import org.cathal02.crates.CrateReward;
import org.cathal02.utils.XMaterial;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrateDeserializer implements JsonDeserializer<Crate>{

    @Override
    public Crate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String name = obj.get("crateName").getAsString();
        ItemStack crateItem = XMaterial.DIAMOND.parseItem();

        try {
            crateItem = BukkitSerializer.base64ToItemStack(obj.get("crateItem").getAsString());
        } catch (IOException e){
            System.out.println("[Crates] Error converting item");
        }

        List<CrateReward> rewards = new ArrayList<>();

        for(JsonElement reward : obj.get("rewards").getAsJsonArray()) {

            JsonObject jsonObj = reward.getAsJsonObject();
            try {
                ItemStack item = BukkitSerializer.base64ToItemStack(jsonObj.get("item").getAsString());
                double chance = jsonObj.get("chance").getAsDouble();

                rewards.add(new CrateReward(item,chance));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new Crate(name,crateItem,rewards);
        }
}
