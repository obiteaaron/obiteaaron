package com.github.obiteaaron.bencode;

/**
 * @author obiteaaron
 * @since 2018/4/20 23:11
 */
public class BPair<T> {
    private T result;
    private int current;

    private BPair(T result, int current) {
        this.result = result;
        this.current = current;
    }

    T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    static <T> BPair<T> of(T result, int current) {
        return new BPair<>(result, current);
    }

    static <T> BPair<T> empty() {
        return new BPair<>(null, 0);
    }

    void merge(BPair<T> bPair) {
        this.current = bPair.current;
        this.result = bPair.result;
    }
}
