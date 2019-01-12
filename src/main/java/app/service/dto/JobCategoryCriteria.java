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
 * Criteria class for the JobCategory entity. This class is used in JobCategoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /job-categories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCategoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter parentId;

    private StringFilter name;

    public JobCategoryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobCategoryCriteria that = (JobCategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        parentId,
        name
        );
    }

    @Override
    public String toString() {
        return "JobCategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
            "}";
    }

}
