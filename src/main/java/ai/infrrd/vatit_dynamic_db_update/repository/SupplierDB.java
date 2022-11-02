package ai.infrrd.vatit_dynamic_db_update.repository;


import ai.infrrd.vatit_dynamic_db_update.entities.SupplierData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDB extends JpaRepository<SupplierData,Integer> {
//    @Query("CREATE TABLE aircraft_backup AS SELECT * FROM u")
//    List<SupplierData> cloneTable();


}
