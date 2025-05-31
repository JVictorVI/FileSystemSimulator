package com.filesystem;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws IOException {
        FileSystemSimulator fs = new FileSystemSimulator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Simulador de Sistema de Arquivos - modo shell");
        System.out.println("Digite 'help' para listar comandos.");
        System.out.println("Digite o caminho de um diretório com '/' (Ex: root/files)");
        System.out.println("Digite o caminho de um arquivo com '/' (Ex: /root/files/doc.txt)");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            String[] parts = line.trim().split(" ");
            if (parts.length == 0) continue;

            String cmd = parts[0].toLowerCase();

            try {
                switch (cmd) {
                    case "help":
                        System.out.println("Comandos: mkdir, touch, ls, rmfile, rmdir, renamefile, renamedir, copyfile, exit");
                        break;

                    case "mkdir":
                        if (parts.length != 3) {
                            System.out.println("Uso: mkdir <caminho_pai> <nome_diretorio>");
                        } else {
                            fs.createDirectory(parts[1], parts[2]);
                        }
                        break;

                    case "touch":
                        if (parts.length != 3) {
                            System.out.println("Uso: touch <caminho_diretorio> <nome_arquivo>");
                        } else {
                            fs.createFile(parts[1], parts[2]);
                        }
                        break;

                    case "ls":
                        if (parts.length != 2) {
                            System.out.println("Uso: ls <caminho_diretorio>");
                        } else {
                            fs.listDirectory(parts[1]);
                        }
                        break;

                    case "rmfile":
                        if (parts.length != 3) {
                            System.out.println("Uso: rmfile <caminho_diretorio> <nome_arquivo>");
                        } else {
                            fs.deleteFile(parts[1], parts[2]);
                        }
                        break;

                    case "rmdir":
                        if (parts.length != 3) {
                            System.out.println("Uso: rmdir <caminho_pai> <nome_diretorio>");
                        } else {
                            fs.deleteDirectory(parts[1], parts[2]);
                        }
                        break;

                    case "renamefile":
                        if (parts.length != 4) {
                            System.out.println("Uso: renamefile <caminho_diretorio> <nome_antigo> <nome_novo>");
                        } else {
                            fs.renameFile(parts[1], parts[2], parts[3]);
                        }
                        break;

                    case "renamedir":
                        if (parts.length != 4) {
                            System.out.println("Uso: renamedir <caminho_pai> <nome_antigo> <nome_novo>");
                        } else {
                            fs.renameDirectory(parts[1], parts[2], parts[3]);
                        }
                        break;

                    case "copyfile":
                        if (parts.length != 4) {
                            System.out.println("Uso: copyfile <caminho_origem> <nome_arquivo> <caminho_destino>");
                        } else {
                            fs.copyFile(parts[1], parts[2], parts[3]);
                        }
                        break;

                    case "exit":
                        System.out.println("Encerrando...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Comando desconhecido: " + cmd);
                }
            } catch (Exception e) {
                System.out.println("Erro ao executar comando: " + e.getMessage());
            }
        }
    }

}



