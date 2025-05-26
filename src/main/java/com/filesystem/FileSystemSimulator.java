package com.filesystem;

import java.util.List;
import java.util.Optional;

public class FileSystemSimulator {

    private final Directory root;
    private final Journal journal;

    public FileSystemSimulator() {
        root = new Directory("root");
        journal = new Journal();
    }

    public void replayJournal() {
        List<String> commands = journal.readCommands();
        for (String cmd : commands) {
            String[] parts = cmd.split(" ");
            switch (parts[0]) {
                case "CREATE_DIR":
                    createDirectory(parts[1], parts[2], false);
                    break;
                case "CREATE_FILE":
                    createFile(parts[1], parts[2], false);
                    break;
                case "DELETE_FILE":
                    deleteFile(parts[1], parts[2], false);
                    break;
                case "DELETE_DIR":
                    deleteDirectory(parts[1], parts[2], false);
                    break;
                case "RENAME_FILE":
                    renameFile(parts[1], parts[2], parts[3], false);
                    break;
                case "RENAME_DIR":
                    renameDirectory(parts[1], parts[2], parts[3], false);
                    break;
                default:
                    System.err.println("Comando desconhecido no journal: " + cmd);
            }
        }
    }

    private Directory findDirectory(String path) {
        if (path.equals("root")) return root;
        String[] names = path.split("/");
        Directory current = root;
        
        for (int i = 1; i < names.length; i++) {
            final int index = i;
            Optional<Directory> next = current.getSubDirectories().stream()
            .filter(d -> d.getName().equals(names[index]))
            .findFirst();
            
            if (next.isPresent()) {
                current = next.get();
            } else {
                return null;
            }
        }

        return current;
    }

    public void createDirectory(String parentPath, String dirName) {
        createDirectory(parentPath, dirName, true);
    }

    private void createDirectory(String parentPath, String dirName, boolean log) {
    
        Directory parent = findDirectory(parentPath);
    
        if (parent != null) {
    
            boolean exists = parent.getSubDirectories().stream()
                .anyMatch(d -> d.getName().equals(dirName));

                if (exists) {        
                    System.out.println("Diretório já existe: " + dirName);
                    return;
                }

        Directory newDir = new Directory(dirName);
        parent.getSubDirectories().add(newDir);

        if (log) journal.logCommand("CREATE_DIR " + parentPath + " " + dirName);
    
        } else {
            System.out.println("Diretório pai não encontrado: " + parentPath);
        }
    }

    public void createFile(String dirPath, String fileName) {
        createFile(dirPath, fileName, true);
    }

    private void createFile(String dirPath, String fileName, boolean log) {
        Directory dir = findDirectory(dirPath);
    
        if (dir != null) {
            boolean exists = dir.getFiles().stream()
                    .anyMatch(f -> f.getName().equals(fileName));

            if (exists) {
                System.out.println("Arquivo já existe: " + fileName);
                return;
            }

            dir.getFiles().add(new FSFile(fileName));
            if (log) journal.logCommand("CREATE_FILE " + dirPath + " " + fileName);
        } else {
            System.out.println("Diretório não encontrado: " + dirPath);
        }
    }

    public void deleteFile(String dirPath, String fileName) {
        deleteFile(dirPath, fileName, true);
    }

    private void deleteFile(String dirPath, String fileName, boolean log) {
        Directory dir = findDirectory(dirPath);
        if (dir != null) {
            dir.getFiles().removeIf(f -> f.getName().equals(fileName));
            if (log) journal.logCommand("DELETE_FILE " + dirPath + " " + fileName);
        }
    }

    public void deleteDirectory(String parentPath, String dirName) {
        deleteDirectory(parentPath, dirName, true);
    }

    private void deleteDirectory(String parentPath, String dirName, boolean log) {
        Directory parent = findDirectory(parentPath);
        if (parent != null) {
            parent.getSubDirectories().removeIf(d -> d.getName().equals(dirName));
            if (log) journal.logCommand("DELETE_DIR " + parentPath + " " + dirName);
        }
    }

    public void renameFile(String dirPath, String oldName, String newName) {
        renameFile(dirPath, oldName, newName, true);
    }

    private void renameFile(String dirPath, String oldName, String newName, boolean log) {
        Directory dir = findDirectory(dirPath);
        if (dir != null) {
            for (FSFile f : dir.getFiles()) {
                if (f.getName().equals(oldName)) {
                    f.rename(newName);
                    if (log) journal.logCommand("RENAME_FILE " + dirPath + " " + oldName + " " + newName);
                    break;
                }
            }
        }
    }

    public void renameDirectory(String parentPath, String oldName, String newName) {
        renameDirectory(parentPath, oldName, newName, true);
    }

    private void renameDirectory(String parentPath, String oldName, String newName, boolean log) {
        Directory parent = findDirectory(parentPath);
        if (parent != null) {
            for (Directory d : parent.getSubDirectories()) {
                if (d.getName().equals(oldName)) {
                    d.setName(newName);
                    if (log) journal.logCommand("RENAME_DIR " + parentPath + " " + oldName + " " + newName);
                    break;
                }
            }
        }
    }

    public void listDirectory(String path) {
        Directory dir = findDirectory(path);
        if (dir != null) {
            System.out.println("Conteúdo do diretório '" + path + "':");
            for (Directory d : dir.getSubDirectories()) {
                System.out.println("[DIR] " + d.getName());
            }
            for (FSFile f : dir.getFiles()) {
                System.out.println("[FILE] " + f.getName());
            }
        } else {
            System.out.println("Diretório '" + path + "' não encontrado.");
        }
    }

    public String listDirectoryString(String path) {
    Directory dir = findDirectory(path);
    if (dir != null) {
        StringBuilder builder = new StringBuilder();

        for (FSFile file : dir.getFiles()) {
            builder.append("ARQUIVO: ").append(file.getName()).append("\n");
        }
        for (Directory sub : dir.getSubDirectories()) {
            builder.append("DIRETÓRIO: ").append(sub.getName()).append("\n");
        }

        journal.logCommand("LIST_DIR " + path);
        return builder.toString();
    } else {
        return "Diretório não encontrado: " + path;
    }
}

}
