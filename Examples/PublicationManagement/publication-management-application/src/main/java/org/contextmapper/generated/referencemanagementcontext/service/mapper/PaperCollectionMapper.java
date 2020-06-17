package org.contextmapper.generated.referencemanagementcontext.service.mapper;


import org.contextmapper.generated.referencemanagementcontext.domain.*;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperCollectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaperCollection} and its DTO {@link PaperCollectionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaperCollectionMapper extends EntityMapper<PaperCollectionDTO, PaperCollection> {


    @Mapping(target = "paperItemLists", ignore = true)
    @Mapping(target = "removePaperItemList", ignore = true)
    PaperCollection toEntity(PaperCollectionDTO paperCollectionDTO);

    default PaperCollection fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaperCollection paperCollection = new PaperCollection();
        paperCollection.setId(id);
        return paperCollection;
    }
}
