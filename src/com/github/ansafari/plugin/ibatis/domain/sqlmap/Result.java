package com.github.ansafari.plugin.ibatis.domain.sqlmap;

import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.impl.PomTargetPsiElementImpl;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public interface Result extends DomElement {

    @NameValue
    @Attribute("property")
    GenericAttributeValue<String> getProperty();

    @Attribute("typeHandler")
    GenericAttributeValue<TypeAlias> getTypeHandler();

    @Attribute("resultMap")
    @Referencing(ResultMapReferenceConverter.class)
    GenericAttributeValue<ResultMap> getResultMap();

    class ResultMapReferenceConverter implements CustomReferenceConverter {

        @NotNull
        @Override
        public PsiReference[] createReferences(GenericDomValue genericDomValue, PsiElement element, ConvertContext context) {
            return new PsiReference[]{new ResultMapReference(element)};
        }

    }

    class ResultMapReference extends PsiReferenceBase<PsiElement> {

        private static final Pattern dotPattern = Pattern.compile("\\.");

        ResultMapReference(@NotNull PsiElement element) {
            super(element);
        }

        @Override
        public PsiElement resolve() {

            String rawText = getElement().getText();
            if (rawText.length() <= 2) {
                return null;
            }

            String[] split = dotPattern.split(rawText.substring(1, rawText.length() - 1), 2);

            String namespace;
            String id;

            if (split.length == 2) {
                namespace = split[0];
                id = split[1];
            } else {
                namespace = "";
                id = split[0];
            }

            CommonProcessors.FindFirstProcessor<ResultMap> processor = new CommonProcessors.FindFirstProcessor<>();
            ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processResultMaps(namespace, id, processor);
            ResultMap foundValue = processor.getFoundValue();
            if (foundValue != null) {
                DomTarget target = DomTarget.getTarget(foundValue);
                if (target != null) {
                    XmlElement xmlElement = foundValue.getXmlElement();
                    final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
                    return new PomTargetPsiElementImpl(target) {
                        @Override
                        public String getLocationString() {
                            return locationString;
                        }
                    };
                }
            }

            return null;

        }

        @NotNull
        @Override
        public Object[] getVariants() {
            CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<>();
            ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processResultMapNames(processor);
            return processor.toArray(new String[processor.getResults().size()]);
        }

    }

}
