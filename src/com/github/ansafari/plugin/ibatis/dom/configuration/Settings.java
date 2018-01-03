package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * properties element in iBATIS configuration xml file
 */
public interface Settings extends DomElement {

    public GenericAttributeValue<String> getCacheModelsEnabled();

    public GenericAttributeValue<String> getEnhancementEnabled();

    public GenericAttributeValue<String> getLazyLoadingEnabled();

    public GenericAttributeValue<Integer> getMaxRequests();

    public GenericAttributeValue<Integer> getMaxSessions();

    public GenericAttributeValue<Integer> getMaxTransactions();

    @Attribute("useStatementNamespaces")
    public GenericAttributeValue<String> getUseStatementNamespaces();

    public GenericAttributeValue<Integer> getDefaultStatementTimeout();

    public GenericAttributeValue<Integer> getStatementCachingEnabled();

    public GenericAttributeValue<String> getClassInfoCacheEnabledd();
}