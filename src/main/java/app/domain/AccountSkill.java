package app.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "account_skill")
@AssociationOverrides({
    @AssociationOverride(name = "pk.skill", joinColumns = @JoinColumn(name = "skill_id")),
    @AssociationOverride(name = "pk.account", joinColumns = @JoinColumn(name = "account_id"))
})
public class AccountSkill implements Serializable {

    private AccountSkillId pk = new AccountSkillId();
    private SkillLevel skillLevel;

    public AccountSkill() {
    }

    public AccountSkill(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    @EmbeddedId
    public AccountSkillId getPk() {
        return pk;
    }

    public void setPk(AccountSkillId pk) {
        this.pk = pk;
    }

    @Transient
    public Skill getSkill(){
        return getPk().getSkill();
    }


    public void setSkill(Skill skill){
        getPk().setSkill(skill);
    }

    @Transient
    public UserAccount getAccount(){
        return getPk().getAccount();
    }

    public void setAccount(UserAccount account){
        getPk().setAccount(account);
    }

    @ManyToOne
    @JoinColumn(name = "skill_level_id", nullable = false)
    public SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AccountSkill that = (AccountSkill) o;

        if (getPk() != null ? !getPk().equals(that.getPk())
            : that.getPk() != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
