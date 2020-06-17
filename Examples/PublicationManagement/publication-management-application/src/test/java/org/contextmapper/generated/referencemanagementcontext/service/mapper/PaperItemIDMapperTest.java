package org.contextmapper.generated.referencemanagementcontext.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaperItemIDMapperTest {

    private PaperItemIDMapper paperItemIDMapper;

    @BeforeEach
    public void setUp() {
        paperItemIDMapper = new PaperItemIDMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paperItemIDMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paperItemIDMapper.fromId(null)).isNull();
    }
}
