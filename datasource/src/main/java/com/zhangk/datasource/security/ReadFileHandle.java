package com.zhangk.datasource.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * 文件处理工具类
 * Created by zhangbin on 16/6/4.
 */
public class ReadFileHandle {

    protected static final Log logger = LogFactory.getLog(ReadFileHandle.class);

    /**
     * 读取文本
     *
     * @param fileName
     */
    public static String readFileByChars(String fileName) {
        File file = new File(fileName);
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        OutputStream outputStream = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            inputReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputReader);


            String line = bufferReader.readLine();
            return line;

        } catch (IOException e) {
            logger.error("文件读取异常!===========================");
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputReader != null) {
                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
