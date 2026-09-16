package com.teto;

import com.teto.command.Context;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public interface IStream {

    default InputStream stringInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    default InputStreamReader stringStreamReader(String str) {
        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));

        // Create an InputStreamReader
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        return reader;
    }

    default InputStream urlStream(String url) {
        try {
            return new URI(url).toURL().openConnection().getInputStream();
        } catch (Exception e) {

        }
        return null;
    }

    default FileInputStream fileInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    default OutputStream stringOutputStream() {
        return new ByteArrayOutputStream();
    }

}
