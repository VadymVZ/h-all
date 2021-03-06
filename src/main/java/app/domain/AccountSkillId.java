package app.domain;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class AccountSkillId implements Serializable {

    private static final long serialVersionUID = -7320448872869136400L;

    private Skill skill;
    private UserAccount account;

    @ManyToOne
    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @ManyToOne
    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountSkillId that = (AccountSkillId) o;

        if (skill != null ? !skill.equals(that.skill) : that.skill != null) return false;
        if (account != null ? !account.equals(that.account) : that.account != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (skill != null ? skill.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
