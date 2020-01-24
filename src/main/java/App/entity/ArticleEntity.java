package App.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "article")
public class ArticleEntity extends BaseEntity{
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "creation_date", nullable = false)
    private Date creation_date;

    @Column(columnDefinition="TEXT", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
