package org.contextmapper.generated.referencemanagementcontext.repository;

import org.contextmapper.generated.referencemanagementcontext.domain.PaperItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PaperItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaperItemRepository extends JpaRepository<PaperItem, Long> {
}
