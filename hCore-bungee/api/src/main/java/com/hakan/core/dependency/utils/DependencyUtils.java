package com.hakan.core.dependency.utils;

import com.hakan.core.configuration.utils.ConfigUtils;
import com.hakan.core.dependency.DependencyAttribute;
import com.hakan.core.utils.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DependencyUtils class to save
 * jar from website to folder and
 * load it to classpath.
 */
public final class DependencyUtils {

    private static final Pattern PATTERN = Pattern.compile("(?=\\d)[\\w\\\\.-]+");

    /**
     * Downloads jar from
     * website to dependency folder.
     *
     * @param attribute The dependency attribute.
     */
    public static void downloadJar(@Nonnull DependencyAttribute attribute) {
        Validate.notNull(attribute, "attribute cannot be null!");

        try {
            File saveFile = new File(attribute.asSavePath());
            if (saveFile.exists()) return;

            URL website = new URL(attribute.asJarUrl());
            InputStream in = website.openStream();

            ConfigUtils.createFile(saveFile.getPath());
            Files.copy(in, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads jar from
     * website to dependency folder.
     *
     * @param attribute The dependency attribute.
     */
    public static void downloadJars(@Nonnull DependencyAttribute attribute) {
        Validate.notNull(attribute, "attribute cannot be null!");

        List<DependencyAttribute> dependencyAttributes = DependencyUtils.findAttributes(attribute);
        dependencyAttributes.forEach(DependencyUtils::downloadJar);
    }

    /**
     * Loads the dependencies from
     * the given url text.
     *
     * @param clazz The class for loader instance.
     */
    public static void loadJar(@Nonnull Class<?> clazz,
                               @Nonnull DependencyAttribute attribute) {
        try {
            Validate.notNull(clazz, "class cannot be null!");

            URL url = new File(attribute.asSavePath()).toURI().toURL();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(clazz.getClassLoader(), url);
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save jar from website to folder.
     *
     * @param clazz     The class for loader instance.
     * @param attribute Main dependency attribute.
     */
    public static void loadJars(@Nonnull Class<?> clazz,
                                @Nonnull DependencyAttribute attribute) {
        Validate.notNull(attribute, "attribute cannot be null!");

        List<DependencyAttribute> dependencyAttributes = DependencyUtils.findAttributes(attribute);
        dependencyAttributes.forEach(_attribute -> DependencyUtils.loadJar(clazz, _attribute));
    }



    /**
     * Finds the dependencies from
     * the given main dependency and
     * returns all the sub dependencies
     * in a linked list.
     *
     * @param attribute Main dependency attribute.
     */
    public static List<DependencyAttribute> findAttributes(@Nonnull DependencyAttribute attribute) {
        Validate.notNull(attribute, "attribute cannot be null!");


        List<DependencyAttribute> attributes = new LinkedList<>();

        try {
            URL webUrl = new URL(attribute.asPomUrl());
            URLConnection connection = webUrl.openConnection();
            connection.addRequestProperty("User-Agent", "jar-agent");
            connection.connect();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(connection.getInputStream());
            doc.getDocumentElement().normalize();



            Map<String, String> properties = new HashMap<>();
            NodeList propertyList = doc.getElementsByTagName("properties");
            for (int itr = 0; itr < propertyList.getLength(); itr++) {
                Node node = propertyList.item(itr);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                Element element = (Element) node;
                NodeList nodeList = element.getChildNodes();

                int bound = nodeList.getLength();
                for (int i = 0; i < bound; i++) {
                    Node _node = nodeList.item(i);
                    if (_node.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    Element _element = (Element) _node;
                    properties.put(_element.getNodeName(), _element.getTextContent());
                }
            }


            NodeList dependencyList = doc.getElementsByTagName("dependency");
            for (int itr = 0; itr < dependencyList.getLength(); itr++) {
                Node node = dependencyList.item(itr);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                Element element = (Element) node;

                String website = attribute.getWebsite();
                String savePath = attribute.getSavePath();
                String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
                String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
                String version = element.getElementsByTagName("version").item(0).getTextContent();

                if (element.getElementsByTagName("scope").item(0) != null) {
                    String scope = element.getElementsByTagName("scope").item(0).getTextContent();
                    if (scope.equalsIgnoreCase("provided"))
                        continue;
                    else if (scope.equalsIgnoreCase("system"))
                        continue;
                    else if (scope.equalsIgnoreCase("test"))
                        continue;
                }

                for (Map.Entry<String, String> entry : properties.entrySet())
                    if (groupId.contains("${" + entry.getKey() + "}"))
                        groupId = groupId.replace("${" + entry.getKey() + "}", entry.getValue());
                for (Map.Entry<String, String> entry : properties.entrySet())
                    if (artifactId.contains("${" + entry.getKey() + "}"))
                        artifactId = artifactId.replace("${" + entry.getKey() + "}", entry.getValue());
                for (Map.Entry<String, String> entry : properties.entrySet())
                    if (version.contains("${" + entry.getKey() + "}"))
                        version = version.replace("${" + entry.getKey() + "}", entry.getValue());


                Matcher matcher = PATTERN.matcher(version);
                if (matcher.find()) version = matcher.group();

                attributes.add(new DependencyAttribute(groupId, artifactId, version, website, savePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        attributes.add(attribute);
        return attributes;
    }
}
