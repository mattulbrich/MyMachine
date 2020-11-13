package edu.kit.iti.formal.mymachine.serialise;

import com.thoughtworks.xstream.XStream;
import edu.kit.iti.formal.mymachine.Machine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class XStreamSerialiser implements MachineSerialiser {
    @Override
    public Machine deserialise(InputStream is) throws IOException {

        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream); // to be removed after 1.5
        xstream.allowTypesByWildcard(new String[] {
                "edu.kit.iti.formal.**"
        });

        xstream.setMode(XStream.ID_REFERENCES);
        try (ObjectInputStream ois = xstream.createObjectInputStream(is)) {
            Machine machine = (Machine) ois.readObject();
            return machine;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void serialise(Machine m, OutputStream os) throws IOException {
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream); // to be removed after 1.5
        xstream.allowTypesByWildcard(new String[] {
                "edu.kit.iti.formal.**"
        });
        xstream.setMode(XStream.ID_REFERENCES);
        try(ObjectOutputStream oos = xstream.createObjectOutputStream(os)) {
            oos.writeObject(m);
        }
    }
}
