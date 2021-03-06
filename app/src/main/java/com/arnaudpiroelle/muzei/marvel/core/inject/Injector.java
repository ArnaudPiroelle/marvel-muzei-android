package com.arnaudpiroelle.muzei.marvel.core.inject;

import android.content.Context;

import com.arnaudpiroelle.muzei.marvel.core.inject.module.RootModule;

import dagger.ObjectGraph;

public class Injector {
    public static ObjectGraph objectGraph = null;
    public static Context context;

    public static void init(final Object rootModule) {

        if (objectGraph == null) {
            objectGraph = ObjectGraph.create(rootModule);
        } else {
            objectGraph = objectGraph.plus(rootModule);
        }

        // Inject statics
        objectGraph.injectStatics();
    }

    public static void init(final Object rootModule, final Object target) {
        init(rootModule);
        inject(target);
    }

    public static void plus(final Object module) {
        objectGraph = objectGraph.plus(module);
    }

    public static void inject(final Object target) {
        objectGraph.inject(target);
    }

    public static void injectSafely(final Object target) {
        if (objectGraph == null) {
            init(new RootModule());
        }
        inject(target);
    }

    public static <T> T resolve(Class<T> type) {
        return objectGraph.get(type);
    }

    public static Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Tried to retrieve context for injection but is null! Run Injector.setContext() where you're not in the application.");
        }
        return context;
    }

    public static void setContext(Context c) {
        if (context == null) {
            context = c;
        }
    }
}
