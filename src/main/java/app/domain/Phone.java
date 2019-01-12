package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Phone.
 */
@Entity
@Table(name = "phone")
public class Phone implements Serializable {


    private static final long serialVersionUID = -3875591856451509376L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public Phone number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        if (phone.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), phone.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Phone{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
