package com.github.ragudos.kompeter.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUtils {
    public static final Path copyMove(String sourcePath, String targetPath) throws IOException {
        Path s = Paths.get(sourcePath);
        Path t = Paths.get(targetPath);

        Path res = Files.copy(s, t.resolve(s.getFileName()), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Created " + res.toString());

        return res;
    }
}
