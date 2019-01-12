package app.domain;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class ProfileLanguageId implements Serializable {

    private static final long serialVersionUID = 2654950033478480055L;
    private Profile profile;
    private Language language;

    @ManyToOne
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @ManyToOne
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileLanguageId that = (ProfileLanguageId) o;

        if (profile != null ? !profile.equals(that.profile) : that.profile != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (profile != null ? profile.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}
