package ai.infrrd.vatit_dynamic_db_update.config;

//import ai.infrrd.vatit_dynamic_db_update.entities.SupplierData;
import ai.infrrd.vatit_dynamic_db_update.model.DataSupplier;
import org.springframework.batch.item.ItemProcessor;


public class UserItemProcessor implements ItemProcessor<DataSupplier, DataSupplier> {
    @Override
    public DataSupplier process(DataSupplier dataSupplier) throws Exception {
        return null;
    }
}
