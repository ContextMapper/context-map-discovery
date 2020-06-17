package org.contextmapper.generated.referencemanagementcontext.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaperCollectionMapperTest {

    private PaperCollectionMapper paperCollectionMapper;

    @BeforeEach
    public void setUp() {
        paperCollectionMapper = new PaperCollectionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paperCollectionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paperCollectionMapper.fromId(null)).isNull();
    }
}
