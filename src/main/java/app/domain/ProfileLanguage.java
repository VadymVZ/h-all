package app.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "profile_language")
@AssociationOverrides({
    @AssociationOverride(name = "pk.profile", joinColumns = @JoinColumn(name = "profile_id")),
    @AssociationOverride(name = "pk.language", joinColumns = @JoinColumn(name = "language_id"))
})
public class ProfileLanguage implements Serializable {

    private static final long serialVersionUID = -2298281755192499001L;
    private ProfileLanguageId pk = new ProfileLanguageId();
    private LanguageLevel languageLevel;

    public ProfileLanguage() {
    }

    public ProfileLanguage(ProfileLanguageId pk, LanguageLevel languageLevel) {
        this.pk = pk;
        this.languageLevel = languageLevel;
    }

    @EmbeddedId
    public ProfileLanguageId getPk() {
        return pk;
    }

    public void setPk(ProfileLanguageId pk) {
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
    public Language getLanguage(){
        return getPk().getLanguage();
    }

    public void setLanguage(Language language){
        getPk().setLanguage(language);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_level_id", nullable = false)
    public LanguageLevel getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(LanguageLevel languageLevel) {
        this.languageLevel = languageLevel;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProfileLanguage that = (ProfileLanguage) o;

        if (getPk() != null ? !getPk().equals(that.getPk())
            : that.getPk() != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
