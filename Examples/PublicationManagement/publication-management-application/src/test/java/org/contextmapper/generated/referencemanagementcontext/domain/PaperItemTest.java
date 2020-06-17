package org.contextmapper.generated.referencemanagementcontext.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.contextmapper.generated.referencemanagementcontext.web.rest.TestUtil;

public class PaperItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperItem.class);
        PaperItem paperItem1 = new PaperItem();
        paperItem1.setId(1L);
        PaperItem paperItem2 = new PaperItem();
        paperItem2.setId(paperItem1.getId());
        assertThat(paperItem1).isEqualTo(paperItem2);
        paperItem2.setId(2L);
        assertThat(paperItem1).isNotEqualTo(paperItem2);
        paperItem1.setId(null);
        assertThat(paperItem1).isNotEqualTo(paperItem2);
    }
}
