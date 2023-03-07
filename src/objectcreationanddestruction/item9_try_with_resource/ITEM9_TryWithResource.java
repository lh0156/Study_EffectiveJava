package objectcreationanddestruction.item9_try_with_resource;

import java.io.*;

public class ITEM9_TryWithResource {

    //try-finally - 더 이상 자원을 회수하는 최선의 방책 이아니다
    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }

    //자원이 둘 이상이면 try-finally 방식은 너무 지저분하다!
    static void copy (String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        int BUFFERED_SIZE = 1024;

        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFERED_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    static String firstLineOfFileExceptionTest(String path) throws IOException {
        try {
            //readLine();
            throw new IllegalArgumentException();
        } finally {
            //close();
            throw new NullPointerException();
        }
    }

    static String firstLineOfFileWithCatch(String path, String defaultVal) {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }


    }
}
