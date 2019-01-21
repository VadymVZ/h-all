package app.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "account")
public class UserAccount implements Serializable {


    private static final long serialVersionUID = -1556376301143779146L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "is_activated")
    private Boolean activated;

    @Column(nullable = false, name = "is_recruiter")
    private Boolean recruiter;

    @Column(nullable = false, name = "should_receive_mailing")
    private Boolean receiveMailing;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.account", cascade = CascadeType.ALL)
    private Set<AccountSkill> accountSkills = new HashSet<AccountSkill>(0);

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
    private Profile profile;

    @JsonIgnore
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private Contact contact;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "user_account",
        joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<StopWord> stopWords = new HashSet<>();

    public Set<StopWord> getStopWords() {
        return stopWords;
    }

    public void setStopWords(Set<StopWord> stopWords) {
        this.stopWords = stopWords;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActivated() {
        return activated;
    }

    public UserAccount activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean isRecruiter() {
        return recruiter;
    }

    public UserAccount recruiter(Boolean recruiter) {
        this.recruiter = recruiter;
        return this;
    }

    public void setRecruiter(Boolean recruiter) {
        this.recruiter = recruiter;
    }

    public Boolean isReceiveMailing() {
        return receiveMailing;
    }

    public UserAccount receiveMailing(Boolean receiveMailing) {
        this.receiveMailing = receiveMailing;
        return this;
    }

    public void setReceiveMailing(Boolean receiveMailing) {
        this.receiveMailing = receiveMailing;
    }

    public String getName() {
        return name;
    }

    public UserAccount name(String name) {
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAccount userAccount = (UserAccount) o;
        if (userAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", activated='" + isActivated() + "'" +
            ", recruiter='" + isRecruiter() + "'" +
            ", receiveMailing='" + isReceiveMailing() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
