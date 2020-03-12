package com.es.core.preparer;

import java.util.Map;

public interface ParametersPreparer<T> {
    Object[] getPreparedParameters(T T);

    Map<String, Object> fillMapForSaving(T T);
}
