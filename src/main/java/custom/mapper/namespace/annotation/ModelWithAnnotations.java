package custom.mapper.namespace.annotation;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@MapperType("model_with_annotation")
@Data
public class ModelWithAnnotations {

    @MapperField("Model_name")
    private String name;

    @MapperField("list")
    private Collection<String> customList = new ArrayList<>();

    @MapperField("sub_model_list")
    private Collection<SubModel> subModelList = new ArrayList<>();

    @MapperField("sub_model_list_2")
    private Collection<SubModel> subModelList2 = new HashSet<>();

    //@MapperField("UnrecognizedSubModel")
    private UnrecognizedSubModel unrecognizedSubModel;
}
