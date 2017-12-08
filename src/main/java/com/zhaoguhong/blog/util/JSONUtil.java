package com.zhaoguhong.blog.util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JSONUtil {
  public static final String DEFAULT_DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
  public static final String IGNORE_PROPERTEIS_FILTER = "ignoreProperteisFilter";

  public static ObjectMapper buildObjectMapper() {
    return buildObjectMapper(null);
  }

  public static ObjectMapper buildObjectMapper(String dateFormat) {
    if (dateFormat == null) {
      dateFormat = "yyyy-MM-dd HH:mm:ss";
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat(dateFormat));
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  public static <T> List<T> jsonToList(String json, Class<T> clazz) {
    ObjectMapper mapper = buildObjectMapper();

    JavaType javaType = mapper
        .getTypeFactory().constructCollectionType(List.class, clazz);
    try {
      return ((List) mapper.readValue(json, javaType));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String prettyJSON(Object object) {
    String json = null;
    ObjectMapper mapper = buildObjectMapper();
    try {
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return json;
  }

  public static String toJSON(Object object) {
    return toJSON(object, "yyyy-MM-dd HH:mm:ss");
  }

  public static String toJSON(Object object, String dateFormat) {
    ObjectMapper mapper = buildObjectMapper(dateFormat);
    StringWriter writer = new StringWriter();
    try {
      mapper.writeValue(writer, object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  public static String toJSON(Object object, String[] ignoreProperties) {
    ObjectMapper mapper = buildObjectMapper();
    StringWriter writer = new StringWriter();
//    mapper.addMixIn(Object.class, PropertyFilterMixIn.class);
    FilterProvider filterProvider = new SimpleFilterProvider().addFilter("ignoreProperteisFilter",
        SimpleBeanPropertyFilter.serializeAllExcept(ignoreProperties));
    try {
      mapper.writer(filterProvider).writeValue(writer, object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

//  public static String toXML(Object object) {
//    XmlMapper xmlMapper = new XmlMapper();
//    try {
//      return xmlMapper.writeValueAsString(object);
//    } catch (JsonProcessingException e) {
//    }
//    return null;
//  }

  public static JsonNode toJsonNode(Object obj) {
    if (obj == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    return mapper.valueToTree(obj);
  }

  public static JsonNode toJsonNode(String json) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    try {
      return mapper.readTree(json);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    try {
      JavaType javaType = mapper.getTypeFactory()
          .constructCollectionType(List.class, clazz);

      return ((List) mapper.readValue(json, javaType));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<String, Object> toMap(Object obj) {
    ObjectMapper mapper = buildObjectMapper();

    JavaType javaType = mapper
        .getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class,
            new Class[] {String.class, Object.class});

    Map props = (Map) mapper.convertValue(obj, javaType);
    return props;
  }

  public static Map<String, Object> toMap(String json) {
    if (json == null) {
      return null;
    }
    ObjectMapper mapper = buildObjectMapper();
    try {
      JavaType javaType = mapper
          .getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class,
              new Class[] {String.class, Object.class});

      return ((Map) mapper.readValue(json, javaType));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object toObject(String json) {
    return toObject(json, Object.class);
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    ObjectMapper mapper = buildObjectMapper();
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object toObject(TreeNode treeNode) {
    return toObject(treeNode, Object.class);
  }

  public static <T> T toObject(TreeNode treeNode, Class<T> clazz) {
    ObjectMapper mapper = buildObjectMapper();
    try {
      return mapper.treeToValue(treeNode, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

//  public static Map<String, Object> xmlToMap(String xml) {
//    if (xml == null) {
//      return null;
//    }
//    JacksonXmlModule module = new JacksonXmlModule();
//    XmlMapper xmlMapper = new XmlMapper(module);
//    Map content = null;
//    try {
//      content = (Map) xmlMapper.readValue(xml, Map.class);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return content;
//  }
}