package oop.ex6.parser;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * this iterator is more powerful iterator. the additional function is :
 * 1. get current - return the current object of the iterator
 * 2. get next - return the next element but not change the iterator location
 * (as opposed to next function)
 *
 * @param <T> - the iterator elements type
 */
public class ComplexIterator<T> implements Iterator<T> {
    //save the inner iterator
    private Iterator<T> iterator;
    //save the current element
    private T currentElement;
    //save the next element
    private T nextElement;

    /**
     * Ctor
     *
     * @param iterator - the basic iterator to wrap
     */
    public ComplexIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return (null != nextElement) || (iterator.hasNext());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (null != nextElement) {
            currentElement = nextElement;
        } else {
            currentElement = iterator.next();
        }
        nextElement = null;
        return currentElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * return the current element
     *
     * @return the current element
     */
    public T current() {
        return currentElement;
    }

    /**
     * return the next element but don't change the iterator position
     *
     * @return the next element
     */
    public T getNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (null == nextElement) {
            nextElement = iterator.next();
        }

        return nextElement;
    }
}