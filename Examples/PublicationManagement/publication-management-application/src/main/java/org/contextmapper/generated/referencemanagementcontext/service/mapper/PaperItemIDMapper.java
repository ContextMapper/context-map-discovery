package org.contextmapper.generated.referencemanagementcontext.service.mapper;


import org.contextmapper.generated.referencemanagementcontext.domain.*;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemIDDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaperItemID} and its DTO {@link PaperItemIDDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaperItemIDMapper extends EntityMapper<PaperItemIDDTO, PaperItemID> {



    default PaperItemID fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaperItemID paperItemID = new PaperItemID();
        paperItemID.setId(id);
        return paperItemID;
    }
}
