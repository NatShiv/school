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
public class EmblemFaculty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String filePath;
    long fileSize;
    String mediaType;
    @Lob
    byte[] preview;

    @OneToOne
    Faculty faculty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EmblemFaculty that = (EmblemFaculty) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
