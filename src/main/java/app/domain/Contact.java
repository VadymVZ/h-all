package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
public class Contact implements Serializable {


    private static final long serialVersionUID = -3416317058200765953L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "skype")
    private String skype;

    @Column(name = "github")
    private String github;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "email")
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private UserAccount account;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "contact")
    private Set<Phone> phones = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "contact")
    private Set<Photo> photos = new HashSet<>();

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

    public Contact name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public Contact lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSkype() {
        return skype;
    }

    public Contact skype(String skype) {
        this.skype = skype;
        return this;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getGithub() {
        return github;
    }

    public Contact github(String github) {
        this.github = github;
        return this;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getTelegram() {
        return telegram;
    }

    public Contact telegram(String telegram) {
        this.telegram = telegram;
        return this;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public Contact linkedin(String linkedin) {
        this.linkedin = linkedin;
        return this;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getEmail() {
        return email;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        if (contact.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contact.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", skype='" + getSkype() + "'" +
            ", github='" + getGithub() + "'" +
            ", telegram='" + getTelegram() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
