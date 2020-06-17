package org.contextmapper.generated.referencemanagementcontext.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.contextmapper.generated.referencemanagementcontext.web.rest.TestUtil;

public class PaperCollectionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperCollection.class);
        PaperCollection paperCollection1 = new PaperCollection();
        paperCollection1.setId(1L);
        PaperCollection paperCollection2 = new PaperCollection();
        paperCollection2.setId(paperCollection1.getId());
        assertThat(paperCollection1).isEqualTo(paperCollection2);
        paperCollection2.setId(2L);
        assertThat(paperCollection1).isNotEqualTo(paperCollection2);
        paperCollection1.setId(null);
        assertThat(paperCollection1).isNotEqualTo(paperCollection2);
    }
}
