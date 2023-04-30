package org.ziegelbauer.homepage.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 10000)
    private String title;
    private String body;
    private String author;
    private Date created;
    private Date modified;
}
