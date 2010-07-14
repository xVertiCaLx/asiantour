package com.og.app.gui;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.*;

import java.util.*;

public class TestListField extends MainScreen implements ListFieldCallback {
    
    public static TestListField thisInstance = null;
    private static final String[] _elements = {"First element\n\ntestttt", "Second element", "Third element", "Fourth element", "Fifth element", "6","7","8","9","10", "6","7","8","9","10"};
    private Vector _listElements = new Vector(_elements.length, 1);
    
    public synchronized static TestListField getInstance() {
        if (thisInstance == null) { 
            thisInstance = new TestListField();
        }
        return thisInstance;
    }   
    
    public TestListField() {
        super();
        GuiConst.reinitFont();
        
        //LabelField lblTitle = new LabelField("Asian Tour", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        //setTitle(lblTitle);
    
        //--------------------------- Panel Manager Initialization ----------------------------
        ListField newsPanel = new ListField(0);
        
        //--------------------------- Adding it to the screen ---------------------------------
        //add(newsPanel);
        
        //--------------------------- Horizontal Tab Manager ----------------------------------
        //Draws the list row.
        ColouredListField colourList = new ColouredListField();
        
        //Set the ListFieldCallback
        colourList.setCallback(this);
        
        int elementLength = _elements.length;
        
        //Populate the ListField & ListFieldCallback with data.
        for(int count = 0; count < elementLength; ++count)
        {
            colourList.insert(count);
            this.insert(_elements[count], count);
        }
        add(colourList);
    }
    public void drawListRow(ListField list, Graphics g, int index, int y, int w) 
    {
        
        //We don't need to draw anything here because it is handled
        //by the paint method of our custom ColouredListField.
    }
    
    //Returns the object at the specified index.
    public Object get(ListField list, int index) 
    {
        return _listElements.elementAt(index);
    }
    
    //Returns the first occurence of the given String, bbeginning the search at index, 
    //and testing for equality using the equals method.
    public int indexOfList(ListField list, String p, int s) 
    {
        //return listElements.getSelectedIndex();
        return _listElements.indexOf(p, s);
    }
    
    //Returns the screen width so the list uses the entire screen width.
    public int getPreferredWidth(ListField list) 
    {
        return Graphics.getScreenWidth();
    }
    
    //Adds a String element at the specified index.
    public void insert(String toInsert, int index) 
    {
        _listElements.insertElementAt(toInsert, index);
    }
    
    //Erases all contents.
    public void erase() 
    {
        _listElements.removeAllElements();
    }
} 
