package org.iu.chess.common;

import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ObservableSet<T> implements Iterable<T> {
  private final Set<Consumer<T>> changeListener = Sets.newHashSet();
  private final Set<T> delegate;

  private ObservableSet(Set<T> delegate) {
    this.delegate = delegate;
  }

  public void addChangeListener(Consumer<T> listener) {
    changeListener.add(listener);
  }

  @Override
  public Iterator<T> iterator() {
    return delegate.iterator();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    delegate.forEach(action);
  }

  @Override
  public Spliterator<T> spliterator() {
    return delegate.spliterator();
  }

  public boolean add(T value) {
    changeListener.forEach(listener -> listener.accept(value));
    return delegate.add(value);
  }

  public static <T>ObservableSet<T> of(Set<T> delegate) {
    return new ObservableSet<>(delegate);
  }
}
