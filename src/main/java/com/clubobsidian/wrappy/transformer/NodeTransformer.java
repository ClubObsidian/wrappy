package com.clubobsidian.wrappy.transformer;

public abstract class NodeTransformer<T> {

    private Class<T> clazz;

    public NodeTransformer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClassToTransform() {
        return this.clazz;
    }

    public abstract T transform(T transform);

}