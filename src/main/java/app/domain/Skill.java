package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
public class Skill implements Serializable {


    private static final long serialVersionUID = -5028935102192131827L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.skill", cascade=CascadeType.ALL)
    private Set<AccountSkill> accountSkills = new HashSet<AccountSkill>(0);

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Skill name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Set<AccountSkill> getAccountSkills() {
        return accountSkills;
    }

    public void setAccountSkills(Set<AccountSkill> accountSkills) {
        this.accountSkills = accountSkills;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Skill skill = (Skill) o;
        if (skill.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
