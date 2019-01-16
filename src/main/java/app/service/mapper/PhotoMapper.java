package app.service.mapper;

import app.domain.Photo;
import app.service.ContactService;
import app.service.dto.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Photo and its DTO PhotoDTO.
 */
@Mapper(componentModel = "spring", uses = {ContactService.class})
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {


    @Override
    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "contact.name", target = "contactName")
    @Mapping(source = "contact.lastName", target = "contactLastName")
    PhotoDTO toDto(Photo entity);

    @Override
    @Mapping(source = "contactId", target = "contact.id")
    @Mapping(source = "contactName", target = "contact.name")
    @Mapping(source = "contactLastName", target = "contact.lastName")
    Photo toEntity(PhotoDTO dto);

    default Photo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Photo photo = new Photo();
        photo.setId(id);
        return photo;
    }

}
