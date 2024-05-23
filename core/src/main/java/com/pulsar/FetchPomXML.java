package com.pulsar;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FetchPomXML {

    private static XmlReader xmlReader = new XmlReader();

    public static class Artifact {

        public String groupId;
        public String artifactId;
        public String version;

        public XmlReader.Element pom;
        public String jarURL;

        public Array<Artifact> dependencies = new Array<>();

    }

    private static Artifact load(String artifactURL) {

        String[] parts = artifactURL.split(":");
        String groupId = parts[0];
        String artifactId = parts[1];
        String version = parts[2];

        String repositoryUrl = "https://repo.maven.apache.org/maven2/";
        String pomPath = groupId.replace('.', '/') + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".pom";

        try {
            URL url = new URL(repositoryUrl + pomPath);

            URLConnection connection = url.openConnection();

            connection.setRequestProperty("Accept", "application/xml");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println("Loading artifact!");

            Artifact artifact = new Artifact();
            artifact.groupId = groupId;
            artifact.artifactId = artifactId;
            artifact.version = version;

            XmlReader.Element element = xmlReader.parse(String.valueOf(content));
            artifact.pom = element;
            artifact.jarURL = repositoryUrl + String.format("%s/%s/%s/%s-%s.jar", groupId.replaceAll("\\.", "/"), artifactId, version, artifactId, version);

            if (element.hasChild("dependencies")) {
                Array<XmlReader.Element> dependencies = element.getChildByName("dependencies").getChildrenByName("dependency");

                for (XmlReader.Element dependency : dependencies) {

                    String depGroupId = dependency.getChildByName("groupId").getText();
                    String depArtifactId = dependency.getChildByName("artifactId").getText();
                    String depVersion = dependency.getChildByName("version").getText();
                    System.out.println("Loading Dependency :: " + depGroupId + ":" + depArtifactId + ":" + depVersion);
                    Artifact dependencyArtifact = load(depGroupId + ":" + depArtifactId + ":" + depVersion);

                    artifact.dependencies.add(dependencyArtifact);

                }
            }
            System.out.printf("Finished loading artifact %s!", artifactId);

            return artifact;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static void printJars(Artifact artifact) {
        System.out.println(artifact.jarURL);
        for (Artifact dependency : artifact.dependencies) {
            printJars(dependency);
        }
    }

    public static void main(String[] args) {
        Artifact artifact = load("com.badlogicgames.gdx:gdx:1.12.1");
        printJars(artifact);
    }
}
