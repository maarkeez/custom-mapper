package custom.mapper.namespace.annotation;

import lombok.Data;

@MapperTypeCustom("SubModel")
@Data
public class SubModel {

    @MapperFieldCustom("SubModelName")
    private String name;

}
