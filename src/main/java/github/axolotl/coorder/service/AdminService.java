package github.axolotl.coorder.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Getter
    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @PostConstruct
    public void initAdmin() {
        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "环境变量 ADMIN_PASSWORD 未配置，可用“export ADMIN_PASSWORD='your_password'”配置。"
            );
        }
    }

    public boolean verify(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }

}
