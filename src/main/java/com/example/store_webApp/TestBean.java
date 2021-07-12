package com.example.store_webApp;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Objects;

@ApplicationScoped
@Named
public class TestBean implements Serializable {
    private String name = "iagl";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBean testBean = (TestBean) o;
        return Objects.equals(name, testBean.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
