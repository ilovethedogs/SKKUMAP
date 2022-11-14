package swe2022.team6.skkumap.Utilities;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File getFile(String path) throws IOException {
        File result = new File(path);
        if (!result.exists()) {
            return null;
        }
        else {
            return result;
        }
    }
}
