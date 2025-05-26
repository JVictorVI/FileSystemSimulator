package com.filesystem;

public class FSFile {
    private String name;

    public FSFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return "Arquivo: " + name;
    }
}
