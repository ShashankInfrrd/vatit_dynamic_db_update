package ai.infrrd.vatit_dynamic_db_update.batch;

import ai.infrrd.vatit_dynamic_db_update.model.DataSupplier;
import ai.infrrd.vatit_dynamic_db_update.repository.SupplierDB;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<DataSupplier> {

    @Autowired
    private SupplierDB supplierDB;

    @Autowired
    public DBWriter (SupplierDB supplierDB) {
        this.supplierDB = supplierDB;
    }

    @Override
    public void write(List<? extends DataSupplier> dataSupplier) throws Exception{

        System.out.println("Data saved for dataSupplier "+ dataSupplier.toString());
        supplierDB.saveAll(dataSupplier);
//        supplierDB.save(dataSupplier.get(0));


    }
}
