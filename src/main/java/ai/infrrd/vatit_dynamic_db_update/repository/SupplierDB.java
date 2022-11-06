package ai.infrrd.vatit_dynamic_db_update.repository;

import ai.infrrd.vatit_dynamic_db_update.model.DataSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDB extends JpaRepository<DataSupplier,Integer> {
    @Modifying
    @Query(value = "truncate table DataSupplier", nativeQuery = true)
    void truncateTable();

}
