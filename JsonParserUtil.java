import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParserUtil {

    public static Object parseJson(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser = factory.createParser(jsonString);

        return parseJsonToken(parser);
    }

    private static Object parseJsonToken(JsonParser parser) throws Exception {
        JsonToken token = parser.nextToken();

        if (token == JsonToken.START_OBJECT) {
            Map<String, Object> map = new HashMap<>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                parser.nextToken(); // move to value
                map.put(fieldName, parseJsonToken(parser));
            }
            return map;
        } else if (token == JsonToken.START_ARRAY) {
            List<Object> list = new ArrayList<>();
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                list.add(parseJsonToken(parser));
            }
            return list;
        } else if (token == JsonToken.VALUE_STRING) {
            return parser.getText();
        } else if (token == JsonToken.VALUE_NUMBER_INT) {
            return new BigInteger(parser.getText());
        } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            return new BigDecimal(parser.getText());
        } else if (token == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        } else if (token == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        } else if (token == JsonToken.VALUE_NULL) {
            return null;
        }

        throw new IllegalStateException("Unexpected token: " + token);
    }

    public static void main(String[] args) {
        String jsonString = "{\"name\":\"John\", \"age\":30, \"salary\":12345.67, \"isMarried\":false, \"children\":[\"Ann\",\"Billy\"]}";

        try {
            Object result = parseJson(jsonString);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
