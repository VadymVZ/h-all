package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A SkillLevel.
 */
@Entity
@Table(name = "skill_level")
public class SkillLevel implements Serializable {


    private static final long serialVersionUID = 6399663443472096019L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skillLevel")
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

    public SkillLevel name(String name) {
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
        SkillLevel skillLevel = (SkillLevel) o;
        if (skillLevel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skillLevel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SkillLevel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
