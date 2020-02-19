package com.es.core.model.phone;

import java.util.Objects;

public class Color {
    private Long id;
    private String code;

    public Color() {
    }

    public Color(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.code);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Color)) {
            return false;
        }
        Color color = (Color) obj;
        return Objects.equals(this.id, color.id) && Objects.equals(this.code, color.code);
    }
}
