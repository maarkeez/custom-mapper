package custom.mapper.namespace.annotation;

import lombok.Data;

@MapperType("SubModel")
@Data
public class SubModel {

    @MapperField("SubModelName")
    private String name;

}
