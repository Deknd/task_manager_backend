package com.knd.developer.task_manager.domain.task;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Исользуется для хранения и работы с тасками
 */
@Data
public class Task implements Serializable {
    private Long id;
    private Long user_id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;
    private  PriorityTask priority;

    /**
     * Шифрует title для безопасного хранения в базе данных
     * @param title - название таска
     */
    public void setTitle(String title) {
        if(title==null){
            this.title=null;
        } else this.title = Base64.getUrlEncoder().encodeToString(title.getBytes());
    }

    /**
     * Шифрует description для безопасного хранения в базе данных
     * @param description - описание таска
     */
    public void setDescription(String description) {
        if(description == null) {
            this.description = null;
        } else this.description = Base64.getUrlEncoder().encodeToString(description.getBytes());
    }

    /**
     * Расшифровывает title
     * @return - возвращает название таска расшифрованным
     */
    public String getTitle() {
        if(title == null)
            return null;
        byte[] decodedBytes = Base64.getUrlDecoder().decode(title);

        return new String(decodedBytes);
    }
    /**
     * Расшифровывает description
     * @return - возвращает описание таска расшифрованным
     */
    public String getDescription() {
        if(description == null)
            return null;
        byte[] decodedBytes = Base64.getUrlDecoder().decode(description);

        return new String(decodedBytes);
    }
}
