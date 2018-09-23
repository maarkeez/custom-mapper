package custom.mapper.namespace;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SObject {
    private Map<String, Object> fields = new HashMap<>();
    private String type;
}
