package custom;

import custom.mapper.MapperException;
import custom.mapper.namespace.NamespaceMapper;
import custom.mapper.namespace.SObject;
import custom.mapper.namespace.annotation.ModelWithAnnotations;
import custom.mapper.namespace.annotation.SubModel;
import custom.mapper.namespace.annotation.UnrecognizedSubModel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Application {

    public static void main(String[] args) throws ClassNotFoundException, IOException, MapperException {
        setLoggingLevel(ch.qos.logback.classic.Level.INFO);

        SubModel sm = new SubModel();
        sm.setName("THIS IS sub-model-name: WOOHA! (1)");
        SubModel sm2 = new SubModel();
        sm2.setName("THIS IS sub-model-name: WOOHA! (2)");

        UnrecognizedSubModel ursbm = new UnrecognizedSubModel();
        ursbm.setName("UNRECOGNIZED");

        ModelWithAnnotations mwa = new ModelWithAnnotations();
        mwa.setName("THIS IS MY NAME: DMD!");
        mwa.getCustomList().add("Field_1_in_list");
        mwa.getCustomList().add("Field_2_in_list");
        mwa.getCustomList().add("Field_3_in_list");
        mwa.getCustomList().add("Field_4_in_list");
        mwa.getSubModelList().add(sm);
        mwa.getSubModelList().add(sm2);
        mwa.getSubModelList2().add(sm);
        mwa.getSubModelList2().add(sm2);
        mwa.setUnrecognizedSubModel(ursbm);

        NamespaceMapper mapper = new NamespaceMapper();
        SObject sObject = mapper.readValue(mwa);

        assert "model_with_annotation".equals(sObject.getType());
        assert"THIS IS MY NAME: DMD!=={}".equals(sObject.getFields().get("default_namespace__Model_name"));

        ModelWithAnnotations mwa2 = mapper.writeValue(sObject, new ModelWithAnnotations());


        log.info("<<·······················································>>");
        log.info("=================== SOBJECT ==============================");
        log.info(sObject.toString());
        log.info("=================== ORIGINAL ==============================");
        log.info(mwa.toString());
        log.info("=================== WRITTEN AS VALE =======================");
        log.info(mwa2.toString());

    }

    public static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }
}
