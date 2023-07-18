package antivoland.transporeon.model.graph;

public class Edge {
    public final EdgeType type;
    public final double distance;

    public Edge(EdgeType type, double distance) {
        this.type = type;
        this.distance = distance;
    }
}