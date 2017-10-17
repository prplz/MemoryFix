package io.prplz.memoryfix;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker extends Thread {

    private final MemoryFix mod;
    private final String url;
    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer())
            .registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer())
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public UpdateChecker(MemoryFix mod) {
        this.mod = mod;
        url = "https://mods.purple.services/update/check/" + MemoryFix.MOD_NAME + "/" + MemoryFix.VERSION;
    }

    @Override
    public void run() {
        for (int retry = 0; retry < 3; retry++) {
            try {
                UpdateResponse response = check(url);
                mod.setUpdateMessage(response.updateMessage);
            } catch (Exception ex) {
                System.out.println("GET " + url + " failed:");
                System.out.println(ex.toString());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException interrupted) {
                    return;
                }
                continue;
            }
            return;
        }
    }

    private static class UpdateResponse {
        IChatComponent updateMessage;
    }

    private UpdateResponse check(String url) throws IOException {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");

            int response = con.getResponseCode();
            if (response == 200) {
                try (InputStreamReader in = new InputStreamReader(con.getInputStream(), "UTF-8")) {
                    return gson.fromJson(in, UpdateResponse.class);
                }
            }
            throw new IOException("HTTP " + response);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
