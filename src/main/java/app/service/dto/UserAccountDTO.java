package app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the UserAccount entity.
 */
public class UserAccountDTO implements Serializable {


    private static final long serialVersionUID = 3369349386743048118L;
    private Long id;
    private String name;
    private Boolean activated;
    private Boolean recruiter;
    private Boolean receiveMailing;
    private Set<AccountSkillDTO> accountSkills;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Boolean recruiter) {
        this.recruiter = recruiter;
    }

    public Boolean getReceiveMailing() {
        return receiveMailing;
    }

    public void setReceiveMailing(Boolean receiveMailing) {
        this.receiveMailing = receiveMailing;
    }

    public Set<AccountSkillDTO> getAccountSkills() {
        return accountSkills;
    }

    public void setAccountSkills(Set<AccountSkillDTO> accountSkills) {
        this.accountSkills = accountSkills;
    }

   /* public Set<AccountSkill> getAccountSkills() {
        return accountSkills;
    }

    public void setAccountSkills(Set<AccountSkill> accountSkills) {
        this.accountSkills = accountSkills;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAccountDTO userAccountDTO = (UserAccountDTO) o;
        if (userAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
