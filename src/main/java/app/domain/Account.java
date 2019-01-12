package app.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * An account entity.
 */
@Entity
@Table(name = "account")

public class Account implements Serializable {


    private static final long serialVersionUID = -3051212108964687474L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotNull
    @Column(nullable = false, name = "is_activated")
    private boolean activated = false;

    @NotNull
    @Column(nullable = false, name = "is_recruiter")
    private boolean recruiter = false;

    @NotNull
    @Column(nullable = false, name = "should_receive_mailing")
    private boolean receiveMailing = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.account")
    private Set<AccountSkill> accountSkills = new HashSet<AccountSkill>(0);

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private Profile profile;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "user_account",
        joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isRecruiter() {
        return recruiter;
    }

    public void setRecruiter(boolean recruiter) {
        this.recruiter = recruiter;
    }

    public boolean isReceiveMailing() {
        return receiveMailing;
    }

    public void setReceiveMailing(boolean receiveMailing) {
        this.receiveMailing = receiveMailing;
    }

    public Set<AccountSkill> getAccountSkills() {
        return this.accountSkills;
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
}
