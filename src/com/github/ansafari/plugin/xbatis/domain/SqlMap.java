package com.github.ansafari.plugin.xbatis.domain;

import java.util.List;

public interface SqlMap extends com.intellij.util.xml.DomElement {

    List<Insert> getInserts();

    List<Delete> getDeletes();

    List<Select> getSelects();

    List<Update> getUpdates();

}
