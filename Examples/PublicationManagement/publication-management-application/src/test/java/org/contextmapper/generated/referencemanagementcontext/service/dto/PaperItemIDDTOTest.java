package org.contextmapper.generated.referencemanagementcontext.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.contextmapper.generated.referencemanagementcontext.web.rest.TestUtil;

public class PaperItemIDDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperItemIDDTO.class);
        PaperItemIDDTO paperItemIDDTO1 = new PaperItemIDDTO();
        paperItemIDDTO1.setId(1L);
        PaperItemIDDTO paperItemIDDTO2 = new PaperItemIDDTO();
        assertThat(paperItemIDDTO1).isNotEqualTo(paperItemIDDTO2);
        paperItemIDDTO2.setId(paperItemIDDTO1.getId());
        assertThat(paperItemIDDTO1).isEqualTo(paperItemIDDTO2);
        paperItemIDDTO2.setId(2L);
        assertThat(paperItemIDDTO1).isNotEqualTo(paperItemIDDTO2);
        paperItemIDDTO1.setId(null);
        assertThat(paperItemIDDTO1).isNotEqualTo(paperItemIDDTO2);
    }
}
