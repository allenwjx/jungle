package com.zeh.jungle.client.serializer;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * JAXB xml转换
 *
 * @author allen
 * @version $Id: JaxbSerializer.java, v 0.1 2016年10月27日 下午5:55:25 czj12867 Exp $
 */
public class JaxbSerializer implements Serializer {

    private JaxbSerializer() {

    }

    public static final JaxbSerializer getInstance() {
        return JaxbSerializerHolder.INSTANCE;
    }

    /** 
     * @see com.zeh.jungle.client.serializer.Serializer#serialize(Object, String)
     */
    @Override
    public <T> String serialize(T obj, String dateFormat) {

        String xmlStr = "";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            Writer w = new StringWriter();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, w);

            xmlStr = w.toString();
        } catch (Exception e) {
        }
        return xmlStr;
    }

    /** 
     * @see com.zeh.jungle.client.serializer.Serializer#deserialize(String, Class, String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String text, Class<T> clazz, String dateFormat) {
        T t = null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            t = (T) unmarshaller.unmarshal(new StringReader(text));
        } catch (Exception e) {

        }
        return t;
    }

    private static final class JaxbSerializerHolder {
        private static final JaxbSerializer INSTANCE = new JaxbSerializer();
    }
}
