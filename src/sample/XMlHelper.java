package sample;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMlHelper {


    public static void xmlParser(String path) {
        //1.或去SAXParserFactory实例
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2.获取SAXparser实例
        try {
            SAXParser saxParser = factory.newSAXParser();
            //创建Handel对象
            SAXHandel handel = new SAXHandel();
            saxParser.parse(path, handel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class SAXHandel extends DefaultHandler {
        private String key = "";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            key = qName;

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String value = new String(ch, start, length).trim();
            if (!value.equals("") && !key.equals("")) {
                switch (key) {
                    case "path":
                        KeyConfig.getInstance().setPath(value);
                        break;
                    case "storePassword":
                        KeyConfig.getInstance().setStorePassword(value);
                        break;
                    case "keyAlias":
                        KeyConfig.getInstance().setKeyAlias(value);
                        break;
                    case "keyPassword":
                        KeyConfig.getInstance().setKeyPassword(value);
                        break;
                }
                System.out.println(key + "---" + value);
            }
        }
    }


}
