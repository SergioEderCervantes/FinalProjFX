package org.Modules;

@FunctionalInterface
public interface CustomRunnable<T,P> {
    void run(T object, P parameter);
}
