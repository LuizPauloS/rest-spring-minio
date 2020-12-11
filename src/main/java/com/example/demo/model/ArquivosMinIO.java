package com.example.demo.model;

public class ArquivosMinIO {

    private String name;
    private String lastModified;
    private long size;

    public ArquivosMinIO(String name, String lastModified, long size) {
        this.name = name;
        this.lastModified = lastModified;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
