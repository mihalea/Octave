package ro.mihalea.octave;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Mircea on 29-Sep-14.
 */
public class Main {
    public static java.util.List<Image> icons = new ArrayList<Image>();

    public static void main(String[] args){
        try {
            setupIcons();
            new StatusWindow();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void setupIcons(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        icons.add(toolkit.getImage(Main.class.getResource("/res/icon_16.png")));
        icons.add(toolkit.getImage(Main.class.getResource("/res/icon_64.png")));
    }
}
