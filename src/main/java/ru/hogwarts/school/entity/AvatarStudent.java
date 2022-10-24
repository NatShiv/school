package ru.hogwarts.school.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AvatarStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String filePath;
    long fileSize;
    String mediaType;
    byte[] preview;
    @OneToOne
    Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AvatarStudent that = (AvatarStudent) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
