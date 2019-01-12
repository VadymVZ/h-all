package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CvFile.
 */
@Entity
@Table(name = "cv_file")
public class CvFile implements Serializable {


    private static final long serialVersionUID = 1702170676698270191L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "cv")
    private byte[] cv;

    @Column(name = "cv_content_type")
    private String cvContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getCv() {
        return cv;
    }

    public CvFile cv(byte[] cv) {
        this.cv = cv;
        return this;
    }

    public void setCv(byte[] cv) {
        this.cv = cv;
    }

    public String getCvContentType() {
        return cvContentType;
    }

    public CvFile cvContentType(String cvContentType) {
        this.cvContentType = cvContentType;
        return this;
    }

    public void setCvContentType(String cvContentType) {
        this.cvContentType = cvContentType;
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
        CvFile cvFile = (CvFile) o;
        if (cvFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cvFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CvFile{" +
            "id=" + getId() +
            ", cv='" + getCv() + "'" +
            ", cvContentType='" + getCvContentType() + "'" +
            "}";
    }
}
