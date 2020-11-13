package edu.kit.iti.formal.mymachine.serialise;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.panel.Output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface MachineSerialiser {

    Machine deserialise(InputStream is) throws IOException;

    void serialise(Machine m, OutputStream os) throws IOException;
}
