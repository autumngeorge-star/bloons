package com.hongbao.bloons.helpers;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Composite disposable registry helper for batch resource teardown.
 * Collects Disposable references as code instantiates them and enables safe batch cleanup.
 */
public class DisposableRegistry implements Disposable {

    private final Set<Disposable> disposablesSet = Collections.newSetFromMap(new IdentityHashMap<>());
    private final List<Disposable> disposablesList = new ArrayList<>();
    private boolean disposed = false;

    /**
     * Registers a Disposable resource with the registry.
     *
     * @param disposable The Disposable resource to register.
     * @param <T>        Type extending Disposable.
     * @return The registered resource for inline chaining.
     */
    public synchronized <T extends Disposable> T register(T disposable) {
        if (disposable == null) {
            return null;
        }
        if (disposed) {
            try {
                disposable.dispose();
            } catch (Exception e) {
                // Suppress exception if already disposed or failed during teardown
            }
            return disposable;
        }
        if (!disposablesSet.contains(disposable)) {
            disposablesSet.add(disposable);
            disposablesList.add(disposable);
        }
        return disposable;
    }

    /**
     * Unregisters a Disposable resource if freed early.
     *
     * @param disposable The Disposable resource to unregister.
     * @return true if removed, false otherwise.
     */
    public synchronized boolean unregister(Disposable disposable) {
        if (disposable == null) {
            return false;
        }
        disposablesList.remove(disposable);
        return disposablesSet.remove(disposable);
    }

    /**
     * Returns the count of currently registered Disposable resources.
     *
     * @return Number of registered disposables.
     */
    public synchronized int size() {
        return disposablesList.size();
    }

    /**
     * Returns whether this DisposableRegistry has been disposed.
     *
     * @return true if disposed, false otherwise.
     */
    public synchronized boolean isDisposed() {
        return disposed;
    }

    /**
     * Disposes all registered Disposable resources in batch and clears the internal tracking collection.
     */
    @Override
    public synchronized void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;

        for (int i = disposablesList.size() - 1; i >= 0; i--) {
            Disposable disposable = disposablesList.get(i);
            if (disposable != null) {
                try {
                    disposable.dispose();
                } catch (Exception e) {
                    // Suppress exceptions from double disposal or early freed native resources
                }
            }
        }
        disposablesList.clear();
        disposablesSet.clear();
    }
}
