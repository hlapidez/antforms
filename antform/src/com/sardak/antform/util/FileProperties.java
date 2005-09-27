package com.sardak.antform.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * The FileProperties class allows to store properties without changing the
 * formatting of the file. Already existing properties are updated others are
 * added to the end of the file.
 * 
 * 
 * 
 * @see java.util.Properties
 * 
 * @author k3rmitt
 * 
 */
public class FileProperties {

    private StringWriter m_stringWriter;

    private Properties m_props;

    private File m_fileProperty;

    public FileProperties(File pFile) throws IOException {

        if (pFile == null)
            throw new IOException("Invalid file");

        m_stringWriter = new StringWriter();
        m_props = new Properties();
        this.m_fileProperty = pFile;

        if (pFile.exists()) {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(pFile));
            m_props = new Properties();
            m_props.load(bis);
            bis.close();

            bis = new BufferedInputStream(new FileInputStream(pFile));
            m_stringWriter = new StringWriter();
            int c;
            while ((c = bis.read()) != -1)
                m_stringWriter.write(c);
            bis.close();

        }
    }

    /**
     * 
     * Save to the file the properties.
     * 
     * @param the
     *            properties to save
     * @throws IOException
     *             if something goes wrong
     */
    public void store(Properties p) throws IOException {
        if (p == null)
            return;
        Set set = p.entrySet();
        Iterator iter = set.iterator();

        if (!iter.hasNext())
            return;

        StringBuffer sb = m_stringWriter.getBuffer();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (m_props.containsKey(key)) {
                int start = sb.indexOf(key);
                int starteq = sb.indexOf("=", start);
                int end = sb.indexOf("\n", starteq);
                if (end == -1)
                    end = sb.length() - 1;
                sb.replace(starteq + 1, end, value);
            } else {
                sb.append("\n" + key + "=" + value);
            }
        }

        if (!m_fileProperty.exists())
            m_fileProperty.createNewFile();
        PrintWriter pw = new PrintWriter(new FileOutputStream(m_fileProperty));
        pw.write(sb.toString());
        pw.close();

    }

    public Set keySet() {

        return m_props.keySet();

    }

    public String getProperty(String key) {
        return m_props.getProperty(key);

    }

    public static void main(String[] args) {

        try {
            FileProperties fp = new FileProperties(new File("test.properties"));

            Properties p = new Properties();
            p.put("gc.home.dir", "d:/javadev/src/GenCommons");
            p.put("gc.debug", "false");
            p.put("gc.zzz", "abcdetytut");
            p.put("gc.AAA", "abcdetytut");

            fp.store(p);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
