package com.zhaoguhong.blog.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {

  public static void main(String[] args) {
    fuzhiwenj f = new fuzhiwenj();
    boolean a=f.copy("E:\\apache-tomcat-7.0.40.zip", "E:\\我的文档");
    System.out.println(a);
  }

}

class fuzhiwenj {
  boolean copy(String fileFrom, String fileTo) {
    try {
      FileInputStream in = new java.io.FileInputStream(fileFrom);
      FileOutputStream out = new FileOutputStream(fileTo);
      byte[] bt = new byte[204800];
      int count;
      while ((count = in.read(bt)) > 0) {
        out.write(bt, 0, count);
      }
      in.close();
      out.close();
      return true;
    } catch (IOException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}

