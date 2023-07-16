package antivoland.transporeon.model.segmentation;

class PoleSlice extends Slice {
    private final Segment segment = new Segment();

    PoleSlice(SliceType type) {
        super(type);
    }

    @Override
    Segment segmentFor(double lon) {
        return segment;
    }
}