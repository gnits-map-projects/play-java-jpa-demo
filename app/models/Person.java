package models;

import javax.persistence.*;

@Entity(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(unique=true)
    public String name;

    public String city;

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
