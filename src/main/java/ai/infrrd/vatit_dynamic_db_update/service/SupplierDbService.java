package ai.infrrd.vatit_dynamic_db_update.service;

import ai.infrrd.vatit_dynamic_db_update.repository.SupplierDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplierDbService {
    @Autowired
    SupplierDB supplierDB;

    @Transactional
    public void truncateMyTable() {
        supplierDB.truncateTable();
    }

}
