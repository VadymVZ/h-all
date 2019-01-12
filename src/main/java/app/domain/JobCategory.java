package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A JobCategory.
 */
@Entity
@Table(name = "job_category")
public class JobCategory implements Serializable {


    private static final long serialVersionUID = 4043403455148506503L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public JobCategory parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public JobCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobCategory jobCategory = (JobCategory) o;
        if (jobCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobCategory{" +
            "id=" + getId() +
            ", parentId=" + getParentId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
