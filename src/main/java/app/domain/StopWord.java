package app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A StopWord.
 */
@Entity
@Table(name = "stop_word")
public class StopWord implements Serializable {


    private static final long serialVersionUID = 3935526336592791735L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word_value")
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private UserAccount account;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public StopWord key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StopWord stopWord = (StopWord) o;
        if (stopWord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stopWord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StopWord{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            "}";
    }
}
