package org.contextmapper.generated.referencemanagementcontext.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.contextmapper.generated.referencemanagementcontext.web.rest.TestUtil;

public class PaperItemIDTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperItemID.class);
        PaperItemID paperItemID1 = new PaperItemID();
        paperItemID1.setId(1L);
        PaperItemID paperItemID2 = new PaperItemID();
        paperItemID2.setId(paperItemID1.getId());
        assertThat(paperItemID1).isEqualTo(paperItemID2);
        paperItemID2.setId(2L);
        assertThat(paperItemID1).isNotEqualTo(paperItemID2);
        paperItemID1.setId(null);
        assertThat(paperItemID1).isNotEqualTo(paperItemID2);
    }
}
