package custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

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

        log.info("model_with_annotation=={}", sObject.getType());
        log.info("THIS IS MY NAME: DMD!=={}", sObject.getFields().get("default_namespace__Model_name"));
        log.info("\n{}", objectMapper.writeValueAsString(sObject));

        ModelWithAnnotations mwa2= mapper.writeValue(sObject,new ModelWithAnnotations() );


        log.info("==============================================================\n");
        log.info("==============================================================\n\n");

        log.info("=================== ORIGINAL ==============================");
        log.info(objectMapper.writeValueAsString(mwa));
        log.info("=================== WRITTEN AS VALE =======================");
        log.info(objectMapper.writeValueAsString(mwa2));

    }

    public static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }
}
