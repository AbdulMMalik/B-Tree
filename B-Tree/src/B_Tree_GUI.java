
/*
 * (C) 2004 - Geotechnical Software Services
 *
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA  02111-1307, USA.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;



/**
 * G demo program. Demonstrates:
 *
 * <ul>
 * <li>Nested GObject hierarchy
 * <li>Style inheritance
 * <li>Simple selection interaction
 * </ul>
 *
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public class B_Tree_GUI extends JFrame
        implements GInteraction {

    private GScene  scene_;
    GWindow window = new GWindow();
    private BTree bTree = new BTree(new Node(3));
    private JButton button = new JButton("Add Node");
    private JTextField textField = new JTextField("", 20);
    private JLabel label = new JLabel("Enter New Node");
    private ArrayList<Double> xaxis = new ArrayList<>();

    public B_Tree_GUI() {
        super ("G Graphics Library - Demo 8");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String text = textField.getText();
                bTree.addKey(new Key(Integer.valueOf(text)));

                scene_.removeAll();
                xaxis.clear();

                double startX = 440;
                double startY = 100;

                Key[] keys = bTree.getParentKey();
                bTree.printKeys(keys, (int) startX, (int) startY);
//                System.out.println("Parent Node Key is: " + keys[0].value);

                printKeys(keys, startX, startY, null);
                window.redraw();
                scene_.refresh();

            }
        });
        show_Graph();
    }

    JPanel topLevel = new JPanel();

    public void show_Graph(){

        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        // Create the GUI
        topLevel.setLayout (new BorderLayout());
        getContentPane().add (topLevel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(label);
        textField.setSize(50,20);
        buttonPanel.add(textField);
        buttonPanel.add(button);
        JPanel graphicsPanel = new JPanel();
        topLevel.add (buttonPanel, BorderLayout.NORTH);

        // Create the graphic canvas

        topLevel.add (window.getCanvas(), BorderLayout.CENTER);

        // Create scene with default viewport and world extent settings
        scene_ = new GScene (window, "Scene");

        double w0[] = {0.0,    1200.0, 0.0};
        double w1[] = {1200.0, 1200.0, 0.0};
        double w2[] = {0.0,       0.0, 0.0};
        scene_.setWorldExtent (w0, w1, w2);

        GStyle style = new GStyle();
        style.setForegroundColor (new Color (0, 0, 0));
        style.setBackgroundColor (new Color (255, 255, 255));
        style.setFont (new Font ("Dialog", Font.BOLD, 14));
        scene_.setStyle (style);

        // Create som graphic objects

//        GObject[] parentObject = new GObject[3];
        double startX = 440;
        double startY = 100;

        Key[] keys = bTree.getParentKey();

//        GObject gObject = null;
        printKeys(keys, startX, startY, null);

        pack();
        setSize (new Dimension (1500, 1000));
        setVisible (true);
        window.startInteraction (this);

    }

    public void printKeys(Key[] keys, double startX, double startY, GObject gObject){


        //Iterating through all keys of a node.
        for(int i = 0; i < keys.length; i++){

            //Firstly printing the left most keys of all nodes.
            TestObject object = null;
            if(keys[i] != null){

                object = new TestObject(String.valueOf(keys[i].value));
                if(keys[i].leftNode != null) {
                    printKeys(keys[i].leftNode.keys, startX - 30, startY + 100, object);
                }
            }

            //Print the central value.
            if(keys[i] != null) {

                if(xaxis.contains(startX)) {

                    if(i == 0){
                        startX += 30;
                        while (xaxis.contains(startX)) {
                            startX += 30;
                        }
//                        GObject object11 = new TestObject(String.valueOf(keys[i].value), scene_, startX, startY);
                        object.setX_(startX);
                        object.setY_(startY);
                        if(gObject == null || i > 0){

                            object.setParent(scene_);
                        }else {
                            object.setParent(gObject);
                        }

                        scene_.add(object);

                    }else {

                        if(keys[i].value < 10) {
//                            GObject object11 = new TestObject(String.valueOf(keys[i].value), scene_, (startX + 15), startY);
                            object.setX_(startX + 12);
                            object.setY_(startY);
                            if(gObject == null || i > 0){
                                object.setParent(scene_);
                            }else
                                object.setParent(gObject);
                            scene_.add(object);
                        }else if(keys[i].value >= 10){
//                            GObject object = new TestObject(String.valueOf(keys[i].value), scene_, (startX + 25), startY);
                            object.setX_(startX + 20);
                            object.setY_(startY);
                            if(gObject == null || i > 0){
                                object.setParent(scene_);
                            }else
                                object.setParent(gObject);
                            scene_.add(object);
                        }
                    }
                }
                else {
//                    GObject object22 = new TestObject (String.valueOf(keys[i].value), scene_, startX, startY);
                    object.setX_(startX);
                    object.setY_(startY);
                    if(gObject == null || i > 0){
                        object.setParent(scene_);
                    }else
                        object.setParent(gObject);
                    scene_.add(object);
                }

                xaxis.add(startX);
            }

            //At last print the right keys of each node.
            if(keys[i] != null){

                if(keys[i].rightNode != null) {
                    if(keys[i + 1] == null) {
                            printKeys(keys[i].rightNode.keys, startX + 30, startY + 100, object);
                    }
                }
            }

        }
        //Print end line after printing each keys of a node.
    }


    public void event (GScene scene, int event, int x, int y)
    {
        if (event == GWindow.BUTTON1_UP ||
                event == GWindow.BUTTON2_UP) {
            boolean isSelected = event == GWindow.BUTTON1_UP;

            GSegment selectedSegment = scene_.findSegment (x, y);
            if (selectedSegment == null) return;

            GStyle style = selectedSegment.getOwner().getStyle();
            if (style == null) return;

            if (isSelected)
                style.setBackgroundColor (new Color ((float) Math.random(),
                        (float) Math.random(),
                        (float) Math.random()));
            else
                style.unsetBackgroundColor();

            scene_.refresh();
        }
    }

    public class TestObject extends GObject
    {
        private TestObject  parent_;
        private double      x_, y_;
        private GSegment    rectangle_;
        private GSegment    line_;

        GStyle style = new GStyle();

        TestObject (String name) {


            style.setForegroundColor (new Color (0, 0, 0));
            style.setBackgroundColor (new Color (125, 255, 255));
            style.setFont (new Font ("Dialog", Font.BOLD, 14));
            scene_.setStyle (style);

            line_ = new GSegment();
            addSegment (line_);

            rectangle_ = new GSegment();
            addSegment (rectangle_);

            rectangle_.setText (new GText (name, GPosition.MIDDLE));

            setStyle (style);

        }

        public void setParent(GObject parent){
            parent_ = parent instanceof TestObject ? (TestObject) parent : null;
            if(parent_ != null)
            parent_.add(this);
        }

        double getX()
        {
            return x_;
        }


        double getY()
        {
            return y_;
        }

        public void setX_(double x){
            this.x_ = x;
        }
        public void setY_(double y){
            this.y_ = y;
        }


        public void draw()
        {
            if (parent_ != null)
                line_.setGeometry (parent_.getX(), parent_.getY(), x_, y_);

//            circle_.setGeometryXy (Geometry.createCircle (x_, y_, 40.0));
            rectangle_.setGeometryXy(Geometry.createRectangle(x_, y_, 10.0, 10.0));
//            circle_.setStyle();
        }
    }



    public static void main (String[] args)
    {
        new B_Tree_GUI();
    }
}
