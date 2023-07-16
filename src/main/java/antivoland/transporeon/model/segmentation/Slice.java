package antivoland.transporeon.model.segmentation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
abstract class Slice {
    final SliceType type;

    abstract Segment segmentFor(double lon);
}