package ai.infrrd.vatit_dynamic_db_update.batch;

import ai.infrrd.vatit_dynamic_db_update.model.DataSupplier;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class Processor implements ItemProcessor<DataSupplier, DataSupplier> {

    @Override
    public DataSupplier process(DataSupplier dataSupplier) throws Exception {
        return dataSupplier;
    }
}
