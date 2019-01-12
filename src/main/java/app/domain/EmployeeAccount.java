package app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A EmployeeAccount.
 */
@Entity
@Table(name = "employee_account")
public class EmployeeAccount implements Serializable {


    private static final long serialVersionUID = -4730622405888271691L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Min(value = 2)
    @Column(name = "age", nullable = false)
    private Integer age;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public EmployeeAccount firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public EmployeeAccount lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public EmployeeAccount age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
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
        EmployeeAccount employeeAccount = (EmployeeAccount) o;
        if (employeeAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeeAccount{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
