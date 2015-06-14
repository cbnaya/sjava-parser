package parser;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class ComplexItrator<T> {
    public ComplexItrator(Iterator<T> iterator)
    {
        this.iterator = iterator;
    }

    public boolean hasNext()
    {
        if (null != nextElement)
        {
            return true;
        }

        return iterator.hasNext();
    }

    public T next()
    {
        if (!hasNext())
        {
            throw new NoSuchElementException();
        }
        if (null != nextElement)
        {
            T result = nextElement;
            nextElement = null;
            return result;
        }
        else
        {
            return iterator.next();
        }
    }

    public T current()
    {
        return currentElement;
    }

    public T getNext()
    {
        if (!hasNext())
        {
            throw new NoSuchElementException();
        }
        if (null == nextElement)
        {
            nextElement = next();
        }

        return nextElement;
    }

    private Iterator<T> iterator;
    private T currentElement;
    private T nextElement;
}
