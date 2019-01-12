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

/**
 * Criteria class for the Contact entity. This class is used in ContactResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contacts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter lastName;

    private StringFilter skype;

    private StringFilter github;

    private StringFilter telegram;

    private StringFilter linkedin;

    private StringFilter email;

    public ContactCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getSkype() {
        return skype;
    }

    public void setSkype(StringFilter skype) {
        this.skype = skype;
    }

    public StringFilter getGithub() {
        return github;
    }

    public void setGithub(StringFilter github) {
        this.github = github;
    }

    public StringFilter getTelegram() {
        return telegram;
    }

    public void setTelegram(StringFilter telegram) {
        this.telegram = telegram;
    }

    public StringFilter getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(StringFilter linkedin) {
        this.linkedin = linkedin;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactCriteria that = (ContactCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(skype, that.skype) &&
            Objects.equals(github, that.github) &&
            Objects.equals(telegram, that.telegram) &&
            Objects.equals(linkedin, that.linkedin) &&
            Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        lastName,
        skype,
        github,
        telegram,
        linkedin,
        email
        );
    }

    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (skype != null ? "skype=" + skype + ", " : "") +
                (github != null ? "github=" + github + ", " : "") +
                (telegram != null ? "telegram=" + telegram + ", " : "") +
                (linkedin != null ? "linkedin=" + linkedin + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
            "}";
    }

}
