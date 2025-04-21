/*
 * Copyright 2025 Intent Exchange, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.drone_highway.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import lombok.Setter;

/** ModelMapperのユーティリティ。 */
@Component
public class ModelMapperUtil {
  @Setter
  private static ModelMapper modelMapper;

  /** オブジェクトマッパー */
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * ソースオブジェクトを指定されたターゲットクラスの新しいインスタンスにマッピングします。
   *
   * @param <S> ソースオブジェクトの型
   * @param <T> ターゲットクラスの型
   * @param source マッピングするソースオブジェクト
   * @param targetClass ソースオブジェクトをマッピングするターゲットクラス
   * @return ソースオブジェクトからマッピングされたターゲットクラスの新しいインスタンス
   */
  public static <S, T> T map(S source, Class<T> targetClass) {
    return modelMapper.map(source, targetClass);
  }

  /**
   * ソースオブジェクトをターゲットオブジェクトにマッピングします。
   *
   * @param <S> ソースオブジェクトの型
   * @param <T> ターゲットオブジェクトの型
   * @param source マッピングするソースオブジェクト
   * @param target マッピングされるターゲットオブジェクト
   * @return ソースオブジェクトからマッピングされたターゲットオブジェクト
   */
  public static <S, T> T merge(S source, T target) {
    modelMapper.map(source, target);
    return target;
  }

  /**
   * Listのソースオブジェクトを指定されたターゲットクラスの新しいインスタンスにマッピングします。
   *
   * @param <S> ソースオブジェクトの型
   * @param <T> ターゲットクラスの型
   * @param source マッピングするソースオブジェクト
   * @param targetClass ソースオブジェクトをマッピングするターゲットクラス
   * @return ソースオブジェクトからマッピングされたターゲットクラスの新しいインスタンス
   */
  public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
    return source.stream().map(m -> map(m, targetClass)).collect(Collectors.toList());
  }

  /**
   * マップのリストをDTOのリストに変換します。
   *
   * @param listMap 変換するマップのリスト
   * @param clazz DTOのクラス
   * @param <T> DTOの型
   * @return DTOのリスト
   */
  public static <T> List<T> convertListMapToListDto(List<Map<String, Object>> listMap,
      Class<T> clazz) {
    return listMap.stream().map(map -> convertMapToDto(map, clazz)).collect(Collectors.toList());
  }

  /**
   * マップをDTOに変換します。
   *
   * @param map 変換するマップ
   * @param clazz DTOのクラス
   * @param <T> DTOの型
   * @return 変換されたDTO
   */
  private static <T> T convertMapToDto(Map<String, Object> map, Class<T> clazz) {
    return objectMapper.convertValue(map, clazz);
  }

  /**
   * DTOをマップに変換します。
   *
   * @param dto 変換するDTO
   * @param <T> DTOの型
   * @return 変換されたマップ
   */
  @SuppressWarnings("unchecked")
  public static <T> Map<String, Object> convertDtoToMap(T dto) {
    return objectMapper.convertValue(dto, Map.class);
  }

  /**
   * DTOのリストをマップのリストに変換します。
   *
   * @param listDto 変換するDTOのリスト
   * @param <T> DTOの型
   * @return 変換されたマップのリスト
   */
  public static <T> List<Map<String, Object>> convertListDtoToListMap(List<T> listDto) {
    return listDto.stream().map(map -> convertDtoToMap(map)).collect(Collectors.toList());
  }

  /**
   * JSON文字列をマップに変換します。
   *
   * @param jsonString 変換するJSON文字列
   * @return 変換されたマップ
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> jsonToMap(String jsonString) {
    try {
      return objectMapper.readValue(jsonString, Map.class);
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage(), e);
    }
  }

  /**
   * JSON文字列をマップのリストに変換します。
   * 
   * @param jsonString
   * @return 変換されたマップのリスト
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  public static List<Map<String, Object>> jsonToListMap(String jsonString) {
    try {
      return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage(), e);
    }
  }

  /**
   * マップをJSON文字列に変換します。
   *
   * @param map 変換するマップ
   * @return 変換されたJSON文字列
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  public static String mapToJson(Map<String, Object> map) {
    try {
      return objectMapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage(), e);
    }
  }

  /**
   * StringをList<List<Double>>に変換します。
   * 
   * @param areaInfo エリア情報
   * @return List<List<Double>>
   * @throws IOException
   */
  public static List<List<Double>> listToString(String string) throws IOException {
    List<List<Double>> airSpace = new ArrayList<>();
    JsonNode rootNode = objectMapper.readTree(string);
    JsonNode coordinatesNode = rootNode.path("coordinates").get(0);
    for (JsonNode coordinate : coordinatesNode) {
      List<Double> coordinatesList = new ArrayList<>();
      coordinatesList.add(coordinate.get(0).asDouble());
      coordinatesList.add(coordinate.get(1).asDouble());
      airSpace.add(coordinatesList);
    }
    return airSpace;
  }

  /**
   * ObjectリストをJSON文字列に変換します。
   * 
   * @param listObject 変換するobjectのリスト
   * @return 変換されたJSON文字列
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  public static String convertListObjectToJson(List<Object> listObject) {
    try {
      return objectMapper.writeValueAsString(listObject);
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage(), e);
    }
  }

  /**
   * JSON文字列をクラスにに変換します。
   *
   * @param jsonString 変換するJSON文字列
   * @return 変換されたマップ
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  @SuppressWarnings("unchecked")
  public static <T> Object jsonStrToClass(String jsonString, Class<T> targetClass) {
    try {
      return objectMapper.readValue(jsonString, targetClass);
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage(), e);
    }
  }

  /**
   * JSON文字列をクラスリストにに変換します。
   *
   * @param jsonString 変換するJSON文字列
   * @return 変換されたマップ
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> jsonStrToListClass(String jsonString, Class<T> targetClass) {
    try {
      return objectMapper.readValue(jsonString,
          objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage(), e);
    }
  }
}

