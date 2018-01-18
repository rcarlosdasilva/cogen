package io.github.rcarlosdasilva.cogen.converter;

import io.github.rcarlosdasilva.cogen.config.convention.JavaType;

public interface DbTypeConverter {

  JavaType convert(String type);

}
