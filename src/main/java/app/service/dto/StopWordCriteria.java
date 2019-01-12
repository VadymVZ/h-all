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
 * Criteria class for the StopWord entity. This class is used in StopWordResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stop-words?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StopWordCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    public StopWordCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getKey() {
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StopWordCriteria that = (StopWordCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        key
        );
    }

    @Override
    public String toString() {
        return "StopWordCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
            "}";
    }

}
