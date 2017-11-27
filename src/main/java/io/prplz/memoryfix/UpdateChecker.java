package io.prplz.memoryfix;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.ForgeVersion;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class UpdateChecker extends Thread {

    private final String url;
    private final Consumer<UpdateResponse> callback;
    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer())
            .registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer())
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public UpdateChecker(String url, Consumer<UpdateResponse> callback) {
        this.url = url;
        this.callback = callback;
    }

    @Override
    public void run() {
        for (int retry = 0; retry < 3; retry++) {
            try {
                UpdateResponse response = check(url);
                try {
                    callback.accept(response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    public static class UpdateResponse {

        private final IChatComponent updateMessage;

        public UpdateResponse(IChatComponent updateMessage) {
            this.updateMessage = updateMessage;
        }

        public IChatComponent getUpdateMessage() {
            return updateMessage;
        }
    }

    private UpdateResponse check(String url) throws IOException {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            String agent = "Java/" + System.getProperty("java.version") + " " +
                    "Forge/" + ForgeVersion.getVersion() + " " +
                    System.getProperty("os.name") + " " +
                    System.getProperty("os.arch") + " ";
            con.setRequestProperty("User-Agent", agent);

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
