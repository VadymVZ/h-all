package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
public class Photo implements Serializable {


    private static final long serialVersionUID = -2421928330952561158L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "value")
    private byte[] value;

    @Column(name = "value_content_type")
    private String valueContentType;

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getValue() {
        return value;
    }

    public Photo value(byte[] value) {
        this.value = value;
        return this;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getValueContentType() {
        return valueContentType;
    }

    public Photo valueContentType(String valueContentType) {
        this.valueContentType = valueContentType;
        return this;
    }

    public void setValueContentType(String valueContentType) {
        this.valueContentType = valueContentType;
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
        Photo photo = (Photo) o;
        if (photo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), photo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", valueContentType='" + getValueContentType() + "'" +
            "}";
    }
}
