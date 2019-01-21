package app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AccountSkill entity.
 */
public class AccountSkillDTO implements Serializable {

    private Long accountId;
    private String accountName;
    private Long skillId;
    private String skillName;
    private Long skillLevelId;
    private String skillLevelName;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Long getSkillLevelId() {
        return skillLevelId;
    }

    public void setSkillLevelId(Long skillLevelId) {
        this.skillLevelId = skillLevelId;
    }

    public String getSkillLevelName() {
        return skillLevelName;
    }

    public void setSkillLevelName(String skillLevelName) {
        this.skillLevelName = skillLevelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountSkillDTO that = (AccountSkillDTO) o;
        return Objects.equals(getAccountId(), that.getAccountId()) &&
            Objects.equals(getSkillId(), that.getSkillId()) &&
            Objects.equals(getSkillLevelId(), that.getSkillLevelId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getSkillId(), getSkillLevelId());
    }

    @Override
    public String toString() {
        return "AccountSkillDTO{" +
            "accountId=" + accountId +
            ", accountName='" + accountName + '\'' +
            ", skillId=" + skillId +
            ", skillName='" + skillName + '\'' +
            ", skillLevelId=" + skillLevelId +
            ", skillLevelName='" + skillLevelName + '\'' +
            '}';
    }
}
