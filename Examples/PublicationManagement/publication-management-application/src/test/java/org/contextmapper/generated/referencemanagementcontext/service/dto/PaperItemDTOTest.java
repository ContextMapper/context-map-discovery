package org.contextmapper.generated.referencemanagementcontext.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.contextmapper.generated.referencemanagementcontext.web.rest.TestUtil;

public class PaperItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperItemDTO.class);
        PaperItemDTO paperItemDTO1 = new PaperItemDTO();
        paperItemDTO1.setId(1L);
        PaperItemDTO paperItemDTO2 = new PaperItemDTO();
        assertThat(paperItemDTO1).isNotEqualTo(paperItemDTO2);
        paperItemDTO2.setId(paperItemDTO1.getId());
        assertThat(paperItemDTO1).isEqualTo(paperItemDTO2);
        paperItemDTO2.setId(2L);
        assertThat(paperItemDTO1).isNotEqualTo(paperItemDTO2);
        paperItemDTO1.setId(null);
        assertThat(paperItemDTO1).isNotEqualTo(paperItemDTO2);
    }
}
