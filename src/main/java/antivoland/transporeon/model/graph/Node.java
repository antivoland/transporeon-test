package antivoland.transporeon.model.graph;

import antivoland.transporeon.model.route.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;

@EqualsAndHashCode
@AllArgsConstructor
public class Node {
    /* todo: remove
    public static final Comparator<Node> COMPARATOR = Comparator
            .comparing((Node node) -> node.type)
            .thenComparing((Node node) -> node.id);
     */

    public final NodeType type;
    public final int id;
    // public Route shortestRoute;

//    public Node(NodeType type, int id) {
//        this.type = type;
//        this.id = id;
//    }
}