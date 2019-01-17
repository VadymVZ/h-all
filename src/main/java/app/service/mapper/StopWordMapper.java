package app.service.mapper;

import app.domain.StopWord;
import app.service.UserAccountService;
import app.service.dto.StopWordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity StopWord and its DTO StopWordDTO.
 */
@Mapper(componentModel = "spring", uses = {UserAccountService.class})
public interface StopWordMapper extends EntityMapper<StopWordDTO, StopWord> {


    @Override
    @Mapping(source = "account.id", target = "accountId")
    StopWordDTO toDto(StopWord entity);

    @Override
    @Mapping(source = "accountId", target = "account.id")
    StopWord toEntity(StopWordDTO dto);

    default StopWord fromId(Long id) {
        if (id == null) {
            return null;
        }
        StopWord stopWord = new StopWord();
        stopWord.setId(id);
        return stopWord;
    }

}
