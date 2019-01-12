package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
public class Profile implements Serializable {


    private static final long serialVersionUID = 8002207902150148269L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salary_amount", precision = 10, scale = 2)
    private BigDecimal salaryAmount;

    @Column(name = "city")
    private String city;

    @Column(name = "description")
    private String description;

    @Column(name = "experience", precision = 10, scale = 2)
    private BigDecimal experience;

    @Column(name = "job_expectations")
    private String jobExpectations;

    @Column(name = "achievements")
    private String achievements;

    @Column(name = "position_name")
    private String positionName;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Account account;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "profile_occupation",
        joinColumns = {@JoinColumn(name = "profile_id", nullable = false, updatable = false)},
        inverseJoinColumns = {@JoinColumn(name = "occupation_id", nullable = false, updatable = false)})
    private Set<Occupation> occupations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.profile")
    private Set<ProfileLanguage> profileLanguages = new HashSet<ProfileLanguage>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    public Profile salaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
        return this;
    }

    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getCity() {
        return city;
    }

    public Profile city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public Profile description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExperience() {
        return experience;
    }

    public Profile experience(BigDecimal experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(BigDecimal experience) {
        this.experience = experience;
    }

    public String getJobExpectations() {
        return jobExpectations;
    }

    public Profile jobExpectations(String jobExpectations) {
        this.jobExpectations = jobExpectations;
        return this;
    }

    public void setJobExpectations(String jobExpectations) {
        this.jobExpectations = jobExpectations;
    }

    public String getAchievements() {
        return achievements;
    }

    public Profile achievements(String achievements) {
        this.achievements = achievements;
        return this;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getPositionName() {
        return positionName;
    }

    public Profile positionName(String positionName) {
        this.positionName = positionName;
        return this;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Occupation> getOccupations() {
        return occupations;
    }

    public void setOccupations(Set<Occupation> occupations) {
        this.occupations = occupations;
    }

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
        Profile profile = (Profile) o;
        if (profile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", salaryAmount=" + getSalaryAmount() +
            ", city='" + getCity() + "'" +
            ", description='" + getDescription() + "'" +
            ", experience=" + getExperience() +
            ", jobExpectations='" + getJobExpectations() + "'" +
            ", achievements='" + getAchievements() + "'" +
            ", positionName='" + getPositionName() + "'" +
            "}";
    }
}
