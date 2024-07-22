package bds.common;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Wrapper around Stream instance that implements Iterable Interface.
 * @param <T> the object type.
 */
public class StreamIterable<T> implements Iterable<T> {
    private final Stream<T> stream;

    /**
     * Argument constructor for StreamIterable.
     * @param stream The stream from which iterator data is extracted.
     */
    public StreamIterable(Stream<T> stream) {
        this.stream = stream;
    }

    /**
     * Method to get the iterator.
     * @return the iterator/
     */
    @Override
    public Iterator<T> iterator() {
        return stream.iterator();
    }

    /**
     * Method to create a new StreamIterable object from a Stream object.
     */
    public static <T> StreamIterable<T> of(Stream<T> stream) {
        return new StreamIterable<>(stream);
    }
}