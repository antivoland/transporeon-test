package antivoland.transporeon.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Edge {
    enum Type {AIR, GROUND}

    public final Type type;
    public final double distance;
}