package ru.hogwarts.school.entity;

import ru.hogwarts.school.validator.Validator;

import javax.persistence.*;
import java.util.Objects;
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int age;
    @ManyToOne()
    @JoinColumn(name = "Faculty_ID")
    private Faculty faculty;

    @OneToOne
    @JoinColumn(name = "avatar_ID")
    private AvatarStudent avatar;

       public Student(Long id, String name, int age) {
        this.id = id;
        this.name =  Validator.validateName(name);
        this.age = Validator.validateNumber(age);
    }
    public Student() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void facultySet(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name =  Validator.validateName(name);
    }

    public void setAge(int age) {
        this.age = Validator.validateNumber(age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (age != student.age) return false;
        if (!Objects.equals(id, student.id)) return false;
        return Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}
