package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
public class Language implements Serializable {

    private static final long serialVersionUID = -8936245127936461705L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.language", cascade=CascadeType.ALL)
    private Set<ProfileLanguage> profileLanguages = new HashSet<ProfileLanguage>();

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

    public Language name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Set<ProfileLanguage> getProfileLanguages() {
        return profileLanguages;
    }

    public void setProfileLanguages(Set<ProfileLanguage> profileLanguages) {
        this.profileLanguages = profileLanguages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Language language = (Language) o;
        if (language.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), language.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
