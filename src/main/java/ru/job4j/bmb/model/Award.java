package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_award")
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int days;

    public Award(String title, String description, int days) {
        this.title = title;
        this.description = description;
        this.days = days;
    }

    public Award() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Award award = (Award) o;
        return days == award.days && Objects.equals(id, award.id) && Objects.equals(title, award.title) && Objects.equals(description, award.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, days);
    }

    @Override
    public String toString() {
        return "Award{"
                + "id="
                + id
                + ", title='"
                + title + '\''
                + ", description='"
                + description + '\''
                + ", days="
                + days
                + '}';
    }
}