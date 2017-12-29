package com.github.ansafari.plugin.setting;

import com.github.ansafari.plugin.generate.GenerateModel;
import com.github.ansafari.plugin.generate.GenerateModelUtils;
import com.github.ansafari.plugin.generate.StatementGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

import static com.github.ansafari.plugin.generate.StatementGeneratorUtils.*;

/**
 * XbatisSettingStorage.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/28 20:36
 */
@State(name = "XbatisSettingStorage", storages = @Storage(id = "other", file = "$APP_CONFIG$/XbatisSettingStorage.xml"))
public class XbatisSettingStorage implements PersistentStateComponent<Element> {

    private GenerateModel statementGenerateModel;

    private Gson gson = new Gson();

    public XbatisSettingStorage() {
        this.statementGenerateModel = GenerateModelUtils.START_WITH_MODEL;
    }

    public static XbatisSettingStorage getInstance() {
        return ServiceManager.getService(XbatisSettingStorage.class);
    }

    @Nullable
    @Override
    public Element getState() {
        Element element = new Element("XbatisSettingStorage");
        element.setAttribute(INSERT_GENERATOR.getId(), gson.toJson(INSERT_GENERATOR.getPatterns()));
        element.setAttribute(DELETE_GENERATOR.getId(), gson.toJson(DELETE_GENERATOR.getPatterns()));
        element.setAttribute(UPDATE_GENERATOR.getId(), gson.toJson(UPDATE_GENERATOR.getPatterns()));
        element.setAttribute(SELECT_GENERATOR.getId(), gson.toJson(SELECT_GENERATOR.getPatterns()));
        element.setAttribute("statementGenerateModel", String.valueOf(statementGenerateModel.getIdentifier()));
        return element;
    }

    @Override
    public void loadState(Element element) {
        loadState(element, INSERT_GENERATOR);
        loadState(element, DELETE_GENERATOR);
        loadState(element, UPDATE_GENERATOR);
        loadState(element, SELECT_GENERATOR);
        String identifier = element.getAttributeValue("statementGenerateModel");

        if (StringUtils.isBlank(identifier)) {
            identifier = "0";
        }
        statementGenerateModel = GenerateModelUtils.getInstance(identifier);
    }

    private Type gsonTypeToken = new TypeToken<Set<String>>() {
    }.getType();

    private void loadState(Element state, StatementGenerator generator) {
        String attribute = state.getAttributeValue(generator.getId());
        if (null != attribute) {
            generator.setPatterns(gson.fromJson(attribute, gsonTypeToken));
        }
    }

    public GenerateModel getStatementGenerateModel() {
        return statementGenerateModel;
    }

    public void setStatementGenerateModel(GenerateModel statementGenerateModel) {
        this.statementGenerateModel = statementGenerateModel;
    }
}
