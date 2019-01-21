package app.service.mapper;

import app.domain.AccountSkill;
import app.service.SkillLevelService;
import app.service.SkillService;
import app.service.UserAccountService;
import app.service.dto.AccountSkillDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity AccountSkill and its DTO AccountSkillDTO.
 */
@Mapper(componentModel = "spring", uses = {UserAccountService.class, SkillService.class, SkillLevelService.class})
public interface AccountSkillMapper extends EntityMapper<AccountSkillDTO, AccountSkill> {


    @Override
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName")
    @Mapping(source = "skill.id", target = "skillId")
    @Mapping(source = "skill.name", target = "skillName")
    @Mapping(source = "skillLevel.id", target = "skillLevelId")
    @Mapping(source = "skillLevel.name", target = "skillLevelName")
    AccountSkillDTO toDto(AccountSkill entity);

    @Override
    @InheritInverseConfiguration
    AccountSkill toEntity(AccountSkillDTO dto);

}
