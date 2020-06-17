package org.contextmapper.generated.referencemanagementcontext.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaperItemMapperTest {

    private PaperItemMapper paperItemMapper;

    @BeforeEach
    public void setUp() {
        paperItemMapper = new PaperItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paperItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paperItemMapper.fromId(null)).isNull();
    }
}
