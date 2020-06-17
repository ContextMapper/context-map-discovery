package org.contextmapper.generated.referencemanagementcontext.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.contextmapper.generated.referencemanagementcontext.web.rest.TestUtil;

public class PaperCollectionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperCollectionDTO.class);
        PaperCollectionDTO paperCollectionDTO1 = new PaperCollectionDTO();
        paperCollectionDTO1.setId(1L);
        PaperCollectionDTO paperCollectionDTO2 = new PaperCollectionDTO();
        assertThat(paperCollectionDTO1).isNotEqualTo(paperCollectionDTO2);
        paperCollectionDTO2.setId(paperCollectionDTO1.getId());
        assertThat(paperCollectionDTO1).isEqualTo(paperCollectionDTO2);
        paperCollectionDTO2.setId(2L);
        assertThat(paperCollectionDTO1).isNotEqualTo(paperCollectionDTO2);
        paperCollectionDTO1.setId(null);
        assertThat(paperCollectionDTO1).isNotEqualTo(paperCollectionDTO2);
    }
}
