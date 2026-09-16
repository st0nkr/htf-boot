package com.teto;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

public interface IFile extends IOptional,IDate {

    default File file(String name) {
        return new File(name);
    }

    default String fileName(String file) {
        String name = new File(file).getName();
        int idx = name.lastIndexOf('.');
        if(idx != -1) {
            return name.substring(0, idx);
        }
        return name;
    }

    default Collection<File> listFilesRecursively(String dir) {
        Collection<File> results = new ArrayList<>();
        if(dir == null) {
            return results;
        }
        if(!new File(dir).exists()) {
            return results;
        }
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                results.addAll(listFilesRecursively(file.getAbsolutePath()));
            } else {
                results.add(file);
            }
        }
        return results;
    }
    default Set<String> listFilesUsingFileWalk(String dir, FileFilter ff) {
        Set<String> results = new HashSet<>();
        if(dir == null) {
            return results;
        }
        if(!new File(dir).exists()) {
            return results;
        }
        Collection<File> allFiles = listFilesRecursively(dir);
        for(File file : allFiles) {
            if(ff.accept(file)) {
                results.add(file.getAbsolutePath());
            }
        }
        return results;
    }

    default boolean saveFile(String fileName, Collection<String> lines) {
        StringBuilder sb = new StringBuilder();
        for(var line : lines) {
            sb.append(line).append("\n");
        }
        return saveFile(fileName, sb.toString());
    }

    default boolean saveFile(String fileName, String contents) {
        try {
             File parent = new File(fileName).getParentFile();
            if(parent != null &&  !parent.exists()) {
                parent.mkdirs();
            }
            FileUtils.writeStringToFile(new File(fileName), contents, Charset.forName("UTF-8"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default Optional<String> readFile(String fileName) {
        if(!fileExists(fileName)) {
            return empty();
        }
        return readFile(new File(fileName));
    }

    default Optional<String> readFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String contents = sb.toString();
                return optional(contents);
            } finally {
                br.close();
            }
        } catch (Exception e) {
            return empty();
        }
    }

    default Collection<String> readFileAsUniqueLines(String fileName) {
        Optional<List<String>> lines = readFileAsLines(new File(fileName));
        final Collection<String> ret = new TreeSet<>();
        if(lines.isPresent()) {
            for(int idx = 0 ; idx < lines.get().size(); idx++) {
                ret.add(lines.get().get(idx));
            }
        }
        return ret;
    }
    default Optional<List<String>> readFileAsLines(File file) {
        Optional<String> contents = readFile(file);
        if (contents.isEmpty()) {
            return Optional.empty();
        }
        String[] lines = contents.get().split("\n");
        return Optional.ofNullable(tidyUpLines(Arrays.asList(lines)));
    }

    default Optional<List<String>> readFileAsLines(String fileName) {
        Optional<String> contents = readFile(fileName);
        if (contents.isEmpty()) {
            return Optional.empty();
        }
        String[] lines = contents.get().split("\n");
        return Optional.ofNullable(tidyUpLines(Arrays.asList(lines)));
    }

    default List<String> tidyUpLines(List<String> list) {
        final List<String> lines = new ArrayList<>();
        for(String line : list) {
            lines.add(line.replace("\r","").replace("\n","").strip());
        }
        return lines;
    }

    default boolean isLink(String name) {
        File f = new File(name);
        boolean isLink = Files.exists(f.toPath(), LinkOption.NOFOLLOW_LINKS);
        return !isLink;
    }
    default boolean fileExists(String name) {
        try {
            File f = new File(name);
            if(isLink(name)) {
                return false;
            }
            return f.isFile() && f.exists();
        } catch (Exception e) {
            return false;
        }
    }
}
