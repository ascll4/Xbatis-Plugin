package com.github.ansafari.plugin.xbatis.domain;

import com.intellij.jam.model.common.CommonDomModelElement;

import java.util.List;

public interface SqlMap extends CommonDomModelElement {

    List<Insert> getInserts();

    List<Delete> getDeletes();

    List<Select> getSelects();

    List<Update> getUpdates();

}
