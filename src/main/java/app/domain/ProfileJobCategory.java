package app.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "profile_job_category")
@AssociationOverrides({
    @AssociationOverride(name = "pk.profile", joinColumns = @JoinColumn(name = "profile_id")),
    @AssociationOverride(name = "pk.category", joinColumns = @JoinColumn(name = "job_category_id"))
})
public class ProfileJobCategory implements Serializable {

    private static final long serialVersionUID = -2298281755192499001L;
    private ProfileJobCategoryId pk = new ProfileJobCategoryId();
    private boolean mainCategory = false;

    public ProfileJobCategory() {
    }

    public ProfileJobCategory(ProfileJobCategoryId pk) {
        this.pk = pk;
    }

    public ProfileJobCategory(ProfileJobCategoryId pk, boolean mainCategory) {
        this.pk = pk;
        this.mainCategory = mainCategory;
    }

    @EmbeddedId
    public ProfileJobCategoryId getPk() {
        return pk;
    }

    public void setPk(ProfileJobCategoryId pk) {
        this.pk = pk;
    }

    @Transient
    public Profile getProfile(){
        return getPk().getProfile();
    }

    public void setProfile(Profile profile){
        getPk().setProfile(profile);
    }

    @Transient
    public JobCategory getJobCategory(){
        return getPk().getJobCategory();
    }

    public void setJobCategory(JobCategory jobCategory){
        getPk().setJobCategory(jobCategory);
    }

    @NotNull
    @Column(nullable = false, name = "is_main_category")
    public boolean isMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(boolean mainCategory) {
        this.mainCategory = mainCategory;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProfileJobCategory that = (ProfileJobCategory) o;

        if (getPk() != null ? !getPk().equals(that.getPk())
            : that.getPk() != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
