package app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the Profile entity. This class is used in ProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter salaryAmount;

    private StringFilter city;

    private StringFilter description;

    private BigDecimalFilter experience;

    private StringFilter jobExpectations;

    private StringFilter achievements;

    private StringFilter positionName;

    public ProfileCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(BigDecimalFilter salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getExperience() {
        return experience;
    }

    public void setExperience(BigDecimalFilter experience) {
        this.experience = experience;
    }

    public StringFilter getJobExpectations() {
        return jobExpectations;
    }

    public void setJobExpectations(StringFilter jobExpectations) {
        this.jobExpectations = jobExpectations;
    }

    public StringFilter getAchievements() {
        return achievements;
    }

    public void setAchievements(StringFilter achievements) {
        this.achievements = achievements;
    }

    public StringFilter getPositionName() {
        return positionName;
    }

    public void setPositionName(StringFilter positionName) {
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
        final ProfileCriteria that = (ProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(salaryAmount, that.salaryAmount) &&
            Objects.equals(city, that.city) &&
            Objects.equals(description, that.description) &&
            Objects.equals(experience, that.experience) &&
            Objects.equals(jobExpectations, that.jobExpectations) &&
            Objects.equals(achievements, that.achievements) &&
            Objects.equals(positionName, that.positionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        salaryAmount,
        city,
        description,
        experience,
        jobExpectations,
        achievements,
        positionName
        );
    }

    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (salaryAmount != null ? "salaryAmount=" + salaryAmount + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (experience != null ? "experience=" + experience + ", " : "") +
                (jobExpectations != null ? "jobExpectations=" + jobExpectations + ", " : "") +
                (achievements != null ? "achievements=" + achievements + ", " : "") +
                (positionName != null ? "positionName=" + positionName + ", " : "") +
            "}";
    }

}
