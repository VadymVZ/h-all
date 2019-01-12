package app.service.mapper;

import app.domain.Contact;
import app.domain.UserAccount;
import app.service.UserAccountService;
import app.service.dto.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Contact and its DTO ContactDTO.
 */
@Mapper(componentModel = "spring", uses = {UserAccountService.class})
public interface UserAccountMapper {



    default UserAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setId(id);
        return userAccount;
    }
}
