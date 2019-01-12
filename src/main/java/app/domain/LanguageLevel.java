package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A LanguageLevel.
 */
@Entity
@Table(name = "language_level")
public class LanguageLevel implements Serializable {


    private static final long serialVersionUID = 6314146614150381538L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "languageLevel")
    private Set<ProfileLanguage> profileLanguages = new HashSet<ProfileLanguage>(0);


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

    public LanguageLevel name(String name) {
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
        LanguageLevel languageLevel = (LanguageLevel) o;
        if (languageLevel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languageLevel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LanguageLevel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
