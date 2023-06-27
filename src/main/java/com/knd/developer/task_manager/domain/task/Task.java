package com.knd.developer.task_manager.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Исользуется для хранения и работы с тасками
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable {
    private Long id;
    private Long user_id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;
    private PriorityTask priority;

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    /**
     * Шифрует title для безопасного хранения в базе данных
     *
     * @param title - название таска
     */
    public void setTitle(String title) {
        if (title == null) {
            this.title = null;
        } else this.title = Base64.getUrlEncoder().encodeToString(title.getBytes());
    }

    /**
     * Шифрует description для безопасного хранения в базе данных
     *
     * @param description - описание таска
     */
    public void setDescription(String description) {
        if (description == null) {
            this.description = null;
        } else this.description = Base64.getUrlEncoder().encodeToString(description.getBytes());
    }

    /**
     * Расшифровывает title
     *
     * @return - возвращает название таска расшифрованным
     */
    public String getTitle() {
        if (title == null)
            return null;
        byte[] decodedBytes = Base64.getUrlDecoder().decode(title);

        return new String(decodedBytes);
    }

    /**
     * Расшифровывает description
     *
     * @return - возвращает описание таска расшифрованным
     */
    public String getDescription() {
        if (description == null)
            return null;
        byte[] decodedBytes = Base64.getUrlDecoder().decode(description);

        return new String(decodedBytes);
    }

    public static class TaskBuilder {
        private Long id;
        private Long user_id;
        private String title;
        private String description;
        private Status status;
        private LocalDateTime expirationDate;
        private PriorityTask priority;

        TaskBuilder() {
        }

        public TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder user_id(Long user_id) {
            this.user_id = user_id;
            return this;
        }

        public TaskBuilder title(String title) {
            this.title = Base64.getUrlEncoder().encodeToString(title.getBytes());
            return this;
        }

        public TaskBuilder description(String description) {
            this.description = Base64.getUrlEncoder().encodeToString(description.getBytes());
            return this;
        }

        public TaskBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public TaskBuilder expirationDate(LocalDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public TaskBuilder priority(PriorityTask priority) {
            this.priority = priority;
            return this;
        }

        public Task build() {
            return new Task(id, user_id, title, description, status, expirationDate, priority);
        }

        public String toString() {
            String desc = null;
            String titleGet = null;
            if (this.description != null) {
                byte[] descriptionBytes = Base64.getUrlDecoder().decode(this.description);
                desc = new String(descriptionBytes);
            }
            if(this.title != null){
                byte[] titleBytes = Base64.getUrlDecoder().decode(this.title);
                titleGet = new String(titleBytes);
            }


            return "Task.TaskBuilder(id=" + this.id + ", user_id=" + this.user_id + ", title=" + titleGet + ", description=" + desc + ", status=" + this.status + ", expirationDate=" + this.expirationDate + ", priority=" + this.priority + ")";
        }
    }
}
