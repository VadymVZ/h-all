package app.service.mapper;

import app.domain.*;
import app.service.UserAccountService;
import app.service.dto.ContactDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contact and its DTO ContactDTO.
 */
@Mapper(componentModel = "spring", uses = {UserAccountService.class})
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {


    @Override
    @Mapping(source = "account", target = "accountId")
    ContactDTO toDto(Contact entity);

    @Override
    @Mapping(source = "accountId", target = "account")
    Contact toEntity(ContactDTO dto);

    default Contact fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
}
