package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * properties element in iBATIS configuration xml file
 */
public interface Settings extends DomElement {

    GenericAttributeValue<String> getCacheModelsEnabled();

    GenericAttributeValue<String> getEnhancementEnabled();

    GenericAttributeValue<String> getLazyLoadingEnabled();

    GenericAttributeValue<Integer> getMaxRequests();

    GenericAttributeValue<Integer> getMaxSessions();

    GenericAttributeValue<Integer> getMaxTransactions();

    @Attribute("useStatementNamespaces")
    GenericAttributeValue<String> getUseStatementNamespaces();

    GenericAttributeValue<Integer> getDefaultStatementTimeout();

    GenericAttributeValue<Integer> getStatementCachingEnabled();

    GenericAttributeValue<String> getClassInfoCacheEnabledd();
}