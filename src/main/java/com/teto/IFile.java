package com.teto;

import com.teto.command.Context;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public interface IFile extends IOptional,IDate {

    default boolean backUpFile(Context ctx, String fileName) {
        File file = new File(fileName);
        if(file.exists()) {
            String dte = formatDate(new Date());
            String backupFileName = fileName + "."+dte;
            File backupFile = new File(backupFileName);
            if(backupFile.exists()) {
                backupFile.delete();
            }
            file.renameTo(backupFile);
            return true;
        }
        return false;
    }
    default boolean replaceTextsInFile(String fileName, String...replacements) {
        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            List<String> updatedLines = null;
            for(String replacement : replacements) {
                String[] parts = replacement.split(":");

                updatedLines = lines.stream()
                        .map(line -> line.replace(parts[0], parts[1]))
                        .collect(Collectors.toList());
                lines = updatedLines;
            }
            Files.write(Path.of(fileName), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    default boolean copyDirectory(String src, String target) {
        Path sourceDir = Paths.get(src);
        Path targetDir = Paths.get(target);
        System.out.println("Copy "+sourceDir+" -> "+targetDir);
        try {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDirPath = targetDir.resolve(sourceDir.relativize(dir));
                    Files.createDirectories(targetDirPath);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    default File file(String name) {
        return new File(name);
    }
    default Long fileSizeBytes(String fileName) {
        Path path = Paths.get(fileName);

        try {

            // size of a file (in bytes)
            long bytes = Files.size(path);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    default Long fileSizeKiloBytes(String fileName) {
        Path path = Paths.get(fileName);

        try {

            // size of a file (in bytes)
            long kbytes = Files.size(path) / 1024;
            return kbytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    default String fileName(String file) {
        String name = new File(file).getName();
        int idx = name.lastIndexOf('.');
        if(idx != -1) {
            return name.substring(0, idx);
        }
        return name;
    }

    default String fileExtension(File file) {
        if (file == null) {
            return null;
        }
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return "";

    }
    default String mimeType(String fileName) {
        Path filePath = Paths.get(fileName);

        try {
            String mimeType = Files.probeContentType(filePath);
            return mimeType;
        } catch (IOException e) {
           return null;
        }
    }

    default Collection<File> listDirectories(String dir) {

        File dirFile = new File(dir);
        if(!dirFile.exists()) {
            return null;
        }
        File[] dirs = dirFile.listFiles(pathname -> pathname.isDirectory());
        return Arrays.asList(dirs);
    }

    default Collection<String> listDirectoryNames(String dir) {

        File dirFile = new File(dir);
        if(!dirFile.exists()) {
            return null;
        }
        File[] dirs = dirFile.listFiles(pathname -> pathname.isDirectory());
        List<String> set = new ArrayList<>();
        for(File d : dirs) {
            set.add(d.getName());
        }
        Collections.sort(set);
        return set;
    }

    default boolean isRealFile(File f) {
        if(isRealDirectory(f)) {
            return true;
        }
        return false;
    }
    default boolean isRealDirectory(File pathname) {
        File parent = pathname.getParentFile();
        if(parent == null) {
            return true;
        }
        if(isLink(parent)) {
            return false;
        }
        return isRealDirectory(parent);
    }

    default boolean isLink(File pathname) {
        return Files.isSymbolicLink(pathname.toPath());
    }


    default Set<String> listFilesUsingFileWalk(String dir) {
        return listFilesUsingFileWalk(dir, pathname -> {
            if(pathname.isFile()) {
                return true;
            }
            return false;
        });
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

    default void deleteFile(String file) {
        if(fileExists(file)) {
            new File(file).delete();
        }
    }

    default List<String> findFiles(String dir, FileFilter ff) {
        return new ArrayList<>(listFilesUsingFileWalk(dir, ff));
    }
    default boolean appendToFile(String file, String line) {
        PrintWriter out = null;
        try {
            if(!fileExists(file)) {
                new File(file).createNewFile();
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.println(line);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    default boolean appendLineToFile(String file, String line) {
        PrintWriter out = null;
        try {
            if(!fileExists(file)) {
                new File(file).createNewFile();
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.print(line+"\n");
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    default boolean appendToFile(String file, List<String> lines) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            for(String line : lines) {
                out.println(line);
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
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

    default boolean dirExists(String name) {
        if(name == null) {
            return false;
        }
        try {

            File dir = new File(name);
            return dir.isDirectory() && dir.exists();
        } catch (Exception e) {
            return false;
        }
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

    default boolean directoryExists(String name) {
        try {
            File f = new File(name);
            if(isLink(name)) {
                return false;
            }
            return f.isDirectory() && f.exists();
        } catch (Exception e) {
            return false;
        }
    }

    default File createTempFile(Context ctx, String prefix, String postFix, String defaultName) {
        try {
            File file = File.createTempFile(prefix, postFix);
            return file;
        } catch (Exception e) {
            return new File(defaultName);
        }
    }

    default boolean mkdirs(String dir) {
        File d = new File(dir);
        if(!d.exists()) {
            return d.mkdirs();
        }
        return true;
    }

}
