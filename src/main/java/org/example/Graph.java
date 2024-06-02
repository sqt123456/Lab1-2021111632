package org.example;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Link;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public class Graph {
    private Map<String, Map<String, Integer>> adjacencyList = new HashMap<>();

    public void addEdge(String from, String to) {
        adjacencyList.putIfAbsent(from, new HashMap<>());
        Map<String, Integer> edges = adjacencyList.get(from);
        edges.put(to, edges.getOrDefault(to, 0) + 1);
    }

    public void printGraph() {
        for (String key : adjacencyList.keySet()) {
            System.out.println("From " + key + ":");
            Map<String, Integer> edges = adjacencyList.get(key);
            for (String edge : edges.keySet()) {
                System.out.println("  To " + edge + ", Weight: " + edges.get(edge));
            }
        }
    }
    /*
    public void createGraphVisualization() {
        MutableGraph g = mutGraph("example").setDirected(true);
        adjacencyList.forEach((node, edges) -> {
            MutableNode fromNode = mutNode(node).add(Color.BLACK).add(Style.FILLED, Color.WHITE.fill());
            edges.forEach((target, weight) -> {
                MutableNode toNode = mutNode(target).add(Color.BLACK).add(Style.FILLED, Color.WHITE.fill());
                Link link = to(toNode).with(Label.of(String.valueOf(weight)), Style.BOLD, Color.BLACK.font());
                fromNode.addLink(link);
            });
            g.add(fromNode);
        });

        try {
            Graphviz.fromGraph(g).height(700).render(Format.PNG).toFile(new File("src/main/resources/graph_2.png"));
            System.out.println("Graph has been rendered and saved to 'src/main/resources/graph_2.png'");
        } catch (Exception e) {
            System.out.println("Error rendering graph: " + e.getMessage());
        }
    }
    */

    public void createGraphVisualization(List<String> shortestPath) {
        MutableGraph g = mutGraph("example").setDirected(true);
        Set<String> pathSet = (shortestPath != null) ? new HashSet<>(shortestPath) : new HashSet<>();

        adjacencyList.forEach((node, edges) -> {
            MutableNode fromNode = mutNode(node).add(Color.BLACK).add(Style.FILLED, Color.WHITE.fill());
            edges.forEach((target, weight) -> {
                MutableNode toNode = mutNode(target).add(Color.BLACK).add(Style.FILLED, Color.WHITE.fill());
                Link link = to(toNode).with(Label.of(String.valueOf(weight)), Style.BOLD, Color.BLACK.font());
                if (pathSet.contains(node) && pathSet.contains(target) && pathSet.size() > 1) {
                    if (isPathEdge(node, target, shortestPath)) {
                        link = link.with(Color.RED, Style.BOLD);
                    }
                }
                fromNode.addLink(link);
            });
            g.add(fromNode);
        });

        String filePath = shortestPath == null ? "src/main/resources/graph_1.png" : "src/main/resources/graph_4.png";
        try {
            Graphviz.fromGraph(g).height(700).render(Format.PNG).toFile(new File(filePath));
            System.out.println("Graph has been rendered and saved to '" + filePath + "'");
        } catch (Exception e) {
            System.out.println("Error rendering graph: " + e.getMessage());
        }
    }

    private boolean isPathEdge(String node, String target, List<String> path) {
        if (path == null || path.isEmpty()) return false;
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i).equals(node) && path.get(i + 1).equals(target)) {
                return true;
            }
        }
        return false;
    }





    public static Graph createGraphFromFile(String filePath) {
        Graph graph = new Graph();
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Replace all non-letter characters with space, and normalize white spaces
                contentBuilder.append(line).append(" ");
            }
        } catch (IOException e) {
            System.out.println("Failed to read the file: " + e.getMessage());
            return null;
        }

        String content = contentBuilder.toString();
        // Replace all non-alphabetic characters with space and trim multiple spaces to a single space
        content = content.replaceAll("[^a-zA-Z ]", " ").replaceAll("\\s+", " ").trim();
        String[] words = content.toLowerCase().split("\\s+");

        String previousWord = null;
        for (String word : words) {
            if (!word.isEmpty()) {
                if (previousWord != null) {
                    graph.addEdge(previousWord, word);
                }
                previousWord = word;
            }
        }
        return graph;
    }

    public void showDirectedGraph() {
        // This method will now call the function to create a visual representation of the graph
        createGraphVisualization(null);
        System.out.println("The directed graph visualization has been generated.");
    }


    //来获取邻接表
    public Map<String, Map<String, Integer>> getAdjacencyList() {
        return adjacencyList;
    }



    /*
    private static Graph createGraphFromFile(String filePath) {
        Graph graph = new Graph();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String previousWord = null;
            while ((line = reader.readLine()) != null) {
                for (String word : line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+")) {
                    if (!word.isEmpty()) {
                        if (previousWord != null) {
                            graph.addEdge(previousWord, word);
                        }
                        previousWord = word;
                    }
                }
                //previousWord = null; // 重置以避免行间连接
            }
        } catch (IOException e) {
            System.out.println("Failed to read the file: " + e.getMessage());
        }
        return graph;
    }

     */


}
