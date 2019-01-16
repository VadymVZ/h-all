package app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Photo entity.
 */
public class PhotoDTO implements Serializable {

    private Long id;
    private byte[] value;
    private String valueContentType;
    private Long contactId;
    private String contactName;
    private String contactLastName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getValueContentType() {
        return valueContentType;
    }

    public void setValueContentType(String valueContentType) {
        this.valueContentType = valueContentType;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoDTO photoDTO = (PhotoDTO) o;
        if (photoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), photoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


}
