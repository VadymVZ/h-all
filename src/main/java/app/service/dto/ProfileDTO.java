package app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Profile entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    private BigDecimal salaryAmount;

    private String city;

    private String description;

    private BigDecimal experience;

    private String jobExpectations;

    private String achievements;

    private String positionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExperience() {
        return experience;
    }

    public void setExperience(BigDecimal experience) {
        this.experience = experience;
    }

    public String getJobExpectations() {
        return jobExpectations;
    }

    public void setJobExpectations(String jobExpectations) {
        this.jobExpectations = jobExpectations;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (profileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
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
