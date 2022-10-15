package ru.hogwarts.school.entity;

import ru.hogwarts.school.validator.Validator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
@Entity
public class Faculty {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;
    @OneToMany(mappedBy = "faculty")
   private Collection<Student> students;

    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = Validator.validateString(name,"Название факультета не может быть пустым и должено содержать только буквы.");
        this.color = Validator.validateString(color,"Цвет факультета не может быть пустым и должен содержать только буквы.");
    }

    public Faculty() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name =  Validator.validateString(name,"Название факультета не может быть пустым и должено содержать только буквы.");
    }

    public void setColor(String color) {
        this.color = Validator.validateString(color,"Цвет факультета не может быть пустым и должен содержать только буквы.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty)) return false;

        Faculty faculty = (Faculty) o;

        if (!Objects.equals(id, faculty.id)) return false;
        if (!Objects.equals(name, faculty.name)) return false;
        return Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
