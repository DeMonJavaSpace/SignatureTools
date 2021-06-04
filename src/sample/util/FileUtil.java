package sample.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author DeMon
 * Created on 2021/5/27.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class FileUtil {
    /**
     * 往文件中写入字符串
     *
     * @param filePath 文件保存路径
     * @param content  文件内容
     * @param append   是否追加
     */
    public static void writeTxt(String filePath, String content, boolean append) {
        try {
            File file = new File(filePath);
            //构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(file.getAbsolutePath(), append);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTxt(String filePath, String content) {
        writeTxt(filePath, content, false);
    }

    /**
     * 读取文件中的字符串
     *
     * @param filePath
     * @return
     */
    public static String readText(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(filePath);
            char[] buf = new char[1024];
            int num = 0;
            while ((num = fr.read(buf)) != -1) {
                sb.append(new String(buf, 0, num));
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static boolean reName(File file, String newname) {//文件重命名
        if (file.exists()) {
            File newfile = new File(file.getParent() + File.separator + newname);//创建新名字的抽象文件
            if (file.renameTo(newfile)) {
                System.out.println("重命名成功！");
                return true;
            } else {
                System.out.println("重命名失败！新文件名已存在");
                return false;
            }
        } else {
            System.out.println("重命名文件不存在！");
            return false;
        }

    }


    /**
     * 先根遍历序递归删除文件夹
     *
     * @param dirFile 要被删除的文件或者目录
     * @return 删除成功返回true, 否则返回false
     */
    public static boolean deleteDir(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteDir(file);
            }
        }

        return dirFile.delete();
    }

    /**
     * @param file
     * @param name
     * @return
     */
    public static String createNewFileName(File file, String name) {
        if (name == null || name.length() == 0) {
            return file.getName();
        }
        return getFileName(file) + "-" + name + getFileSuffix(file);
    }

    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 获取不包含后缀的文件名
     *
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        return file.getName().replaceAll("[.][^.]+$", "");
    }

    /**
     * 复制文件
     *
     * @param inFile
     * @param outFile
     * @throws IOException
     */
    public static String copyFile(String inFile, String outFile) throws IOException {
        FileInputStream inStream = new FileInputStream(inFile);
        FileOutputStream outStream = new FileOutputStream(outFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        outStream.flush();
        inStream.close();
        outStream.close();
        return outFile;
    }

    /**
     * 美团Apk渠道方案java版
     *
     * @param apkPath
     * @param channelKey
     * @param channel
     */
    public static void addChannel(String apkPath, String channelKey, String channel) {
        try {
            File file = new File(apkPath).getParentFile();
            if (!file.exists() || Utils.isEmpty(channelKey) || Utils.isEmpty(channel)) {
                return;
            }
            ZipFile zipFile = new ZipFile(apkPath);
            ZipParameters zipParameters = new ZipParameters();
            // 目标路径
            zipParameters.setRootFolderNameInZip("META-INF/");
            File channelFile = new File(file, channelKey + "_" + channel);
            if (!channelFile.exists()) {
                channelFile.createNewFile();
            }
            zipFile.addFile(channelFile, zipParameters);
            channelFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
