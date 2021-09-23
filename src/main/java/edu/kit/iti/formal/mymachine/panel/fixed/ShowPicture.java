package edu.kit.iti.formal.mymachine.panel.fixed;
import edu.kit.iti.formal.mymachine.panel.Picture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class ShowPicture extends Picture {
    private static final java.net.URL URL = makeURL();
    private static final Image IMAGE = makeImage();

    private static URL makeURL() {
        URL result = ShowPicture.class.getResource("/img/displayWindow.png");
        if (result == null) {
            throw new RuntimeException(new FileNotFoundException("displayWindow.png not found"));
        }
        return result;
    }

    private static Image makeImage() {
        try {
            return ImageIO.read(URL);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ShowPicture() {
        super("#ShowPicture", getDim(IMAGE));
    }

    @Override
    protected Image getImage() {
        return IMAGE;
    }

	@Override
	public void changeDesign() {
		// TODO Auto-generated method stub
		
	}
}
