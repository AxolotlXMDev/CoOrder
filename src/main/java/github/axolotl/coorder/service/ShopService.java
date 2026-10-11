package github.axolotl.coorder.service;

import github.axolotl.coorder.entity.Shop;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {
    @Value("${app.config.shops-path}")
    private String shopsPath;
    @Getter
    private List<Shop> shops;

    @PostConstruct
    public void init() {
        reloadShops();
    }

    public Shop getShopById(String id) {
        return shops.stream()
                .filter(shop -> shop.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void reloadShops() {
        shops = new ArrayList<>();
        File folder = new File(shopsPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    String text = Files.readString(file.toPath());
                    ObjectMapper mapper = new ObjectMapper();
                    Shop shop = mapper.readValue(text, Shop.class);
                    shops.add(shop);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
