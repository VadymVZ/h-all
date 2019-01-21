package app.service.mapper;

import app.domain.UserAccount;
import app.service.UserAccountService;
import app.service.dto.UserAccountDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity UserAccount and its DTO UserAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {UserAccountService.class, AccountSkillMapper.class})
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount>{


    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "activated", target = "activated")
    @Mapping(source = "recruiter", target = "recruiter")
    @Mapping(source = "receiveMailing", target = "receiveMailing")
    @Mapping(source = "accountSkills", target = "accountSkills")
    UserAccountDTO toDto(UserAccount entity);

    @Override
    @InheritInverseConfiguration
    UserAccount toEntity(UserAccountDTO dto);

}
