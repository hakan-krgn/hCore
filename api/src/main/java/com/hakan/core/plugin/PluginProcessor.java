package com.hakan.core.plugin;

import com.hakan.core.utils.Validate;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation processor to create
 * a plugin.yml file automatically
 * at compile time.
 */
@SupportedAnnotationTypes("com.hakan.core.plugin.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_18)
public final class PluginProcessor extends AbstractProcessor {

    /**
     * File creating process method.
     *
     * @param annotations The annotation interfaces requested to be processed
     * @param roundEnv    Environment for information about the current and prior round
     * @return Whether the set of annotations are claimed by this processor
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Plugin.class)) {
            Plugin annotation = element.getAnnotation(Plugin.class);

            if (annotation == null)
                continue;

            Validate.isFalse(element.getKind().isClass(), "plugin annotation can only be used on classes!");
            Validate.isTrue(annotation.version().isEmpty(), "version cannot be empty!");
            Validate.isTrue(annotation.name().isEmpty(), "name cannot be empty!");


            Map<String, Object> data = new LinkedHashMap<>();
            if (!annotation.name().isEmpty())
                data.put("name", annotation.name());
            if (!annotation.version().isEmpty())
                data.put("version", annotation.version());
            if (!annotation.description().equals(""))
                data.put("description", annotation.description());
            if (!annotation.apiVersion().equals(""))
                data.put("api-version", annotation.apiVersion());
            if (!annotation.load().equals(""))
                data.put("load", annotation.load());
            if (!annotation.website().equals(""))
                data.put("website", annotation.website());
            if (annotation.authors().length != 0)
                data.put("authors", annotation.authors());
            if (!annotation.prefix().equals(""))
                data.put("prefix", annotation.prefix());
            if (annotation.depends().length != 0)
                data.put("depend", annotation.depends());
            if (annotation.softDepends().length != 0)
                data.put("softdepend", annotation.softDepends());
            if (annotation.loadBefore().length != 0)
                data.put("loadbefore", annotation.loadBefore());
            if (annotation.libraries().length != 0)
                data.put("libraries", annotation.libraries());

            TypeElement typeElement = (TypeElement) element;
            data.putIfAbsent("name", typeElement.getSimpleName().toString());
            data.putIfAbsent("main", typeElement.getQualifiedName().toString());
            data.putIfAbsent("version", System.currentTimeMillis());


            try {
                Filer filer = this.processingEnv.getFiler();
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml");
                Writer writer = resource.openWriter();

                Yaml yaml = new Yaml();
                yaml.dump(data, writer);

                writer.flush();
                writer.close();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}